package org.erola.spring.boot.samples.netty.httprouter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import org.erola.spring.boot.netty.httprouter.exception.RouterInvokeErrorException;
import org.erola.spring.boot.netty.httprouter.exception.RouterNotFoundException;
import org.erola.spring.boot.netty.httprouter.exception.ServiceUnavailableException;
import org.erola.spring.boot.netty.httprouter.router.IRouteDispatcher;
import org.erola.spring.boot.samples.netty.httprouter.model.ResponseModel;
import org.erola.spring.boot.samples.netty.httprouter.response.*;
import java.util.concurrent.ExecutionException;

public class HttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    /**
     * 路由分发器实例
     */
    private IRouteDispatcher routeDispatcher;

    /**
     * 构造函数
     * @param routeDispatcher
     */
    public HttpServerHandler(IRouteDispatcher routeDispatcher){
        this.routeDispatcher = routeDispatcher;
    }

    /**
     * 非业务异常统一处理
     * @param ctx
     * @param e
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable e) {
        if(e instanceof RouterNotFoundException){
           /*
             * 响应404
             */
            IHttpResponseWapper responseWapper = new NotFondResponseWapper(ctx);
            responseWapper.write();
        }else if(e instanceof ServiceUnavailableException){
            /*
             * 响应503
             */
            IHttpResponseWapper response = new UnavailableResponseWapper(ctx, e);
            response.write();
        }else{
            /*
             * 其他按照500响应
             */
            IHttpResponseWapper response = new ErrorResponseWapper(ctx, e);
            response.write();
        }
        ctx.close();
    }

    /**
     *
     * @param ctx
     * @param request
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws ExecutionException, InterruptedException, IllegalAccessException {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CREATED);
        try{
            Object responseData = routeDispatcher.doDispatch(request, response);
            IHttpResponseWapper responseWapper = new JsonResponseWapper(ctx, request, response, responseData);
            responseWapper.write();
        }catch (RouterInvokeErrorException e){
            /*
             * 业务逻辑异常处理
             */
            ResponseModel responseData = ResponseModel.getErrorResponse(e.getMessage());
            IHttpResponseWapper responseWapper = new JsonResponseWapper(ctx, request, response, responseData);
            responseWapper.write();
        }
    }
}