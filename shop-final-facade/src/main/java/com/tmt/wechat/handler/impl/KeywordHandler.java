package com.tmt.wechat.handler.impl;

import com.tmt.common.utils.SpringContextHolder;
import com.tmt.wechat.bean.base.WechatConfig;
import com.tmt.wechat.bean.base.WechatConstants;
import com.tmt.wechat.bean.message.MsgHead;
import com.tmt.wechat.bean.message.RespMsg;
import com.tmt.wechat.entity.MetaKeyword;
import com.tmt.wechat.handler.Handler;
import com.tmt.wechat.service.MetaKeywordServiceFacade;

/**
 * 关键词匹配
 * @author root
 */
public class KeywordHandler implements Handler {
	
	Handler handler;	
	MetaKeywordServiceFacade keywordService; // 关键词搜索服务
	
	public KeywordHandler() {
		keywordService = SpringContextHolder.getBean(MetaKeywordServiceFacade.class);
	}
	
	/**
	 * 关键词是终结类型 
	 *     -- 必须放在处理流的最后
	 * @param request
	 * @param app
	 * @param type
	 * @param config
	 * @return
	 */
	@Override
	public RespMsg doHandler(MsgHead request, WechatConfig app, Byte type, String config) {
		if (type == null || type == WechatConstants.HANDLER_none) {
			return null;
		}
		// 只能处理为 7 的类型
		if (type == WechatConstants.HANDLER_keyword) {
			
			// 只需要匹配一个即可
			MetaKeyword metaKeyword = keywordService.searchOne(config, app.getAppId());
			
			// 是否是关键词支持的类型
			if (metaKeyword == null || metaKeyword.getType() == null 
					|| !this.supporTypes(metaKeyword.getType().byteValue())) {
				return null;
			}
			
			// 校验是否能处理
			Byte ctype = metaKeyword.getType().byteValue();
			if (ctype == null || ctype == WechatConstants.HANDLER_keyword) {
				return null;
			}
			
			// 具体的配置
			String cconfig = metaKeyword.getConfig();
			
			// 是一个中转类型
			if (handler != null) {
				return handler.doHandler(request, app, ctype, cconfig);
			}
		}
		return null;
	}
	
	private Boolean supporTypes(Byte type) {
		if (type == WechatConstants.HANDLER_text || type == WechatConstants.HANDLER_rich
				|| type == WechatConstants.HANDLER_pic) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	@Override
	public void setNextHandler(Handler handler) {
		this.handler = handler;
	}
}