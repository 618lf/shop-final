package com.tmt.report.order;

import java.util.Map;

import com.tmt.core.persistence.JdbcSqlExecutor;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.report.ReportHandler;
import com.tmt.shop.entity.Order;

/**
 * 用户下单
 * 
 * @author lifeng
 */
public class UserOrderHandler implements ReportHandler<Order> {

	ReportHandler<Order> reportHandler;

	// 插入数据
	private String sql = "INSERT INTO REPORT_USER_ORDER(REPORT_DATE, QUANTITY, AMOUNT, USER_ID, USER_NAME) VALUES(:REPORT_DATE, :QUANTITY, :AMOUNT, :USER_ID, :USER_NAME) ON DUPLICATE KEY UPDATE QUANTITY = QUANTITY + 1, AMOUNT = AMOUNT + :AMOUNT";

	@Override
	public void doHandler(Order object) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("USER_ID", object.getCreateId());
		param.put("USER_NAME", object.getCreateName());
		param.put("QUANTITY", 1);
		param.put("AMOUNT", object.getAmount());
		param.put("REPORT_DATE", DateUtils.getFormateDate(object.getCreateDate(), "yyyy-MM-dd HH"));
		JdbcSqlExecutor.insert(sql, param);

		// 下一个处理器
		if (reportHandler != null) {
			reportHandler.doHandler(object);
		}
	}

	@Override
	public void setNextHandler(ReportHandler<Order> handler) {
		reportHandler = handler;
	}
}