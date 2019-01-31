/*
 *                               .__
 *      ____   _______    ____   |  |   _____
 *    _/ __ \  \_  __ \  /  _ \  |  |   \__  \
 *    \  ___/   |  | \/ (  <_> ) |  |__  / __ \_
 *     \___  >  |__|     \____/  |____/ (____  /
 *         \/                                \/
 *
 *    - - - - - bake.liu@outlook.com - - - - -
 *
 */
package org.erola.spring.boot.netty.httprouter.interceptor;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import java.lang.reflect.Method;

/**
 *
 * @description: 拦截器接口空实现，继承此类不必实现拦截器接口中定义的所有方法
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-20
 *
 */
public class SimpleInterceptorAdapter implements IInterceptor {
    @Override
    public boolean preHandle(FullHttpRequest request, FullHttpResponse response, Method handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(FullHttpRequest request, FullHttpResponse response, Method handler, Object ret) throws Exception {

    }

    @Override
    public void afterCompletion(FullHttpRequest request, FullHttpResponse response, Method handler, Exception ex) throws Exception {

    }
}