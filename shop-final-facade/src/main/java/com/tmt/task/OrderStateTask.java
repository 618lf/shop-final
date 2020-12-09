package com.tmt.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tmt.core.utils.Lists;
import com.tmt.shop.entity.OrderState;
import com.tmt.shop.service.OrderStateServiceFacade;
import com.tmt.shop.state.StateHandler;
import com.tmt.shop.state.impl.BookStateHandler;
import com.tmt.shop.state.impl.PayStateHandler;
import com.tmt.shop.state.impl.ReceiptStateHandler;
import com.tmt.shop.state.impl.RefundProcessStateHandler;
import com.tmt.shop.state.impl.RefundsStateHandler;
import com.tmt.shop.state.impl.ShippingStateHandler;
import com.tmt.system.entity.Task;
import com.tmt.system.service.TaskExecutor;

/**
 * 订单状态
 * 主要处理 - 发货后或者退款后的事件
 * @author root
 */
public class OrderStateTask implements TaskExecutor {

	@Autowired
	private OrderStateServiceFacade orderStateService;
	
	// 事件处理器
	private StateHandler handler;
		
	public OrderStateTask() {
		handler = new BookStateHandler();
		StateHandler shippingStateHandler = new ShippingStateHandler();
		StateHandler payStateHandler = new PayStateHandler();
		StateHandler receiptStateHandler = new ReceiptStateHandler();
		StateHandler refundProcessStateHandler = new RefundProcessStateHandler();
		StateHandler refundsStateHandler = new RefundsStateHandler();
		handler.setNextHandler(shippingStateHandler);
		shippingStateHandler.setNextHandler(payStateHandler);
		payStateHandler.setNextHandler(receiptStateHandler);
		receiptStateHandler.setNextHandler(refundProcessStateHandler);
		refundProcessStateHandler.setNextHandler(refundsStateHandler);
 	}
	
	@Override
	public Boolean doTask(Task arg0) {
		// 每次更新500条数据(批量的更新)
		List<OrderState> updates = orderStateService.queryUpdateAbles(500);
		
		// 处理状态（有过期时间）失败，则置为可执行
		List<OrderState> _updates = Lists.newArrayList();
		// 处理事件
		for(OrderState event: updates) {
			Byte flag = handler.doHandler(event);
			
			// 表明这种状态处理失败(才需要继续处理 --- 时间没到)
			if (flag == 0) {
				_updates.add(event);
			}
		}
		
		// 处理失败的置为可执行
		orderStateService.enabled(_updates);
		return Boolean.TRUE;
	}

	@Override
	public String getName() {
		return "订单状态跟踪";
	}
}
