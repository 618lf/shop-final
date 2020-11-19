package com.tmt.report.payment;

import java.util.Map;

import com.tmt.core.persistence.JdbcSqlExecutor;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.report.ReportHandler;
import com.tmt.shop.entity.Payment;

/**
 * 销售额统计报表
 * 
 * @author lifeng
 */
public class SalesHandler implements ReportHandler<Payment> {

	ReportHandler<Payment> reportHandler;

	// 插入数据
	private String sql = "INSERT INTO REPORT_SALES(REPORT_DATE, QUANTITY, AMOUNT) VALUES(:REPORT_DATE, :QUANTITY, :AMOUNT) ON DUPLICATE KEY UPDATE QUANTITY = QUANTITY + 1, AMOUNT = AMOUNT + :AMOUNT";

	@Override
	public void doHandler(Payment object) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("QUANTITY", 1);
		param.put("AMOUNT", object.getAmount());
		param.put("REPORT_DATE", DateUtils.getFormateDate(object.getPaymentDate(), "yyyy-MM-dd HH"));
		JdbcSqlExecutor.insert(sql, param);

		// 下一个处理器
		if (reportHandler != null) {
			reportHandler.doHandler(object);
		}
	}

	@Override
	public void setNextHandler(ReportHandler<Payment> handler) {
		reportHandler = handler;
	}
}
