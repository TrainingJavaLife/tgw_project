package com.tgw360.uip.exception;

/**
 * DAO层的异常
 * Created by 邹祥 on 2017/11/23 9:16
 */
public class UipDataException extends RuntimeException {

    public UipDataException(Throwable cause) {
        super(cause);
    }

    public UipDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public UipDataException(String message) {
        super(message);
    }
}
