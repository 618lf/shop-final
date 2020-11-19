package com.tmt.report.product;

import java.util.List;
import java.util.Map;

import com.tmt.core.persistence.JdbcSqlExecutor;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.report.ReportHandler;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;

public class ProductHandler implements ReportHandler<Order> {

	// 插入数据
	private String sql = "INSERT INTO REPORT_PRODUCT(REPORT_DATE, QUANTITY, AMOUNT, PRODUCT_ID, PRODUCT_NAME) VALUES(:REPORT_DATE, :QUANTITY, :AMOUNT, :PRODUCT_ID, :PRODUCT_NAME) ON DUPLICATE KEY UPDATE QUANTITY = QUANTITY + 1, AMOUNT = AMOUNT + :AMOUNT";

	@Override
	public void doHandler(Order object) {
		List<OrderItem> items = object.getItems();
		for (OrderItem item : items) {
			Map<String, Object> param = Maps.newHashMap();
			param.put("PRODUCT_ID", item.getProductId());
			param.put("PRODUCT_NAME", item.getProductName());
			param.put("QUANTITY", item.getQuantity());
			param.put("AMOUNT", item.getTotal());
			param.put("REPORT_DATE", DateUtils.getFormateDate(object.getCreateDate(), "yyyy-MM-dd HH"));
			JdbcSqlExecutor.insert(sql, param);
		}
	}

	@Override
	public void setNextHandler(ReportHandler<Order> handler) {

	}
}
