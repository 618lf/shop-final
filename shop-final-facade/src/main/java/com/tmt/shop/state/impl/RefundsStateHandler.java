package com.tmt.shop.state.impl;

import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderOpts;
import com.tmt.shop.entity.OrderState;
import com.tmt.shop.enums.PayStatus;

/**
 * 退款状态处理(直接变为完成)
 * @author root
 */
public class RefundsStateHandler extends DefaultStateHandler{

	/**
	 * 退款之后，如果是完全退款状态则
	 */
	@Override
	protected Byte doInnerHandler(OrderState state) {
		if (state.getOpt() == OrderOpts.REFUNDS) {
			Order order = orderService.get(state.getId());
			if (order.getPaymentStatus() == PayStatus.refunded) {
				
				// 完成此订单
				Boolean flag = orderService.complete(order, null);
				return (byte)(flag ? 1 : 0);
			}
			return 0;
		}
		return -1;
	}
}