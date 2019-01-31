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
package org.erola.spring.boot.netty.httprouter.exception;

/**
 *
 * @description: 用于标识路由匹配失败异常
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-20
 *
 */
public class RouterNotFoundException extends RuntimeException {
    /**
     *
     */
    public RouterNotFoundException(){
        super("404");
    }

    /**
     *
     * @param message
     */
    public RouterNotFoundException(String message){
        super(message);
    }
}