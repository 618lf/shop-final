package com.tmt.shop.state.impl;

import java.util.Date;

import com.tmt.common.utils.DateUtil3;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderOpts;
import com.tmt.shop.entity.OrderState;
import com.tmt.shop.enums.PayStatus;
import com.tmt.shop.service.OrderStateServiceFacade;
import com.tmt.shop.service.RefundsServiceFacade;

/**
 * 退款处理流程
 * @author lifeng
 */
public class RefundProcessStateHandler extends DefaultStateHandler{

    RefundsServiceFacade refundsService;
    OrderStateServiceFacade orderStateService;
	private int[] exTimes = new int[]{2, 5, 8, 10, 20, 60}; // 分钟
   	public RefundProcessStateHandler() {
		refundsService = SpringContextHolder.getBean(RefundsServiceFacade.class);
		orderStateService = SpringContextHolder.getBean(OrderStateServiceFacade.class);
	}
	
   	/**
   	 * 每个订单按照
   	 * 2m 5m 8m 10m 20m 60m
   	 */
	@Override
	protected Byte doInnerHandler(OrderState state) {
		if (state.getOpt() == OrderOpts.REFUND_PROCESS) {
			Order order = orderService.get(state.getId()); Date today = DateUtil3.getTimeStampNow();
			if (order.getPaymentStatus() == PayStatus.refunds_process
					&& (state.getExpire() == null || DateUtil3.after(today, state.getExpire()))) {
				// 完成此订单
				Boolean flag = refundsService.refundsProcess(order);
				if (!flag) {
					// 设置下次处理时间
					int region = state.getEcount();
					    region = region >= exTimes.length ? exTimes.length - 1 : region;
					int seconds = exTimes[region];
					state.setExpire(new Date(today.getTime() + seconds * 1000 ));
					orderStateService.expire(state);
				}
				return (byte)(flag ? 1 : 0);
			}
			return 0;
		}
		return -1;
	}
}