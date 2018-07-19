package com.tgw360.uip.controller.advice;

import com.tgw360.uip.common.CommonResult;
import com.tgw360.uip.exception.UipWrongRequestParameterException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

/**
 * 全局异常处理
 * Created by 邹祥 on 2017/12/5 13:07
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Object exception(Exception e, WebRequest request) {
        if (e instanceof UipWrongRequestParameterException) { // 参数错误
            String msg;
            if (StringUtils.isNotEmpty(e.getMessage())) {
                msg = e.getMessage();
            } else {
                msg = CommonResult.MESSAGE_WRONG_PARAMETER;
            }
            return new CommonResult(CommonResult.STATUS_CODE_WRONG_PARAMETER, msg);
        }
        // TODO: Controller层是否需要打印日志？
        e.printStackTrace();
        return CommonResult.FAILURE_RESULT;
    }

}
