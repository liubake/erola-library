package org.erola.spring.boot.samples.netty.httprouter.controller;

import org.erola.spring.boot.netty.httprouter.annotation.Interceptor;
import org.erola.spring.boot.samples.netty.httprouter.interceptor.Test1Interceptor;

@Interceptor(value = Test1Interceptor.class)
public abstract class BaseController {

}