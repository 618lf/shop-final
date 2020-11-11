package com.tmt.wechat.handler.impl;

import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.JsonMapper;
import com.tmt.core.utils.SpringContextHolder;
import com.tmt.system.entity.UpdateData;
import com.tmt.update.UpdateServiceFacade;
import com.tmt.wechat.bean.base.WechatConfig;
import com.tmt.wechat.bean.message.MsgHead;
import com.tmt.wechat.bean.message.RespMsg;
import com.tmt.wechat.handler.Handler;
import com.tmt.wechat.update.WechatModule;

/**
 * 消息通知
 * @author lifeng
 *
 */
public class NoticeHandler implements Handler{

	Handler handler;
	UpdateServiceFacade updateService;
	
	public NoticeHandler() {
		updateService = SpringContextHolder.getBean(UpdateServiceFacade.class);
	}
	
	@Override
	public RespMsg doHandler(MsgHead request, WechatConfig app, Byte type, String config) {
		
		// 微信操作
		try {
			// 存储的消息
			MsgHead _req = new MsgHead();
			_req.setFromUserName(request.getFromUserName());
			_req.setToUserName(request.getToUserName());
			
			// 发送消息对象
			UpdateData updateData = new UpdateData();
			updateData.setId((Long)IdGen.key());
			updateData.setMsg(JsonMapper.toJson(_req));
			updateData.setModule(WechatModule.USER_OPS);
			updateData.setOpt((byte)0);
			updateService.save(updateData);
		}catch (Exception e) {}
		
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
