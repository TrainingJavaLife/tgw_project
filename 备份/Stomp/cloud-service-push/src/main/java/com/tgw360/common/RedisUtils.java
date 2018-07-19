package com.tgw360.common;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * Created by 危宇 on 2018/1/17 15:09
 */
public class RedisUtils {

    public  static  String getValue(RedisTemplate<String,String> redisTemplate, String key) {
        String value = new String();
        try {
            ValueOperations<String, String> opsForString = redisTemplate.opsForValue();
            //从redis中查询数据
            value = opsForString.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }
}
