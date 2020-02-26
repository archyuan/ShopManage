package com.jyy.daoTest;

import com.jyy.dao.PersonInfoDao;
import com.jyy.entity.PersonInfo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class PersonInfoDaoTest extends BaseTest {

        @Autowired
        private PersonInfoDao personInfoDao;

        @Test
        public void testInsertPersonInfo(){
            PersonInfo personInfo = new PersonInfo();
            personInfo.setName("ss");
            personInfo.setGender("女");
            personInfo.setEnableStatus(1);
            personInfo.setCreateTime(new Date());
            personInfo.setLastEditTime(new Date());
            personInfoDao.insertPersonInfo(personInfo);
        }
}
