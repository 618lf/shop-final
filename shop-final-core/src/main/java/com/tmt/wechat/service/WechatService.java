package com.tmt.wechat.service;

import java.util.Map;

import com.tmt.wechat.bean.base.SnsToken;
import com.tmt.wechat.bean.base.WechatConfig;
import com.tmt.wechat.bean.message.MsgHead;
import com.tmt.wechat.bean.user.UserInfo;

/**
 * 管理整个 wechat 交互的过程
 * 
 * @author lifeng
 */
public interface WechatService {

	/**
	 * 执行栈中绑定此配置
	 * @param config
	 * @return
	 */
	WechatService bind(WechatConfig config);
	
	/**
	 * 执行栈中绑定此配置
	 * @param config
	 * @return
	 */
	void unbind();
	
	/**
	 * 校验参数
	 * @param timestamp
	 * @param nonce
	 * @param signature
	 * @return
	 */
	boolean checkSignature(String timestamp, String nonce, String signature);
	
	/**
	 * 消息事件
	 * @param req
	 * @return
	 */
	MsgHead onMessage(String req);
	
	
	/**
	 * 得到授权地址
	 * @param domain
	 * @param url
	 * @return
	 */
	String getAuthorizeURL(String domain, String url);
	
	/**
	 * 授权登录回调
	 * @param code
	 * @param state
	 * @return
	 */
	SnsToken oauth2AccessToken(String code, String state);
	
	/**
	 * 授权登录后获取用户信息
	 * @param accessToken
	 * @param openId
	 * @return
	 */
	UserInfo authUserinfo(String accessToken, String openId);
	
	/**
	 * 地址做签名
	 * @param url
	 * @return
	 */
	Map<String, String> getUrlSign(String url);
}
