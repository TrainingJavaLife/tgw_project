package com.tgw360.controller;

import com.tgw360.common.CommonResult;
import com.tgw360.entity.MessageState;
import com.tgw360.service.MessageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * Created by 危宇 on 2018/1/10 19:29
 */
@RestController
public class MessageController extends BaseController {

    private final Logger logger = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;


    /**
     * 获取验证码
     *
     * @param phone
     * @param type
     * @return
     */
    @RequestMapping(value = "/message/identifyCode/acquire", method = RequestMethod.POST)
    public Object acquireIdentifyCode(@RequestParam(value = "phone") String phone,
                                      @RequestParam(value = "type") Integer type){
        assertNotBlank(phone, "手机号不能为空");
        assertNotNull(type, "验证码类型不能为空");
        MessageState identifyCode = messageService.acquireIdentifyCode(phone, type);
        return new CommonResult<>(identifyCode.getCode(),identifyCode.getContent());
    }

    /**
     * 验证验证码
     *
     * @param phone
     * @param code
     * @return
     */
    @RequestMapping(value = "/message/identifyCode/verify", method = RequestMethod.POST)
    public Object verifyIdentifyCode(@RequestParam(value = "phone") String phone,
                                     @RequestParam(value = "code") String code,
                                     @RequestParam(value = "type") Integer type) {
        try {
            logger.info("被调用");
            if (messageService.verifyIdentifyCode(phone, code, type)) {
                return new CommonResult<>(CommonResult.STATUS_CODE_SUCCESS, CommonResult.MESSAGE_SUCCESS);
            }else{
                return CommonResult.FAILURE_RESULT;
            }
        } catch (Exception e) {
            logger.error("验证验证码异常，异常信息：{}",e.getMessage());
            return CommonResult.FAILURE_RESULT;
        }
    }

    /**
     * 发送短信到指定手机
     *
     * @param phone
     * @param content
     * @return
     * @throws RemoteException
     * @throws ServiceException
     * @throws MalformedURLException
     */
    @RequestMapping(value = "/message/textMessage/send", method = RequestMethod.POST)
    public Object sendTextMessage(@RequestParam("phone") String phone, @RequestParam("content") String content){
        try {
            if (StringUtils.isBlank(phone) || StringUtils.isBlank(content)){
                return new CommonResult(CommonResult.STATUS_CODE_WRONG_PARAMTER,CommonResult.MESSAGE_WRONG_PARAMTER);
            }
            MessageState messageState = messageService.sendTextMessage(phone, content);
            return new CommonResult<>(messageState.getCode(),messageState.getContent());
        } catch (Exception e) {
            logger.error("发送短信到指定手机异常，异常信息：{}",e.getMessage());
            return CommonResult.FAILURE_RESULT;
        }
    }

}
