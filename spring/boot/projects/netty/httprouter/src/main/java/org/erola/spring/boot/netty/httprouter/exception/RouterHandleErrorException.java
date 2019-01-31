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
 * @description: 用于标识路由处理失败异常
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-20
 *
 */
public class RouterHandleErrorException extends RuntimeException {
    /**
     *
     */
    public RouterHandleErrorException(){
        super("500");
    }

    /**
     *
     * @param message
     */
    public RouterHandleErrorException(String message){
        super(message);
    }

    /**
     *
     * @param cause
     */
    public RouterHandleErrorException(Throwable cause){
        super(cause);
    }
}