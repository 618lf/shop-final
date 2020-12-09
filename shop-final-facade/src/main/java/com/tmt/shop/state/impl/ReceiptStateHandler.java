package com.tmt.shop.state.impl;

import java.util.Date;

import com.tmt.core.utils.time.DateUtils;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderOpts;
import com.tmt.shop.entity.OrderState;

/**
 * 收货处理器
 * @author root
 */
public class ReceiptStateHandler extends DefaultStateHandler {

	@Override
	protected Byte doInnerHandler(OrderState state) {
		if (state.getOpt() == OrderOpts.RECEIPT) {
			if (state.getExpire() != null && DateUtils.after(new Date(), state.getExpire())) {
				Order order = orderService.get(state.getId());
				
				// 会确认是否应该完成
				Boolean flag = orderService.complete(order, null);
				return (byte)(flag ? 1 : 0);
			}
			return 0;
		}
		return -1;
	}
}