package com.tmt.wechat.service;

import com.tmt.wechat.bean.base.WechatConfig;

/**
 * 集合了所有的操作, 后台使用这个接口
 * @author lifeng
 */
public interface WechatOptionService extends WechatService, WechatMenuService, 
       WechatQrCodeService, WechatMaterialService, WechatTemplateMessageService, WechatUserService {
	
	/**
	 * 执行栈中绑定此配置
	 * @param config
	 * @return
	 */
	WechatOptionService bind(WechatConfig config);
}