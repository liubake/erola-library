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
 * @description: 标识多重拦截器的注解
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-13
 *
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface MultiInterceptor {
    /**
     * 多个 Interceptor
     * @return
     */
    Interceptor[] value() default {};
}