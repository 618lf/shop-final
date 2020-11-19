package com.tmt.shop.event.impl;

import java.util.List;
import java.util.Map;

import com.tmt.common.utils.Maps;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.shop.entity.NoticeSetting;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderEvent;
import com.tmt.shop.entity.OrderOpts;
import com.tmt.shop.entity.Shipping;
import com.tmt.shop.service.ShippingServiceFacade;

/**
 * 发货动作
 * @author root
 */
public class ShippingEventHandler extends DefaultEventHandler{
	
	ShippingServiceFacade shippingService;
	
	public ShippingEventHandler() {
		shippingService = SpringContextHolder.getBean(ShippingServiceFacade.class);
	}

	@Override
	protected Boolean doInnerHandler(OrderEvent event) {
		if (event.getOpt() == OrderOpts.SHIPPING) {
			
			Order order = orderService.get(event.getOrderId());
			
			try {
				// 2.发货
				NoticeSetting setting = noticeSettingService.get((byte)3);
				
				// 物流数据
				List<Shipping> shippings = shippingService.queryShippingsByOrderId(order.getId());
				Shipping shipping = shippings.get(0);
				
				// 基本消息
				Map<String, Object> root = Maps.newHashMap();
				root.put("order", order);
				root.put("shipping", shipping);
				
				// 模板消息
				if (setting.getTemplateMsg() == 1) {
					tempalet_msg(setting.getTmTemplate(), order, root);
				}
				
				// 站内信
				if (setting.getSiteMsg() == 1) {
					site_msg(setting.getSmTemplate(), order, root);
				}
				
				// 短信
				if (setting.getSmsMsg() == 1) {
					
				}
			}catch(Exception e) {
				logger.error("构建消息错误",e);
			}
			
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}