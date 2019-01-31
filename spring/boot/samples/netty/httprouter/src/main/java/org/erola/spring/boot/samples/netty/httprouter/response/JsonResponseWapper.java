package org.erola.spring.boot.samples.netty.httprouter.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import java.nio.charset.StandardCharsets;

public class JsonResponseWapper extends BaseResponseWapper {
    /**
     * 响应对象
     */
    private Object responseData;

    private FullHttpRequest request;

    private FullHttpResponse response;

    /**
     * 构造函数
     * @param context
     * @param request
     * @param response
     */
    public JsonResponseWapper(ChannelHandlerContext context, FullHttpRequest request, FullHttpResponse response, Object responseData) {
        super(context);
        this.request = request;
        this.response = response;
        this.responseData = responseData;
    }

    /**
     * @see BaseResponseWapper#getKeepAlive()
     * @return
     */
    @Override
    protected boolean getKeepAlive() {
        return request!=null ? HttpUtil.isKeepAlive(request) : false;
    }

    /**
     * @see BaseResponseWapper#getResponse()
     * @return
     */
    @Override
    protected FullHttpResponse getResponse() {
        String jsonResponse = "";
        if(responseData != null){
            try {
                jsonResponse = JsonHelper.objectMapper.writeValueAsString(responseData);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        response.setStatus(HttpResponseStatus.OK);
        response.headers().set(HttpHeaderNames.CONTENT_TYPE, String.format("application/json; charset=%s", StandardCharsets.UTF_8.name()));
        response = response.replace(Unpooled.wrappedBuffer(jsonResponse.getBytes(StandardCharsets.UTF_8)));
        return response;
    }

    /**
     * json序列化/反序列化辅助类
     */
    private static class JsonHelper{
        /**
         * ObjectMapper 单例
         */
        private static final ObjectMapper objectMapper = getObjectMapper();

        /**
         * 获取 ObjectMapper 实例
         * @return
         */
        private static final ObjectMapper getObjectMapper(){
            ObjectMapper ret=new ObjectMapper();
            ret.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return ret;
        }
    }
}