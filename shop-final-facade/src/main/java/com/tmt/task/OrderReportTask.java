package com.tmt.task;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.report.ReportHandler;
import com.tmt.report.order.OrdersHandler;
import com.tmt.report.order.UserOrderHandler;
import com.tmt.report.product.ProductHandler;
import com.tmt.shop.entity.Order;
import com.tmt.shop.service.OrderItemServiceFacade;
import com.tmt.shop.service.OrderServiceFacade;

/**
 * 统计周期更具设置而定
 * @author root
 */
public class OrderReportTask extends AbstractReportTask {

	@Autowired
	private OrderServiceFacade orderService;
	@Autowired
	private OrderItemServiceFacade oitemService;
	private ReportHandler<Order> handler;
	
	public OrderReportTask() {
		handler = new OrdersHandler();
		ReportHandler<Order> uoHandler = new UserOrderHandler();
		ReportHandler<Order> pHandler = new ProductHandler();
		handler.setNextHandler(uoHandler);
		uoHandler.setNextHandler(pHandler);
	}

	@Override
	public String getName() {
		return "订单统计";
	}

	@Override
	protected void doInner(Map<String, Date> dates) {
		Date start = dates.get("start_date");
		Date end = dates.get("end_date");
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria();
		c.andDateGreaterThanOrEqualTo("CREATE_DATE", start);
		c.andDateLessThan("CREATE_DATE", end);
		int iCount = orderService.countByCondition(qc);
		PageParameters param = new PageParameters(1, 1000, iCount);
		int iPage = param.getPageCount();
		for(int i=1; i<= iPage; i++) {
			param.setPageIndex(i);
			Page page = orderService.queryForPage(qc, param);
			List<Order> orders = page.getData();
			
			// 日结
			for(Order order: orders) {
				order.setItems(oitemService.queryItemsByOrderId(order.getId()));
				handler.doHandler(order);
			}
		}
	}
}