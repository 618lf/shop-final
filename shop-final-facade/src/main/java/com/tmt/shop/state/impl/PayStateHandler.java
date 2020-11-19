package com.tmt.shop.state.impl;

import java.util.Date;

import com.tmt.common.utils.DateUtil3;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderOpts;
import com.tmt.shop.entity.OrderState;

public class PayStateHandler extends DefaultStateHandler{

	@Override
	protected Byte doInnerHandler(OrderState state) {
		if (state.getOpt() == OrderOpts.PAY) {
			if (state.getExpire() != null && DateUtil3.after(new Date(),state.getExpire())) {
                Order order = orderService.get(state.getId());
				
				// 会确认是否应该完成
				Boolean flag = orderService.complete(order, null);
				return (byte)(flag ? 1 : 0);
			}
			
			// 处理失败
			return 0;
		}
		// 继续下面的处理
		return -1;
	}
}
