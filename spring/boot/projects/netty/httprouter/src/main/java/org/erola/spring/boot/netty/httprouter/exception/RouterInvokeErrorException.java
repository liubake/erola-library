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
 * @description: 用于标识方法调用失败异常（包装业务逻辑异常，方便统一处理）
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-20
 *
 */
public class RouterInvokeErrorException extends RuntimeException {
    /**
     *
     */
    public RouterInvokeErrorException(){
        super("InvokeError");
    }

    /**
     *
     * @param message
     */
    public RouterInvokeErrorException(String message){
        super(message);
    }

    /**
     *
     * @param cause
     */
    public RouterInvokeErrorException(Throwable cause){
        super(cause);
    }

    /**
     *
     * @param cause
     */
    public RouterInvokeErrorException(String message, Throwable cause){
        super(message, cause);
    }
}