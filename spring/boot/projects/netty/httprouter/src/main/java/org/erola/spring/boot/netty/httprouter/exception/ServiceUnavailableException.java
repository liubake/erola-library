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
 * @description: 用于标识服务不可用异常
 *
 * @email: bake.liu@outlook.com
 *
 * @create: 2019-01-20
 *
 */
public class ServiceUnavailableException extends RuntimeException {
    /**
     *
     */
    public ServiceUnavailableException(){
        super("503");
    }

    /**
     *
     * @param message
     */
    public ServiceUnavailableException(String message){
        super(message);
    }

    /**
     *
     * @param cause
     */
    public ServiceUnavailableException(Throwable cause){
        super(cause);
    }
}