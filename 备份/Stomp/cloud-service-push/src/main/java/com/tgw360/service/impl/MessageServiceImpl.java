package com.tgw360.service.impl;

import com.tgw360.entity.Message;
import com.tgw360.entity.MessageState;
//import com.tgw360.mapper.MessageMapper;
import com.tgw360.repository.MessageRepository;
import com.tgw360.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created by 危宇 on 2018/1/10 19:29
 */
@Service
public class MessageServiceImpl implements MessageService {


    @Autowired
    private MessageRepository messageRepository;

//    @Autowired
//    private MessageMapper messageMapper;

    @Override
    public MessageState acquireIdentifyCode(String phone, Integer type){
          MessageState identifyCode = messageRepository.acquireIdentifyCode(phone,type);
          return identifyCode;
    }

    @Override
    public MessageState sendTextMessage(String phone, String content){
        MessageState messageState = messageRepository.sendTextMessage(phone, content);
        return messageState;
    }

//    @Override
//    public int addMessage(String sendId, String toUserId, int msgType, String content) {
//        return messageMapper.addMessage(sendId,toUserId,msgType,content);
//    }

    @Override
    public boolean verifyIdentifyCode(String phone, String code, Integer codeType) {
        return messageRepository.verifyIdentifyCode(phone,code,codeType);
    }

//    @Override
//    public int updateMessageById(String msgId, String userId) {
//        return messageMapper.updateMessageById(msgId,userId);
//    }
//
//    @Override
//    public List<Message> findMessageByUser(long userId, int messageType) {
//        return messageMapper.findMessageByUser(userId,messageType);
//    }

    @Override
    public void sendIOSPush(List<String> tokenList, String message) {
        messageRepository.sendIOSPush(tokenList,message);
    }


}
