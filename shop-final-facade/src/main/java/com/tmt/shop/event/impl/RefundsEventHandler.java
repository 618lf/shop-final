package com.tmt.shop.event.impl;

import java.util.List;
import java.util.Map;

import com.tmt.common.utils.Maps;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.shop.entity.NoticeSetting;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderEvent;
import com.tmt.shop.entity.OrderOpts;
import com.tmt.shop.entity.Refunds;
import com.tmt.shop.service.RefundsServiceFacade;

/**
 * 退款处理器
 * @author root
 */
public class RefundsEventHandler extends DefaultEventHandler{

	RefundsServiceFacade refundsService;
	
	public RefundsEventHandler() {
		refundsService = SpringContextHolder.getBean(RefundsServiceFacade.class);
	}
	
	@Override
	protected Boolean doInnerHandler(OrderEvent event) {
		if (event.getOpt() == OrderOpts.REFUNDS) {
			
			Order order = orderService.get(event.getOrderId());
			
			try {
				// 2.收货
				NoticeSetting setting = noticeSettingService.get((byte)9);
				
				// 退款信息
				List<Refunds> refundss = refundsService.queryRefundsByOrderId(event.getOrderId());
				Refunds refunds = refundss != null && refundss.size() > 0 ? refundss.get(0): null;
						
				// 基本消息
				Map<String, Object> root = Maps.newHashMap();
				root.put("order", order);
				root.put("refunds", refunds);
				
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