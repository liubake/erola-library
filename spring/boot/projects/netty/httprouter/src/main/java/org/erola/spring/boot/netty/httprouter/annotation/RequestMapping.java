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

import org.springframework.core.annotation.AliasFor;
import java.lang.annotation.*;

/**
 *
 * @description: 路由映射注解
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-13
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface RequestMapping {
    /**
     * 路由名
     * @return
     */
    String name() default "";

    /**
     * 路由映射的路径列表
     * @return
     */
    @AliasFor("path")
    String[] value() default {};

    /**
     * 路由映射的路径列表
     * @return
     */
    @AliasFor("value")
    String[] path() default {};

    /**
     * 路由映射的请求方法列表
     * @return
     */
    RequestMethod[] method() default {};
}