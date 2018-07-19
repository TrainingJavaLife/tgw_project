package com.tgw360.service;

import com.tgw360.entity.UserToken;

import java.util.List;

/**
 * Created by 危宇 on 2018/1/29 13:43
 */
public interface UserTokenService {
    /**
     * 创建userToken
     * @param userToken
     */
    void save(UserToken userToken);

    /**
     * 根据userId查找UserToken
     * @param userId
     * @return
     */
    UserToken findTokenById(long userId);

    /**
     * 移除userToken
     * @param userId
     */
    void removeToken(long userId);


    /**
     * 修改用户token
     * @param userId
     * @param key
     * @param value
     */
    void updateToken(long userId, String key, String value);

    /**
     * 查找所有的token
     * @return
     */
    List<UserToken> findAllToken();

    /**
     * 根据传入的userId包含的list查找用户token
     * @param userIdList
     * @return
     */
    List<UserToken> findTokenByIds(List userIdList);
}
