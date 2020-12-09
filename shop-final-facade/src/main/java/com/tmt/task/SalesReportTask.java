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
import com.tmt.report.payment.SalesHandler;
import com.tmt.shop.entity.Payment;
import com.tmt.shop.service.PaymentServiceFacade;

/**
 * 统计周期更具设置而定
 * @author root
 */
public class SalesReportTask extends AbstractReportTask{

	@Autowired
	private PaymentServiceFacade paymentService;
	private ReportHandler<Payment> handler;
	
	public SalesReportTask() {
		handler = new SalesHandler();
	}
	
	@Override
	public String getName() {
		return "销售额统计";
	}

	@Override
	protected void doInner(Map<String, Date> dates) {
		Date start = dates.get("start_date");
		Date end = dates.get("end_date");
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria();
		c.andEqualTo("PAY_FLAG", 1);
		c.andDateGreaterThanOrEqualTo("PAYMENT_DATE", start);
		c.andDateLessThan("PAYMENT_DATE", end);
		int iCount = paymentService.countByCondition(qc);
		PageParameters param = new PageParameters(1, 1000, iCount);
		int iPage = param.getPageCount();
		for(int i=1; i<= iPage; i++) {
			param.setPageIndex(i);
			Page page = paymentService.queryForPage(qc, param);
			List<Payment> orders = page.getData();
			
			// 日结
			for(Payment order: orders) {
				handler.doHandler(order);
			}
		}
	}
}
