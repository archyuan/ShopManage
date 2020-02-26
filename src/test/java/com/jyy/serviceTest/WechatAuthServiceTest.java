package com.jyy.serviceTest;

import com.jyy.daoTest.BaseTest;
import com.jyy.dto.WechatAuthExecution;
import com.jyy.entity.PersonInfo;
import com.jyy.entity.WechatAuth;
import com.jyy.service.WechatAuthService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class WechatAuthServiceTest extends BaseTest {

    @Autowired
    private WechatAuthService wechatAuthService;

    @Test
    public void testRegister(){
        WechatAuth wechatAuth = new WechatAuth();
        PersonInfo personInfo = new PersonInfo();
        personInfo.setCreateTime(new Date());
        personInfo.setName("测试一下");
        personInfo.setUserType(1);
        wechatAuth.setPersonInfo(personInfo);
        wechatAuth.setCreateTime(new Date());
        wechatAuth.setOpenId("dd");
        WechatAuthExecution wae = wechatAuthService.register(wechatAuth);
        WechatAuth wechatAuth1 = wechatAuthService.getWechatAuthByOpenId("dd");
        System.out.println(wechatAuth1.getPersonInfo().getName());
    }

}
