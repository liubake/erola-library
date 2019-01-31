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
import org.springframework.stereotype.Component;
import java.lang.annotation.*;

/**
 *
 * @description: 标识 controller 的注解
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-13
 *
 */
@Component
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
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
}