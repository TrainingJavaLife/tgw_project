package com.tgw360.uip.controller;

import com.tgw360.uip.exception.UipWrongRequestParameterException;
import org.apache.commons.lang3.StringUtils;

/**
 * 所有Controller的公共父类
 * Created by 邹祥 on 2017/12/15 11:11
 */
public abstract class BaseController {

    /*
     * 提供一些参数校验的方法
     */

    /**
     * 非空校验
     *
     * @param param
     * @param message
     */
    public void assertNotNull(Object param, String message) {
        if (param == null)
            throw new UipWrongRequestParameterException(message);
    }

    /**
     * 字符串非空校验。不为Null且不为空白串
     *
     * @param param
     * @param message
     */
    public void assertNotBlank(String param, String message) {
        if (StringUtils.isBlank(param))
            throw new UipWrongRequestParameterException(message);
    }


}
