package org.erola.spring.boot.samples.netty.httprouter.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseModel<T> {
    /**
     * 状态
     */
    @JsonProperty(value = "Status")
    public int status;
    /**
     * 附加信息
     */
    @JsonProperty(value = "Message")
    public String message;
    /**
     * 响应对象
     */
    @JsonProperty(value = "Data")
    public T data;

    /**
     * 构造函数
     */
    public ResponseModel(){}

    /**
     * 构造函数
     * @param status
     * @param message
     * @param data
     */
    public ResponseModel(Integer status, String message, T data){
        this.status = status;
        this.message = message;
        this.data = data;
    }

    /**
     * 返回响应成功对象的方法
     * @param value
     * @param <T>
     * @return
     */
    public static <T> ResponseModel<T> getSuccessResponse(T value){
        return new ResponseModel<T>(1, "", value);
    }

    /**
     * 返回响应错误对象的方法
     * @param message
     * @return
     */
    public static ResponseModel<Object> getErrorResponse(String message){
        return new ResponseModel<Object>(0, message, null);
    }
}