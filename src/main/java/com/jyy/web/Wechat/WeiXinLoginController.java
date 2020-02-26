package com.jyy.web.Wechat;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jyy.dto.WechatAuthExecution;
import com.jyy.entity.PersonInfo;
import com.jyy.entity.WechatAuth;
import com.jyy.enums.WechatAuthStateEnum;
import com.jyy.service.PersonInfoService;
import com.jyy.service.WechatAuthService;
import com.jyy.util.UserAccessToken;
import com.jyy.util.WeiXinUser;
import com.jyy.util.WeiXinUserUtil;
import com.jyy.util.WeixinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping("wechatlogin")
/**
 * 从微信菜单点击后调用的接口，可以在url里增加参数（role_type）来表明是从商家还是从玩家按钮进来的，依次区分登陆后跳转不同的页面
 * 玩家会跳转到index.html页面
 * 商家如果没有注册，会跳转到注册页面，否则跳转到任务管理页面
 * 如果是商家的授权用户登陆，会跳到授权店铺的任务管理页面
 *
 *https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxc51e7e6d385b0663&redirect_uri=http://39.106.98.67/background_war/wechatlogin/logincheck&response_type=code&role_type=1&scope=snsapi_userinfo&state=1#wechat_redirect
 */
public class WeiXinLoginController {

	private static Logger log = LoggerFactory
			.getLogger(WeiXinLoginController.class);
	private static final String FRONTEND = "1";
	private static final String SHOPEND = "2";

	@Autowired
	private PersonInfoService personInfoService;

	@Autowired
	private WechatAuthService wechatAuthService;

	@RequestMapping(value = "/logincheck", method = { RequestMethod.GET })
	public String doGet(HttpServletRequest request, HttpServletResponse response) {
		log.debug("weixin login get...");
		String code = request.getParameter("code");
		String roleType = request.getParameter("state");
		log.debug("weixin login code:" + code);

		WeiXinUser user = null;
		String openId = null;
		WechatAuth wechatAuth = null;
		if (null != code) {
			UserAccessToken token;
			try {
				token = WeiXinUserUtil.getUserAccessToken(code);
				log.debug("weixin login token:" + token.toString());
				String accessToken = token.getAccessToken();
				openId = token.getOpenId();
				user = WeiXinUserUtil.getUserInfo(accessToken, openId);
				log.debug("weixin login user:" + user.toString());
				request.getSession().setAttribute("openId", openId);
				wechatAuth = wechatAuthService
						.getWechatAuthByOpenId(openId);
			} catch (IOException e) {
				log.error("error in getUserAccessToken or getUserInfo or findByOpenId: "
						+ e.toString());
				e.printStackTrace();
			}
		}
		log.debug("weixin login success.");
		log.debug("login role_type:" + roleType);
		if(wechatAuth == null){
			PersonInfo personInfo = WeixinUtil.getPersonInfoFromRequest(user);
			wechatAuth = new WechatAuth();
			wechatAuth.setOpenId(openId);
			if(FRONTEND.equals(roleType)){
				personInfo.setUserType(1);
			}else {
				personInfo.setUserType(2);
			}
			wechatAuth.setPersonInfo(personInfo);
			WechatAuthExecution wechatAuthExecution = wechatAuthService.register(wechatAuth);
			if(wechatAuthExecution.getState() != WechatAuthStateEnum.SUCCESS.getState()){
				return null;
			}else{
				personInfo = personInfoService.getPersonInfoById(wechatAuth.getUserId());
				request.getSession().setAttribute("user",personInfo);
			}
		}
		if(FRONTEND.equals(roleType)){
			return "frontend/index";
		}else {
           return "shopadmin/shoplist";
		}

	}
}
