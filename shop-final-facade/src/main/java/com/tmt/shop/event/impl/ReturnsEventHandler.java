package com.tmt.shop.event.impl;

import java.util.Map;

import com.tmt.core.utils.Maps;
import com.tmt.shop.entity.NoticeSetting;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderEvent;
import com.tmt.shop.entity.OrderOpts;

/**
 * 退货处理器
 * 
 * @author root
 */
public class ReturnsEventHandler extends DefaultEventHandler {

	@Override
	protected Boolean doInnerHandler(OrderEvent event) {
		if (event.getOpt() == OrderOpts.RETURNS) {

			Order order = orderService.getWithOrderItems(event.getOrderId());

			try {
				// 2.收货
				NoticeSetting setting = noticeSettingService.get((byte) 6);

				// 基本消息
				Map<String, Object> root = Maps.newHashMap();
				root.put("order", order);

				// 模板消息
				if (setting.getTemplateMsg() == 1) {
					tempalet_msg(setting.getTmTemplate(), order, root);
				}

				// 站内信
				if (setting.getSiteMsg() == 1) {
					site_msg(setting.getSmTemplate(), order, root);
				}
			} catch (Exception e) {
				logger.error("构建消息错误", e);
			}

			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}