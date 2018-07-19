package com.tgw360.entity;

import java.util.Date;
import java.util.List;

/**
 * 消息实体类
 * Created by 危宇 on 2018/1/8 16:16
 */
public class Message {
    private Long id;  // id
    private Long sendId; // 发送者id: 发送人的Id
    private Long toUserId; // 接受者id: 接收人的Id
    private Integer type;  // 消息类型:1.状态栏 2.消息盒子 3.短信 4.邮件
    private String content; // 消息内容
    private Date date; // 时间
    private Integer isRead; // 是否已读

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSendId() {
        return sendId;
    }

    public void setSendId(Long sendId) {
        this.sendId = sendId;
    }

    public Long getToUserId() {
        return toUserId;
    }

    public void setToUserId(Long toUserId) {
        this.toUserId = toUserId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }
}
