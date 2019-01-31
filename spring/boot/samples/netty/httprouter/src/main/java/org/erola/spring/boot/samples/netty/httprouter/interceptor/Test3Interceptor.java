package org.erola.spring.boot.samples.netty.httprouter.interceptor;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.erola.spring.boot.netty.httprouter.interceptor.SimpleInterceptorAdapter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class Test3Interceptor extends SimpleInterceptorAdapter {

    @Override
    public boolean preHandle(FullHttpRequest request, FullHttpResponse response, Method handler) throws Exception {
        System.out.println("Test3Interceptor   --->   preHandle");
        return true;
    }

    @Override
    public void postHandle(FullHttpRequest request, FullHttpResponse response, Method handler, Object ret) throws Exception {
        System.out.println("Test3Interceptor   --->   postHandle");
    }

    @Override
    public void afterCompletion(FullHttpRequest request, FullHttpResponse response, Method handler, Exception ex) throws Exception {
        System.out.println("Test3Interceptor   --->   afterCompletion");
    }

}