package org.erola.spring.boot.samples.netty.httprouter.response;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;

public abstract class BaseResponseWapper implements IHttpResponseWapper {
    /**
     * 所属的ChannelHandlerContext
     */
    protected ChannelHandlerContext context;

    /**
     * http响应抽象类构造函数
     * @param context
     */
    public BaseResponseWapper(ChannelHandlerContext context){
        this.context = context;
    }

    /**
     * 抽象方法，判断响应头是否需要添加keep-alive
     * @return
     */
    protected abstract boolean getKeepAlive();

    /**
     *
     * @return
     */
    protected abstract FullHttpResponse getResponse();

    /**
     * @see IHttpResponseWapper#write()
     */
    @Override
    public void write(){
        FullHttpResponse response = getResponse();
        response.headers().setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        if(getKeepAlive()){
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
            context.write(response);
        }else{
            context.write(response).addListener(ChannelFutureListener.CLOSE);
        }
        context.flush();
    }
}