package com.jyy.service.Impl;

import com.jyy.dao.LocalAuthDao;
import com.jyy.dto.LocalAuthExecution;
import com.jyy.entity.LocalAuth;
import com.jyy.enums.LocalAuthStateEnum;
import com.jyy.service.LocalAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LocalAuthServiceImpl implements LocalAuthService {

    @Autowired
    private LocalAuthDao localAuthDao;


    @Override
    public LocalAuth getLocalAuthByUsernameAndPwd(String userName, String password) {
        return localAuthDao.queryLocalByUserNameAndPwd(userName,password);
    }

    @Override
    public LocalAuth getLocalAuthByUserId(long userId) {
        return localAuthDao.queryLocalByUserId(userId);
    }

    @Override
    public LocalAuthExecution bindLocalAuth(LocalAuth localAuth) throws RuntimeException {
       if(localAuth == null || localAuth.getPassword() == null
       || localAuth.getUserName() == null||localAuth.getPersonInfo()==null
       ||localAuth.getPersonInfo().getUserId()==null){
           return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
       }
       LocalAuth tempAuth = localAuthDao.queryLocalByUserId(localAuth
       .getPersonInfo().getUserId());
       if(tempAuth != null){
           return new LocalAuthExecution(LocalAuthStateEnum.ONLY_ONE_ACCOUNT);
       }
       try{
           localAuth.setCreateTime(new Date());
           localAuth.setLastEditTime(new Date());
           localAuth.setPassword(localAuth.getPassword());
           int eff = localAuthDao.insertLocalAuth(localAuth);
           if(eff <=0){
               throw new RuntimeException("账号绑定失败");
           }else{
               return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS,localAuth);
           }

       }catch (Exception e){
           throw new RuntimeException("insertLocalAuth error:"+e.getMessage());
       }
    }

    @Override
    public LocalAuthExecution modifyLocalAuth(Long userId, String username, String password, String newPassword) {
        if(userId != null&&username!=null&&password!=null&&
        password != null&&!password.equals(newPassword)){
            try{
                int eff = localAuthDao.updateLocalAuth(
                        userId,
                        username,
                        password,
                        newPassword,
                        new Date()
                );
                if(eff <=0){
                    throw new RuntimeException("更新密码失败");
                }
                return new LocalAuthExecution(LocalAuthStateEnum.SUCCESS);
            }catch (Exception e){
                throw new RuntimeException("更新密码失败"+e.getMessage());
            }
        }else{
            return new LocalAuthExecution(LocalAuthStateEnum.NULL_AUTH_INFO);
        }
    }
}
