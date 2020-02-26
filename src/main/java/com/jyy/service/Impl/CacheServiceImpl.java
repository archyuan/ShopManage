package com.jyy.service.Impl;

import com.jyy.cache.JedisUtil;
import com.jyy.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public class CacheServiceImpl implements CacheService {

    @Autowired
    private JedisUtil.Keys jedisKeys;

    @Override
    public void removeFromCache(String keyPrefix) {
        Set<String> keySet = jedisKeys
                .keys(keyPrefix+"*");
        for(String key:keySet){
            jedisKeys.del(key);
        }
    }
}
