package com.tmt.wechat.handler.impl;

import com.tmt.wechat.bean.base.WechatConfig;
import com.tmt.wechat.bean.base.WechatConstants;
import com.tmt.wechat.bean.message.MsgHead;
import com.tmt.wechat.bean.message.RespMsg;
import com.tmt.wechat.bean.message.RespMsgText;
import com.tmt.wechat.handler.Handler;

/**
 * 文本处理器
 * @author root
 */
public class TextHandler implements Handler{

	Handler handler;
	
	@Override
	public RespMsg doHandler(MsgHead request, WechatConfig app, Byte type, String config) {
		// 只能处理为 1 的类型
		if (type == WechatConstants.HANDLER_text) {
			return new RespMsgText(request, config);
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