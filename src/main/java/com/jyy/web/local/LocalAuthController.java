package com.jyy.web.local;

import java.util.HashMap;
import java.util.Map;

import com.jyy.dto.LocalAuthExecution;
import com.jyy.entity.LocalAuth;
import com.jyy.entity.PersonInfo;
import com.jyy.enums.LocalAuthStateEnum;
import com.jyy.service.LocalAuthService;
import com.jyy.util.CodeUtil;
import com.jyy.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "local", method = {RequestMethod.GET, RequestMethod.POST})
public class LocalAuthController {
    @Autowired
    private LocalAuthService localAuthService;

    @PostMapping("/bindlocalauth")
    @ResponseBody
    private Map<String, Object> bindLocalAuth(HttpServletRequest request) {
        Map<String, Object> modeMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modeMap.put("success", false);
            modeMap.put("errMsg", "输入了错误的验证码");
            return modeMap;
        }
        String userName = HttpServletRequestUtil.getString(
                request,
                "userName"
        );

        String password = HttpServletRequestUtil.getString(
                request,
                "password"
        );
        PersonInfo user = (PersonInfo) request.getSession()
                .getAttribute("user");
        if (userName != null && password != null
                && user != null && user.getUserId() != null) {
            LocalAuth localAuth = new LocalAuth();
            localAuth.setUserName(userName);
            localAuth.setPassword(password);
            localAuth.setPersonInfo(user);
            localAuth.setUserId(user.getUserId());
            LocalAuthExecution localAuthExecution =
                    localAuthService.bindLocalAuth(localAuth);
            if (localAuthExecution.getState() ==
                    LocalAuthStateEnum.SUCCESS.getState()) {
                modeMap.put("success", true);
            } else {
                modeMap.put("success", false);
                modeMap.put("errMsg", localAuthExecution.getStateInfo());
            }
        } else {
            modeMap.put("success", false);
            modeMap.put("errMsg", "用户名和密码不能为空");
        }
        return modeMap;
    }

    @PostMapping("/changelocalpwd")
    @ResponseBody
    private Map<String, Object> changeLocalPwd(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        if (!CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }
        String userName = HttpServletRequestUtil
                .getString(request, "userName");
        String password = HttpServletRequestUtil
                .getString(request, "password");
        String newPassword = HttpServletRequestUtil
                .getString(request, "newPassword");
        PersonInfo user = (PersonInfo) request
                .getSession().getAttribute("user");
        if (userName != null && password != null &&
                newPassword != null && user != null
                && user.getUserId() != null && !password.equals(newPassword)) {
            try {
                LocalAuth localAuth = localAuthService.getLocalAuthByUserId(
                        user.getUserId()
                );
                if (localAuth == null || !localAuth.getUserName().equals(userName)) {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "输入的账号非本次登录的账号");
                    return modelMap;
                }

                LocalAuthExecution le = localAuthService
                        .modifyLocalAuth(
                                user.getUserId(),
                                userName,
                                password,
                                newPassword
                        );
                if (le.getState() == LocalAuthStateEnum.
                        SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", le.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入密码");
        }
        return modelMap;
    }

    @PostMapping("/logincheck")
    @ResponseBody
    private Map<String, Object> loginCheck(HttpServletRequest request) {
        Map<String, Object> modelMap = new HashMap<>();
        boolean needVerify = HttpServletRequestUtil
                .getBoolean(request, "needVerify");
        if (needVerify && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "输入了错误的验证码");
            return modelMap;
        }

        String userName = HttpServletRequestUtil
                .getString(request, "userName");
        String password = HttpServletRequestUtil
                .getString(request, "password");

        if (userName != null && password != null) {
            LocalAuth localAuth = localAuthService
                    .getLocalAuthByUsernameAndPwd(
                            userName,
                            password
                    );
            if (localAuth != null) {
                modelMap.put("success", true);
                request.getSession().setAttribute(
                        "user",
                        localAuth.getPersonInfo()
                );

            } else {
                modelMap.put("success", false);
                modelMap.put("errMsg", "用户名或密码错误");
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "用户名和密码均不能为空");
        }
        return modelMap;
    }

    @PostMapping("/logout")
    @ResponseBody
    private Map<String,Object> logout(HttpServletRequest request){
        Map<String,Object> modeMap = new HashMap<>();
        request.getSession().setAttribute("user",null);
        modeMap.put("success",true);
        return modeMap;
    }
}
