package com.tmt.shop.event.impl;

import java.util.Map;

import com.tmt.core.utils.Maps;
import com.tmt.shop.entity.NoticeSetting;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderEvent;
import com.tmt.shop.entity.OrderOpts;

/**
 * 申请退款处理器
 * @author root
 */
public class ApplyRefundEventHandler extends DefaultEventHandler{

	@Override
	protected Boolean doInnerHandler(OrderEvent event) {
		if (event.getOpt() == OrderOpts.APPLY_REFUND) {
			Order order = orderService.get(event.getOrderId());
			
			try {
				// 2.退货申请
				NoticeSetting setting = noticeSettingService.get((byte)7);
				
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
			}catch(Exception e) {
				logger.error("构建消息错误",e);
			}
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}
