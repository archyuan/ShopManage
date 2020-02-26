package com.jyy.daoTest;

import
        com.jyy.dao.AreaDao;
import com.jyy.entity.Area;
import java.util.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AreaDaoTest extends BaseTest {

    @Autowired
   private AreaDao areaDao;

   @Test
   public void testQueryArea(){
       List<Area> areaList = areaDao.queryArea();
       assertEquals(2,areaList.size());
       String a = "aa";
       Map<Character,Integer> result = new HashMap<>();

   }
}
