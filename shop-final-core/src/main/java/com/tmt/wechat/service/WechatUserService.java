package com.tmt.wechat.service;

import com.tmt.wechat.bean.user.FollowResult;
import com.tmt.wechat.bean.user.UserInfo;

/**
 * 用户管理
 * @author lifeng
 */
public interface WechatUserService {

	/**
	 * 用户信息
	 * @param openId
	 * @return
	 */
	UserInfo userinfo(String openId);
	
	/**
	 * 用户列表
	 * @return
	 */
	FollowResult userPage(String next_openid);
}