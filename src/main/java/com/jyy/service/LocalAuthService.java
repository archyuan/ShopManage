package com.jyy.service;

import com.jyy.dto.LocalAuthExecution;
import com.jyy.entity.LocalAuth;

public interface LocalAuthService {

    LocalAuth getLocalAuthByUsernameAndPwd(
            String userName,
            String password
    );

    LocalAuth getLocalAuthByUserId(long userId);


    LocalAuthExecution bindLocalAuth(LocalAuth localAuth)throws RuntimeException;

    LocalAuthExecution modifyLocalAuth(
            Long userId,
            String username,
            String password,
            String newPassword
    );

}
