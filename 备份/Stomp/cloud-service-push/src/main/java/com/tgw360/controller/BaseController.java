package com.tgw360.controller;

import com.tgw360.exception.StompWrongRequestException;
import org.apache.commons.lang3.StringUtils;

/**
 * 所有Controller的公共父类
 * Created by 危宇 on 2018/1/10 19:32
 */
public abstract class BaseController {

    /*
     * 提供一些参数的非空校验
     */

    /**
     * 非空校验
     *
     * @param param
     * @param message
     */
    public void assertNotNull(Object param,String message){
        if (param == null){
            throw new StompWrongRequestException(message);
        }
    }

    /**
     * 字符串非空校验。不为Null且不为空白串
     *
     * @param param
     * @param message
     */
    public void assertNotBlank(String param,String message){
        if (StringUtils.isBlank(param)) {
            throw new StompWrongRequestException(message);
        }
    }
}
