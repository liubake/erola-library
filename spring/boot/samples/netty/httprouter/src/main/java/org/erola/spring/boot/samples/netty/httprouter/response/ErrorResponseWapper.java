package org.erola.spring.boot.samples.netty.httprouter.response;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class ErrorResponseWapper extends BaseResponseWapper {
    /**
     * 对应的异常对象
     */
    private Throwable error;

    /**
     * 异常响应类构造函数
     * @param context
     * @param error
     */
    public ErrorResponseWapper(ChannelHandlerContext context, Throwable error) {
        super(context);
        this.error = error;
    }

    /**
     * @see BaseResponseWapper#getKeepAlive()
     * @return
     */
    @Override
    protected boolean getKeepAlive() {
        return false;
    }

    /**
     * @see BaseResponseWapper#getResponse()
     * @return
     */
    @Override
    protected FullHttpResponse getResponse() {
        Writer writer = new StringWriter();
        error.printStackTrace(new PrintWriter(writer));
        FullHttpResponse ret = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR, Unpooled.wrappedBuffer(writer.toString().getBytes()));
        return ret;
    }
}