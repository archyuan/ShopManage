package com.jyy.service;

import com.jyy.dto.WechatAuthExecution;
import com.jyy.entity.WechatAuth;
import org.springframework.web.multipart.commons.CommonsMultipartFile;



public interface WechatAuthService {

	/**
	 * 
	 * @param openId
	 * @return
	 */
	WechatAuth getWechatAuthByOpenId(String openId);

	/**
	 * 
	 * @param wechatAuth
	 * @param
	 * @return
	 * @throws RuntimeException
	 */
	WechatAuthExecution register(WechatAuth wechatAuth) throws RuntimeException;

}
