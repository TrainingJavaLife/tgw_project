package com.tgw360.service.impl;

import com.tgw360.entity.UserToken;
import com.tgw360.repository.UserTokenRepository;
import com.tgw360.service.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 危宇 on 2018/1/29 13:45
 */
@Service
public class UserTokenServiceImpl implements UserTokenService {

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Override
    public void save(UserToken userToken) {
        userTokenRepository.save(userToken);
    }

    @Override
    public UserToken findTokenById(long userId) {
        return userTokenRepository.findTokenById(userId);
    }

    @Override
    public void removeToken(long userId) {
        userTokenRepository.removeToken(userId);
    }

    @Override
    public void updateToken(long userId, String key, String value) {
        userTokenRepository.updateToken(userId,key,value);
    }

    @Override
    public List<UserToken> findAllToken() {
        return userTokenRepository.findAllToken();
    }

    @Override
    public List<UserToken> findTokenByIds(List userIdList) {
        return userTokenRepository.findTokenByIds(userIdList);
    }
}
