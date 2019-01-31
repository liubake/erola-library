package org.erola.spring.boot.samples.netty.httprouter.controller;

import io.netty.handler.codec.http.FullHttpRequest;
import org.erola.spring.boot.netty.httprouter.annotation.*;
import org.erola.spring.boot.samples.netty.httprouter.interceptor.Test2Interceptor;
import org.erola.spring.boot.samples.netty.httprouter.interceptor.Test3Interceptor;
import org.erola.spring.boot.samples.netty.httprouter.model.ResponseModel;

@Controller(path = "/home")
@Interceptor(order = 1, value = Test2Interceptor.class)
public class HomeController extends BaseController {

    @Interceptor(value = Test3Interceptor.class)
    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public ResponseModel<String> index(FullHttpRequest request){
        return ResponseModel.getSuccessResponse("hello world!");
    }

}