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
import org.erola.common.util.CollectionUtil;
import org.erola.spring.boot.netty.httprouter.annotation.Controller;
import org.erola.spring.boot.netty.httprouter.annotation.Interceptor;
import org.erola.spring.boot.netty.httprouter.exception.RouterHandleErrorException;
import org.erola.spring.boot.netty.httprouter.exception.RouterInvokeErrorException;
import org.erola.spring.boot.netty.httprouter.exception.RouterNotFoundException;
import org.erola.spring.boot.netty.httprouter.exception.ServiceUnavailableException;
import org.erola.spring.boot.netty.httprouter.interceptor.IInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @description: 路由分发实现
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-13
 *
 */
public class RouteDispatcher implements IRouteDispatcher {
    /**
     * RouterTable实例
     */
    private RouterTable routerTable;
    /**
     * spring容器上下文
     */
    private ApplicationContext applicationContext;

    /**
     *
     * @param caseSensitive
     * @param usePathCache
     * @param pathCacheMaxCount
     */
    public RouteDispatcher(boolean caseSensitive, boolean usePathCache, int pathCacheMaxCount){
        this.routerTable = new RouterTable(caseSensitive, usePathCache, pathCacheMaxCount);
    }

    /**
     * 获取容器中指定类型的bean
     * @param requiredType
     * @return
     */
    private Object getBean(Class<?> requiredType) {
        if(applicationContext == null){
            throw new ServiceUnavailableException("RouteDispatcher.ApplicationContext not Injected");
        }
        return applicationContext.getBean(requiredType);
    }

    /**
     * 初始化路由配置，并保存ApplicationContext实例
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String,Object> beans = applicationContext.getBeansWithAnnotation(Controller.class);
        if(beans != null){
            beans.values().stream().forEach(x->routerTable.register(x.getClass()));
        }
        this.applicationContext = applicationContext;
    }

    /**
     * @see IRouteDispatcher#doDispatch(FullHttpRequest, FullHttpResponse)
     * @param request
     * @param response
     * @return
     * @throws IllegalAccessException
     */
    @Override
    public Object doDispatch(FullHttpRequest request, FullHttpResponse response) {
        PatternInfo patternInfo = routerTable.getPatternInfo(request);
        if(patternInfo == null){
            throw new RouterNotFoundException();
        }else{
            Object controllerInstance = getBean(patternInfo.getControllerClass());
            if(controllerInstance != null){
                Object ret = null;
                Method method = null;
                int interceptorIndex = 0;
                Exception dispatchException = null;
                List<IInterceptor> interceptorList = new ArrayList<>();
                try {
                    method = patternInfo.getMethod();
                    Object[] paramObjects = null;
                    Class[] paramTypes = method.getParameterTypes();
                    if(paramTypes!=null && paramTypes.length>0){
                        paramObjects = new Object[paramTypes.length];
                        for(int index=0; index<paramTypes.length; index++){
                            if(FullHttpRequest.class.isAssignableFrom(paramTypes[index])){
                                paramObjects[index] = request;
                            }else if(FullHttpResponse.class.isAssignableFrom(paramTypes[index])){
                                paramObjects[index] = response;
                            }
                        }
                    }
                    List<Interceptor> interceptorAnnotationList = patternInfo.getInterceptorList();
                    if(CollectionUtil.isNotNullOrEmpty(interceptorAnnotationList)){
                        interceptorAnnotationList.stream().forEach(interceptorAnnotation->{
                            IInterceptor interceptorInstance = (IInterceptor)getBean(interceptorAnnotation.value());
                            if(interceptorInstance != null){
                                interceptorList.add(interceptorInstance);
                            }
                        });
                    }
                    int interceptorSize = interceptorList.size();
                    if(interceptorSize > 0){
                        for(; interceptorIndex<interceptorSize; interceptorIndex++){
                            if(!interceptorList.get(interceptorIndex).preHandle(request, response, method)){
                                break;
                            }
                        }
                    }
                    if(interceptorIndex == interceptorSize){
                        ret = patternInfo.getMethod().invoke(controllerInstance, paramObjects);
                        for(int i=interceptorIndex-1; i>=0; i--){
                            interceptorList.get(i).postHandle(request, response, method, ret);
                        }
                    }
                } catch (Exception e) {
                    dispatchException = e;
                }finally {
                    for(int i=interceptorIndex-1; i>=0; i--){
                        try {
                            interceptorList.get(i).afterCompletion(request, response, method, dispatchException);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if(dispatchException != null) {
                        throw new RouterInvokeErrorException(dispatchException);
                    }else
                        return ret;
                }
            }else {
                throw new RouterHandleErrorException(String.format("can't get the controllerInstance, the router map key is %s", patternInfo.getPattern()));
            }
        }
    }
}