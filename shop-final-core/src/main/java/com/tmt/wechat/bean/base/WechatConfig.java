package com.tmt.wechat.bean.base;


/**
 * 微信相关的配置
 * @author lifeng
 */
public interface WechatConfig {

	/**
	 * app_id
	 * @return
	 */
	String getAppId();

	/**
	 * app_Secret
	 * @return
	 */
	String getSecret();

	/**
	 * 接入token
	 * @return
	 */
	String getToken();

	/**
	 * 消息加密
	 * @return
	 */
	String getAesKey();
	
	/**
	 * 原始ID
	 * @return
	 */
	String getSrcId();
	
	/**
	 * 配置信息
	 * @return
	 */
	Object getSetting();
}