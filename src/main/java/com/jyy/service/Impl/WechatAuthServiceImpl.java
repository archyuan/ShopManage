package com.jyy.service.Impl;

import com.jyy.dao.PersonInfoDao;
import com.jyy.dao.WechatAuthDao;
import com.jyy.dto.WechatAuthExecution;
import com.jyy.entity.PersonInfo;
import com.jyy.entity.WechatAuth;
import com.jyy.enums.WechatAuthStateEnum;
import com.jyy.service.WechatAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class WechatAuthServiceImpl implements WechatAuthService {

    private static Logger log = LoggerFactory
            .getLogger(WechatAuthServiceImpl.class);

    @Autowired
    private WechatAuthDao wechatAuthDao;

    @Autowired
    private PersonInfoDao personInfoDao;

    @Override
    public WechatAuth getWechatAuthByOpenId(String openId) {
        return wechatAuthDao.queryWechatInfoByOpenId(openId);
    }

    @Override
    @Transactional
    public WechatAuthExecution register(WechatAuth wechatAuth) throws RuntimeException {
       if(wechatAuth == null || wechatAuth.getOpenId() ==null){
           return new WechatAuthExecution(
                   WechatAuthStateEnum.NULL_AUTH_INFO
           );

       }

       try{
            wechatAuth.setCreateTime(new Date());
            if(wechatAuth.getPersonInfo() != null && wechatAuth.getPersonInfo().getUserId()==null){
                try{
                    wechatAuth.getPersonInfo().setCreateTime(new Date());
                    wechatAuth.getPersonInfo().setEnableStatus(1);
                    PersonInfo personInfo = wechatAuth.getPersonInfo();
                    int effectedNum = personInfoDao.insertPersonInfo(personInfo);
                    wechatAuth.setPersonInfo(personInfo);
                    wechatAuth.setUserId(wechatAuth.getPersonInfo().getUserId());
                    if(effectedNum <=0){
                        throw new RuntimeException("添加用户信息失败");
                    }
                }catch (Exception e){
                    log.error("insertPersonInfo error"+e.toString());
                    throw new RuntimeException("InsertPersonInfo error"+e.getMessage());
                }
            }
            int effectedNum = wechatAuthDao.insertWechatAuth(wechatAuth);
            if(effectedNum <= 0){
                throw new RuntimeException("账号创建失败");
            }else {
                return new WechatAuthExecution(WechatAuthStateEnum.SUCCESS,wechatAuth);
            }
       }catch (Exception e){
           log.error("insertPersonInfo"+e.toString());
           throw new RuntimeException("insertPersoinInfo error"+
                   e.getMessage());
       }
    }
}
