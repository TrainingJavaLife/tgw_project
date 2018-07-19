package com.tgw360.controller.dto;

import java.util.List;

/**
 * Created by 危宇 on 2018/1/16 16:27
 */
public class MessageDTO {
    private Long SendId; // 发送者id: 发送人的Id
    private List ToUserId; // 接受者id: 接收人的Id
    private Integer MessageType;  // 消息类型:1.状态栏 2.消息盒子 3.短信 4.邮件
    private String MessageContent; // 消息内容

    public Long getSendId() {
        return SendId;
    }

    public void setSendId(Long sendId) {
        SendId = sendId;
    }

    public List getToUserId() {
        return ToUserId;
    }

    public void setToUserId(List toUserId) {
        ToUserId = toUserId;
    }

    public Integer getMessageType() {
        return MessageType;
    }

    public void setMessageType(Integer messageType) {
        MessageType = messageType;
    }

    public String getMessageContent() {
        return MessageContent;
    }

    public void setMessageContent(String messageContent) {
        MessageContent = messageContent;
    }
}
