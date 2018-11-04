package com.tmt.wechat.handler.impl;

import com.tmt.wechat.bean.base.WechatConfig;
import com.tmt.wechat.bean.base.WechatConstants;
import com.tmt.wechat.bean.message.MsgHead;
import com.tmt.wechat.bean.message.RespMsg;
import com.tmt.wechat.handler.Handler;

/**
 * 不处理
 * @author root
 */
public class NoneHandler implements Handler {
	
	Handler handler;

	@Override
	public RespMsg doHandler(MsgHead request, WechatConfig app, Byte type, String config) {
		if (type == null || type == WechatConstants.HANDLER_none
				|| type == WechatConstants.HANDLER_view || type == WechatConstants.HANDLER_site_view) {
			return null;
		}
		if (handler != null) {
			return handler.doHandler(request, app, type, config);
		}
		return null;
	}

	@Override
	public void setNextHandler(Handler handler) {
		this.handler = handler;
	}
}
