package com.tmt.shop.event.impl;

import java.util.Map;

import com.tmt.common.utils.Maps;
import com.tmt.shop.entity.NoticeSetting;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderEvent;

/**
 * 通知事件消息处理
 * @author root
 */
public class NoticeEventHandler extends DefaultEventHandler{

	private String title;
	private String type;
	private String content;
	
	/**
	 * 构建消息
	 * @param title
	 * @param type
	 * @param content
	 */
	public NoticeEventHandler(String title, String type, String content){
		this.title = title;
		this.type = type;
		this.content = content;
	}
	
	@Override
	protected Boolean doInnerHandler(OrderEvent event) {
		Order order = orderService.get(event.getOrderId());
		try {
			// 2.其他通知
			NoticeSetting setting = noticeSettingService.get((byte)99);
			
			// 基本消息
			Map<String, Object> root = Maps.newHashMap();
			root.put("order", order);
			root.put("title", title);
			root.put("type", type);
			root.put("content", content);
			
			// 模板消息
			if (setting.getTemplateMsg() == 1) {
				tempalet_msg(setting.getTmTemplate(), order, root);
			}
			
			// 站内信
			if (setting.getSiteMsg() == 1) {
				site_msg(setting.getSmTemplate(), order, root);
			}
		}catch(Exception e) {
			logger.error("构建消息错误",e);
		}
		return Boolean.TRUE;
	}
}