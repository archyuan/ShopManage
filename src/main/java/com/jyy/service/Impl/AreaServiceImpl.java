package com.jyy.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jyy.cache.JedisUtil;
import com.jyy.dao.AreaDao;
import com.jyy.entity.Area;
import com.jyy.service.AreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class AreaServiceImpl implements AreaService {

    @Autowired
    private AreaDao areaDao;

    @Autowired
    private JedisUtil.Keys jedisKeys;

    @Autowired
    private JedisUtil.Strings jedisStrings;


    private static Logger logger =  LoggerFactory.getLogger(AreaServiceImpl.class);
    @Override
    @Transactional
    public List<Area> getAreaList() {
        String key = AREALISTKEY;
        List<Area> areaList = null;
        ObjectMapper mapper = new ObjectMapper();
        if(!jedisKeys.exists(key)){
            areaList = areaDao.queryArea();
            String jsonString = null;
            try {
                jsonString = mapper.writeValueAsString(areaList);
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }
            jedisStrings.set(key,jsonString);
        }else {
            String jsonString = jedisStrings.get(AREALISTKEY);
            JavaType javaType = mapper.getTypeFactory()
                    .constructParametricType(ArrayList.class,Area.class);
            try {
                areaList = mapper.readValue(jsonString,javaType);
            } catch (IOException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e.getMessage());
            }

        }
        return  areaList;
    }
}
