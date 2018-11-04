package com.tmt.wechat.handler.impl;

import java.util.Map;

import com.tmt.common.utils.JsonMapper;
import com.tmt.wechat.bean.base.WechatConfig;
import com.tmt.wechat.bean.base.WechatConstants;
import com.tmt.wechat.bean.message.MsgHead;
import com.tmt.wechat.bean.message.RespMsg;
import com.tmt.wechat.bean.message.RespMsgImage;
import com.tmt.wechat.handler.Handler;

/**
 * 图片处理器
 * @author root
 */
public class ImageHandler implements Handler{

	Handler handler;

	/**
	 * 回复图片消息
	 * 图片等消息，只能先上传到服务器上
	 * config 中格式为{media_Id: '', media_url:''}
	 */
	@Override
	@SuppressWarnings("unchecked")
	public RespMsg doHandler(MsgHead request, WechatConfig app, Byte type, String config) {
		
		// 只能处理为 4 的类型
		if (type == WechatConstants.HANDLER_pic) {
			Map<String, String> _config = JsonMapper.fromJson(config, Map.class);
			if (_config != null) {
				String media_Id = _config.get("media_id");
				return new RespMsgImage(request, media_Id);
			}
			
			// 如果配置错误则不处理
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