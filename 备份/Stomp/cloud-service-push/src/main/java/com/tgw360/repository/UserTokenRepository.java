package com.tgw360.repository;

/**
 * Created by 危宇 on 2018/2/2 16:08
 */

import com.tgw360.entity.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserTokenRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void save(UserToken userToken) {
        mongoTemplate.save(userToken);
    }


    public UserToken findTokenById(long userId) {
        Query query=new Query(Criteria.where("user_id").is(userId));
        UserToken userToken = mongoTemplate.findOne(query,UserToken.class);
        return userToken;
    }


    public void removeToken(long userId) {
        Query query=new Query(Criteria.where("user_id").is(userId));
        mongoTemplate.remove(query,UserToken.class);

    }


    public void updateToken(long userId, String key, String value) {
        mongoTemplate.updateMulti(new Query(new Criteria("user_id").in(userId)),
                new Update().set(key, value), UserToken.class);
    }


    public List<UserToken> findAllToken() {
        List<UserToken> userTokenList = mongoTemplate.findAll(UserToken.class);
        return userTokenList;
    }


    public List<UserToken> findTokenByIds(List userIdList) {
        Query query = Query.query(Criteria.where("user_id").in(userIdList));
        List<UserToken> userTokenList = mongoTemplate.find(query,UserToken.class);
        return userTokenList;
    }
}
