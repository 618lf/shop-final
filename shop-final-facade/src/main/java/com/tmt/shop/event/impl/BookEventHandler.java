package com.tmt.shop.event.impl;

import java.util.List;
import java.util.Map;

import com.tmt.common.utils.StringUtil3;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.SpringContextHolder;
import com.tmt.notice.ActualNotice;
import com.tmt.shop.entity.NoticeSetting;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderEvent;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.OrderOpts;
import com.tmt.shop.entity.Product;

/**
 * 下单事件
 * @author root
 */
public class BookEventHandler extends DefaultEventHandler{

	/**
	 * 下单事件
	 * 1. 生成商品快照(在商品保存的时候生成快照 -- 下单的时候直接保存就好了，这样就省了判断的时间)
	 *    但是这种方案会生成很多数据，没必要，还是跟订单走好一点。
	 * 2. 生成下单消息（如果设置的发送消息）
	 * @param data
	 */
	@Override
	protected Boolean doInnerHandler(OrderEvent event) {
		if (event.getOpt() == OrderOpts.BOOK) {
			Order order = orderService.getWithOrderItems(event.getOrderId());
			List<OrderItem> items = order.getItems();
			List<OrderItem> updates = Lists.newArrayList();
			List<Product> products = Lists.newArrayList();
			for(OrderItem item: items) {
				if (StringUtil3.isBlank(item.getSnapshot())) {
					Product product = shopStaticizer.goods_snapshot_build(item.getProductId());
					// 设置为临时的下单时间
					product.setUpdateDate(order.getCreateDate());
					item.setSnapshot(product.getSnapshot());
					
					// 修改快照
					updates.add(item);
					products.add(product);
				}
			}
			
			try {
				// 修改快照
				if (updates.size() != 0) {
					orderService.updateSnapshot(updates, products);
				}
			}catch(Exception e) {
				logger.error("修改快照错误",e);
			}
			
			try {
				// 1.下订单
				NoticeSetting setting = noticeSettingService.get((byte)1);
				
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
				
				// 发送一个实时的通知
				ActualNotice anotice = SpringContextHolder.getBeanQuietly(ActualNotice.class);
				if (anotice != null) {
					anotice.sendMessage(StringUtil3.format("%s 刚刚下单了", order.getCreateName()));
				}
			}catch(Exception e) {
				logger.error("构建消息错误",e);
			}
			
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}