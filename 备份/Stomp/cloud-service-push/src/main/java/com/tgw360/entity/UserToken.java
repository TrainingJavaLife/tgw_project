package com.tgw360.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * IOS的设备token
 * Created by 危宇 on 2018/1/29 11:10
 */
@Document(collection = "ios_push_token")
public class UserToken {
    @Id
    private String id;
    @Field("user_id")
    private Long userId;
    @Field("token")
    private String token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
