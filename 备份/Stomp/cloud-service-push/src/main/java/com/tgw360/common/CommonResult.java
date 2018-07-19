package com.tgw360.common;

import java.io.Serializable;

/**
 * 通用的返回类型
 *
 * @param <T> 响应的数据类型
 * Created by 危宇 on 2018/1/9 17:24
 */
public class CommonResult<T> implements Serializable {

    public static CommonResult FAILURE_RESULT =
            new CommonResult(CommonResult.STATUS_CODE_FAILURE,CommonResult.MESSAGE_FAILURE);

    public static CommonResult TOKEN_INVALID_RESULT =
            new CommonResult(CommonResult.STATUS_CODE_TOKEN_INVALID,CommonResult.MESSAGE_TOKEN_INVALID);

    private static final long serialVersionUID = 1L;

    /**
     * 状态码-成功
     */
    public static final int STATUS_CODE_SUCCESS = 0;

    /**
     * 状态码-失败
     */
    public static final int STATUS_CODE_FAILURE = 1;

    /**
     * 状态码-请求参数错误
     */
    public static final int STATUS_CODE_WRONG_PARAMTER = 40001;

    /**
     * token已经失效
     */
    public static final int STATUS_CODE_TOKEN_INVALID = 40001;

    public static final String MESSAGE_SUCCESS = "Success";
    public static final String MESSAGE_FAILURE = "Failure";
    public static final String MESSAGE_WRONG_PARAMTER = "请求参数错误";
    public static final String MESSAGE_TOKEN_INVALID = "token已经失效";

    /**
     * 状态码
     */
    private Integer errcode = STATUS_CODE_SUCCESS;

    /**
     * 状态信息
     */
    private String errmsg = MESSAGE_SUCCESS;

    private T data;

    public CommonResult() {
    }

    public CommonResult(T data) {
        this.data = data;
    }

    public CommonResult(Integer errcode, String errmsg) {
        this.errcode = errcode;
        this.errmsg = errmsg;
    }

    public CommonResult(Integer errcode, String errmsg,T data) {
        this.errcode = errcode;
        this.errmsg = errmsg;
        this.data = data;
    }

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
