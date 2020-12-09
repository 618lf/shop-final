package com.tmt.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tmt.shop.entity.OrderEvent;
import com.tmt.shop.event.EventHandler;
import com.tmt.shop.event.impl.ApplyRefundEventHandler;
import com.tmt.shop.event.impl.ApplyReturnsEventHandler;
import com.tmt.shop.event.impl.BookEventHandler;
import com.tmt.shop.event.impl.PayEventHandler;
import com.tmt.shop.event.impl.ReceiptEventHandler;
import com.tmt.shop.event.impl.RefundProcessEventHandler;
import com.tmt.shop.event.impl.RefundsEventHandler;
import com.tmt.shop.event.impl.ReturnsEventHandler;
import com.tmt.shop.event.impl.ShippingEventHandler;
import com.tmt.shop.service.OrderEventServiceFacade;
import com.tmt.system.entity.Task;
import com.tmt.system.service.TaskExecutor;

/**
 * 订单事件
 * --- 主要处理相关的事件
 * @author root
 */
public class OrderEventTask implements TaskExecutor {

	@Autowired
	private OrderEventServiceFacade orderEventService;
	
	// 事件处理器
	private EventHandler handler;
	
	public OrderEventTask() {
		handler = new BookEventHandler();
		EventHandler payEventHandler = new PayEventHandler();
		EventHandler shippingEventHandler = new ShippingEventHandler();
		EventHandler receiptEventHandler = new ReceiptEventHandler();
		EventHandler applyReturnsEventHandler = new ApplyReturnsEventHandler();
		EventHandler returnsEventHandler = new ReturnsEventHandler();
		EventHandler applyRefundEventHandler = new ApplyRefundEventHandler();
		EventHandler refundProcessEventHandler = new RefundProcessEventHandler();
		EventHandler refundsEventHandler = new RefundsEventHandler();
		
		// 下一个处理器
		handler.setNextHandler(payEventHandler);
		payEventHandler.setNextHandler(shippingEventHandler);
		shippingEventHandler.setNextHandler(receiptEventHandler);
		receiptEventHandler.setNextHandler(applyReturnsEventHandler);
		applyReturnsEventHandler.setNextHandler(returnsEventHandler);
		returnsEventHandler.setNextHandler(applyRefundEventHandler);
		applyRefundEventHandler.setNextHandler(refundProcessEventHandler);
		refundProcessEventHandler.setNextHandler(refundsEventHandler);
	}
	
	@Override
	public Boolean doTask(Task arg0) {
		// 每次更新500条数据(批量的更新)
		List<OrderEvent> updates = orderEventService.queryUpdateAbles(500);
		
		// 处理事件
		for(OrderEvent event: updates) {
			handler.doHandler(event);
		}
		
		// 处理完成之后删除
		orderEventService.delete(updates);
		// for gc
		updates.clear();
		updates = null;
		return Boolean.TRUE;
	}
	
	@Override
	public String getName() {
		return "订单事件";
	}
}
