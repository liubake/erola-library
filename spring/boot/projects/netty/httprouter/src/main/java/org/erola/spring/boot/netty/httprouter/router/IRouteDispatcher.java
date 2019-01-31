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
package org.erola.spring.boot.netty.httprouter.router;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import org.springframework.context.ApplicationContextAware;

/**
 *
 * @description: 路由分发接口定义
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-13
 *
 */
public interface IRouteDispatcher extends ApplicationContextAware {

    /**
     * 根据当前请求及路由规则进行调度
     * @param request
     * @param response
     * @return
     * @throws IllegalAccessException
     */
    Object doDispatch(FullHttpRequest request, FullHttpResponse response);

}