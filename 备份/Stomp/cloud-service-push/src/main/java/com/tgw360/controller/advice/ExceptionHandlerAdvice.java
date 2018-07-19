package com.tgw360.controller.advice;

import com.tgw360.common.CommonResult;
import com.tgw360.exception.StompWrongRequestException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

/**
 * 全局异常处理
 * Created by 危宇 on 2018/1/9 19:18
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

    private final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Object exception(Exception e, WebRequest request){
        if (e instanceof StompWrongRequestException) { // 参数错误
            String message;
            if (StringUtils.isNotEmpty(e.getMessage())){
                message = e.getMessage();
            }else{
                message = CommonResult.MESSAGE_WRONG_PARAMTER;
            }
            return new CommonResult(CommonResult.STATUS_CODE_WRONG_PARAMTER,message);
        }
        // TODO: Controller层是否需要打印日志？
        logger.error("全局拦截异常,异常信息{}",e.getMessage());
        return CommonResult.FAILURE_RESULT;
    }
}
