package com.tgw360.exception;

/**
 * 请求参数错误
 * Created by 危宇 on 2018/1/9 19:13
 */
public class StompWrongRequestException extends RuntimeException {

    public StompWrongRequestException(String message){
        super(message);
    }
}
