package com.jyy.daoTest;

import com.jyy.dao.LocalAuthDao;
import com.jyy.entity.LocalAuth;
import com.jyy.entity.PersonInfo;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class LocalAuthDaoTest extends BaseTest {
    @Autowired
    private LocalAuthDao localAuthDao;

    private String username = "testusername";
    private String password = "testpassword";

    @Test
    @Ignore
    public void testInsert(){
        LocalAuth localAuth = new LocalAuth();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setUserId(1L);
        localAuth.setUserId(1L);
        localAuth.setPersonInfo(personInfo);
        localAuth.setUserName(username);
        localAuth.setPassword(password);
        localAuth.setCreateTime(new Date());
        int effectedNum = localAuthDao.insertLocalAuth(localAuth);
        assertEquals(1,effectedNum);
    }

    @Test
    @Ignore
    public void testBQueryByUserNameAndPasswor(){
        LocalAuth localAuth = localAuthDao.queryLocalByUserNameAndPwd(
                username,
                password
        );
        assertEquals("贾园园",localAuth.getPersonInfo().getName());
    }
    @Test
    @Ignore
    public void  testQueryLocalByUserId(){
        LocalAuth localAuth = localAuthDao.queryLocalByUserId(1L);
        assertEquals("贾园园",localAuth.getPersonInfo().getName());
    }

    @Test
    public void testUpdate(){
        Date now = new Date();
        int eff = localAuthDao.updateLocalAuth(
                1l,
                username,
                password,
                "ssss",
                now
        );
        assertEquals(1,eff);
    }
}
