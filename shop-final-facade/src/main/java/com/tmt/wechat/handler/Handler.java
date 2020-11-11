package com.tmt.wechat.handler;

import com.tmt.wechat.bean.base.WechatConfig;
import com.tmt.wechat.bean.message.MsgHead;
import com.tmt.wechat.bean.message.RespMsg;


/**
 * 事件处理器
 * @author root
 */
public interface Handler {

	/**
	 * 事件处理器
	 * @param type
	 * @param config
	 * @return
	 */
	public RespMsg doHandler(MsgHead request, WechatConfig app, Byte type, String config);
	
	/**
	 * 下一个处理器
	 * @param handler
	 */
	public void setNextHandler(Handler handler);
}
