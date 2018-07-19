package com.tgw360.entity.websocket;

/**
 * 验证消息已经被读取
 * Created by 危宇 on 2018/1/18 14:24
 */
public class VerifyMessage {
    private String msgId;
    private String userId;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
