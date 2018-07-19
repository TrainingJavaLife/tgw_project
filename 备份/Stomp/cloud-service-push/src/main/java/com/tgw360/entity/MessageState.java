package com.tgw360.entity;

/**
 * Webservice接受验证码实体类
 * Created by 危宇 on 2018/1/9 17:19
 */
public class MessageState {
    private Integer code;
    private String content;

    public MessageState() {
    }

    public MessageState(Integer code, String content) {
        this.code = code;
        this.content = content;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
