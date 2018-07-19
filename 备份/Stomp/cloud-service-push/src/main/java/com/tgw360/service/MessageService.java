package com.tgw360.service;

import com.tgw360.entity.Message;
import com.tgw360.entity.MessageState;
import com.tgw360.exception.StompDataException;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by 危宇 on 2018/1/10 17:24
 */
public interface MessageService {

    /**
     * 获取验证码
     * @param phone
     * @param type
     * @return
     * @throws RemoteException
     * @throws ServiceException
     * @throws MalformedURLException
     * @throws StompDataException
     */
    MessageState acquireIdentifyCode(String phone, Integer type);

    /**
     * 发送短信到特定手机
     * @param phone
     * @param content
     * @return
     * @throws RemoteException
     * @throws ServiceException
     * @throws MalformedURLException
     */
    MessageState sendTextMessage(String phone,String content);

//    /**
//     * 添加发送记录到数据库
//     * @param sendId
//     * @param toUserId
//     * @param msgType
//     * @param content
//     * @return
//     */
//    int addMessage(String sendId,String toUserId, int msgType,String content);

    /**
     * 验证验证码
     * @param phone
     * @param code
     * @param codeType
     * @return
     */
    boolean verifyIdentifyCode(String phone,String code,Integer codeType);

//    /**
//     * 修改验证码状态是否已读
//     * @param msgId
//     * @param userId
//     * @return
//     */
//    int updateMessageById(String msgId,String userId);
//
//    /**
//     * 根据用户Id和消息类型查询用户相关的消息
//     * @param userId
//     * @param messageType
//     * @return
//     */
//    List<Message> findMessageByUser(long userId,int messageType);

    /**
     * IOS推送消息
     * @param tokenList
     * @param message
     */
    void sendIOSPush(List<String> tokenList,String message);
}
