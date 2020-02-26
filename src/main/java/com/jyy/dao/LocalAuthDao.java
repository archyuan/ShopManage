package com.jyy.dao;

import com.jyy.entity.LocalAuth;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface LocalAuthDao {
    LocalAuth queryLocalByUserNameAndPwd(@Param("userName")String userName,
                             @Param("password")String password);

    LocalAuth queryLocalByUserId(@Param("userId")long userId);

    int insertLocalAuth(LocalAuth localAuth);

    int updateLocalAuth(@Param("userId")Long userId,
                        @Param("userName")String username,
                        @Param("password")String password,
                        @Param("newPassword")String newPassword,
                        @Param("lastEditTime")Date lastEditTime);
}
