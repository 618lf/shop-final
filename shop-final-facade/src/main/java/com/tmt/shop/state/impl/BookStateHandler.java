package com.tmt.shop.state.impl;

import java.util.Date;

import com.tmt.common.utils.DateUtil3;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderOpts;
import com.tmt.shop.entity.OrderState;
import com.tmt.shop.entity.PaymentMethod.Type;

/**
 * 下单状态
 * @author root
 */
public class BookStateHandler extends DefaultStateHandler{

	/**
	 * 下单状态
	 * @param data
	 */
	@Override
	protected Byte doInnerHandler(OrderState state) {
		if (state.getOpt() == OrderOpts.BOOK) {
			if (state.getExpire() != null && DateUtil3.after(new Date(),state.getExpire())) {
				Order order = orderService.get(state.getId());
				
				// 先发货的自动去审核
				if (order.getPaymentMethodType() == Type.BEFORE_DELIVER) {
					// 会确认是否应该取消
					Boolean flag = orderService.confirm(order, null);
					return (byte)(flag ? 1 : 0);
				} 
				
				// 先付款的自动去取消
				else {
					// 会确认是否应该取消
					Boolean flag = orderService.cancel(order, null);
					return (byte)(flag ? 1 : 0);
				}
			}
			
			// 处理失败
			return 0;
		}
		// 继续下面的处理
		return -1;
	}
}