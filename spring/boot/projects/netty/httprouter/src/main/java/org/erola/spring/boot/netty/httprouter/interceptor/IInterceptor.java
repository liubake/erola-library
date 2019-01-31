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
 * @description: 拦截器接口定义
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-13
 *
 */
public interface IInterceptor {

    /**
     * 在进行 action 调用前执行
     * @param request
     * @param response
     * @param handler
     * @return 如果返回 false 则跳过 action 和 postHandle 调用，直接执行 afterCompletion
     * @throws Exception
     */
    boolean preHandle(FullHttpRequest request, FullHttpResponse response, Method handler)throws Exception;

    /**
     * 在进行 action 调用后执行
     * @param request
     * @param response
     * @param handler
     * @param ret
     * @throws Exception
     */
    void postHandle(FullHttpRequest request, FullHttpResponse response, Method handler, Object ret)throws Exception;

    /**
     * afterCompletion 调用始终在最后执行
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    void afterCompletion(FullHttpRequest request, FullHttpResponse response, Method handler, Exception ex)throws Exception;

}
