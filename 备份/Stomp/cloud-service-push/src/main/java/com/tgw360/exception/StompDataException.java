package com.tgw360.exception;

/**
 * Dao层的异常
 * Created by 危宇 on 2018/1/9 19:07
 */
public class StompDataException extends RuntimeException{

    public StompDataException(Throwable cause){
        super(cause);
    }

    public StompDataException(String message,Throwable cause){
        super(message,cause);
    }

    public StompDataException(String message){
        super(message);
    }
}
