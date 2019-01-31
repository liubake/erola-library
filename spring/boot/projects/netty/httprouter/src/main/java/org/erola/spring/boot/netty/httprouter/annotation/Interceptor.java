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
package org.erola.spring.boot.netty.httprouter.annotation;

import java.lang.annotation.*;

/**
 *
 * @description: 标识单个拦截器的注解
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-13
 *
 */
@Documented
@Inherited
@Repeatable(MultiInterceptor.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Interceptor {
    /**
     * 拦截器顺序
     * @return
     */
    int order() default 0;

    /**
     * 拦截器对应的实例类型
     * @return
     */
    Class value();
}