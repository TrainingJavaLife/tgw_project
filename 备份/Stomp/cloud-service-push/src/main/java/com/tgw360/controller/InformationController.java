package com.tgw360.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tgw360.common.CommonResult;
import com.tgw360.entity.Message;
import com.tgw360.entity.UserToken;
import com.tgw360.entity.websocket.VerifyMessage;
import com.tgw360.management.SocketSessionRegistry;
import com.tgw360.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by 危宇 on 2018/1/16 15:29
 */
@RestController
public class InformationController {

    private final Logger logger = LoggerFactory.getLogger(InformationController.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    SocketSessionRegistry webAgentSessionRegistry;

    @Autowired
    private MessageService messageService;

//    @Autowired
//    private UserTokenService userTokenService;

    /**
     * 发送消息
     *
     * @param message
     * @return
     */
//    @RequestMapping(value = "/message/pushMessage/send", method = RequestMethod.GET)
//    public Object sendMessage(String message) {
//        JSONObject jsonObject = JSONObject.parseObject(message); //json字符串转Json对象
//        List<String> keys = webAgentSessionRegistry.getAllSessionIds().entrySet().stream()
//                .map(Map.Entry::getKey)
//                .collect(Collectors.toList());   // 获取所有的会话，再通过循环的方式
//        String sendId = jsonObject.getString("SendId");
//        String content = jsonObject.getString("MessageContent");
//        int msgType = jsonObject.getInteger("MessageType");
//        JSONArray sendToUser = jsonObject.getJSONArray("ToUserId");
//        int result = 0;
//        for (int i = 0; i < sendToUser.size(); i++) {
//            boolean flag = false;
//            for (String key : keys) {
//                if (key.equals(sendToUser.getString(i))) {
//                    flag = true;
//                }
//            }
//            if (flag) {
//                String sessionId = webAgentSessionRegistry.getSessionIds(sendToUser.getString(i)).stream().findFirst().get().toString(); // Java8中新出现的
//                messagingTemplate.convertAndSendToUser(sessionId, "/topic/webSocket", content, createHeaders(sessionId));
//            }
//            result = messageService.addMessage(sendId, sendToUser.getString(i), msgType, content);
//        }
//        if (result>0){
//            return new CommonResult<>(CommonResult.STATUS_CODE_SUCCESS, CommonResult.MESSAGE_SUCCESS);
//        }
//        return CommonResult.FAILURE_RESULT;
//    }

    /**
     * IOS状态栏消息
     * @param message
     * @return
     */
//    @RequestMapping(value = "/message/pushMessage-IOS/send", method = RequestMethod.GET)
//    public Object sendIosMessage(String message){
//        logger.info("被调用");
//        JSONObject jsonObject = JSONObject.parseObject(message); //json字符串转Json对象
//        String sendId = jsonObject.getString("SendId");
//        String content = jsonObject.getString("MessageContent");
//        int msgType = jsonObject.getInteger("MessageType");
//        JSONArray sendToUser = jsonObject.getJSONArray("ToUserId");
//        List<Object> userIdList = Arrays.asList(sendToUser.toArray());
//        List<UserToken> userTokenList = userTokenService.findTokenByIds(userIdList);
//        List<String> tokenList = new ArrayList<>();
//        for (UserToken userToken:userTokenList) {
//            tokenList.add(userToken.getToken());
//        }
//        messageService.sendIOSPush(tokenList,content);
//        int result = 0;
//        for (int i = 0; i < sendToUser.size(); i++) {
//            result = messageService.addMessage(sendId, sendToUser.getString(i), msgType, content);
//        }
//        if (result>0){
//            return new CommonResult<>(CommonResult.STATUS_CODE_SUCCESS, CommonResult.MESSAGE_SUCCESS);
//        }
//        return CommonResult.FAILURE_RESULT;
//    }

    /**
     * 前端确认消息并修改数据库状态
     * @param principal
     * @param verifyMessage 传入确认的消息
     * @throws Exception
     */
//    @MessageMapping("/verify")
//    public void verifyMessageState(Principal principal, VerifyMessage verifyMessage) throws Exception {
//        Set<String> set = webAgentSessionRegistry.getSessionIds(principal.getName());
//        System.out.println("认证的姓名是:" + principal.getName());
//        System.out.println(verifyMessage.getMsgId());
//        String sid = "";
//        int result;
//        if (set.iterator().hasNext()) {
//            sid = set.iterator().next();
//        }
//        String msgId = verifyMessage.getMsgId();
//        result = messageService.updateMessageById(msgId, principal.getName());
//        CommonResult commonResult = CommonResult.FAILURE_RESULT;
//        if (result > 0) {
//            commonResult = new CommonResult<>(CommonResult.STATUS_CODE_SUCCESS, CommonResult.MESSAGE_SUCCESS);
//        }
//        messagingTemplate.convertAndSendToUser(sid, "/topic/verifyMsg", commonResult, createHeaders(sid));
//    }

    /**
     * 发送带表头
     *
     * @param sessionId
     * @return
     */
    private MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);
        return headerAccessor.getMessageHeaders();
    }

    /**
     * 根据用户Id和消息类型查询用户所有相关消息
     * @param userId
     * @param messageType
     * @return
     */
//    @RequestMapping(value = "/message/textMessage/search",method = RequestMethod.GET)
//    public Object findMessageByUser(@RequestParam("userId") long userId,@RequestParam("messageType") int messageType){
//        try {
//            logger.info("被调用");
//            List<Message> messageList = messageService.findMessageByUser(userId, messageType);
//            CommonResult<List<Message>> commonResult = new CommonResult<>();
//            commonResult.setData(messageList);
//            return commonResult;
//        } catch (Exception e) {
//            logger.error("查询用户所有消息异常,异常信息:{}",e.getMessage());
//            return CommonResult.FAILURE_RESULT;
//        }
//    }
}
