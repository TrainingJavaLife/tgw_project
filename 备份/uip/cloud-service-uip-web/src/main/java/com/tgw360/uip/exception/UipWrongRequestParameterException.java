package com.tgw360.uip.exception;

/**
 * "请求参数错误"异常
 * Created by 邹祥 on 2017/12/8 14:21
 */
public class UipWrongRequestParameterException extends RuntimeException {

    public UipWrongRequestParameterException(String message) {
        super(message);
    }
}
