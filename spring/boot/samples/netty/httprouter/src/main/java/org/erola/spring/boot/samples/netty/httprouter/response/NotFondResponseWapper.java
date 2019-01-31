package org.erola.spring.boot.samples.netty.httprouter.response;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

public class NotFondResponseWapper extends BaseResponseWapper {
    /**
     * 构造函数
     * @param context
     */
    public NotFondResponseWapper(ChannelHandlerContext context) {
        super(context);
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
        FullHttpResponse ret = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NOT_FOUND, Unpooled.wrappedBuffer("resources not found".getBytes()));
        return ret;
    }
}