package com.tgw360.uip.common;

/**
 * 通用的返回结果
 *
 * @param <T> 响应的数据类型
 * @author 邹祥
 * @date 2017年11月14日 上午10:04:54
 */
public class CommonResult<T> extends BaseObject {

    public final static CommonResult FAILURE_RESULT =
            new CommonResult(CommonResult.STATUS_CODE_FAILURE, CommonResult.MESSAGE_FAILURE);
    public final static CommonResult TOKEN_INVALID_RESULT =
            new CommonResult(CommonResult.STATUS_CODE_TOKEN_INVALID, CommonResult.MESSAGE_TOKEN_INVALID);

    private static final long serialVersionUID = 1L;

    /**
     * 状态码——成功
     */
    public static final int STATUS_CODE_SUCCESS = 1001;
    /**
     * 状态码——失败
     */
    public static final int STATUS_CODE_FAILURE = 9999;
    /**
     * 状态码——请求参数错误
     */
    public static final int STATUS_CODE_WRONG_PARAMETER = 5002;
    /**
     * token已经失效
     */
    public static final int STATUS_CODE_TOKEN_INVALID = 5003;

    public static final String MESSAGE_SUCCESS = "请求成功";
    public static final String MESSAGE_FAILURE = "请求失败";
    public static final String MESSAGE_WRONG_PARAMETER = "请求参数错误";
    public static final String MESSAGE_TOKEN_INVALID = "token已经失效";
    /**
     * 状态码
     */
    private Integer code = STATUS_CODE_SUCCESS;
    /**
     * 状态信息
     */
    private String message = MESSAGE_SUCCESS;

    private T data;

    public CommonResult() {

    }

    public CommonResult(T data) {
        this.data = data;
    }

    public CommonResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

	/*
     * Getters & Setters
	 */

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
