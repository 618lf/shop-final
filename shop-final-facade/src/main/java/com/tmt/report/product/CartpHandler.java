package com.tmt.report.product;

import java.util.Map;

import com.tmt.core.persistence.JdbcSqlExecutor;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.report.ReportHandler;
import com.tmt.shop.entity.CartItem;

public class CartpHandler implements ReportHandler<CartItem> {

	// 插入数据
	private String sql = "INSERT INTO REPORT_CART_PRODUCT(REPORT_DATE, QUANTITY, AMOUNT, PRODUCT_ID, PRODUCT_NAME) VALUES(:REPORT_DATE, :QUANTITY, :AMOUNT, :PRODUCT_ID, :PRODUCT_NAME) ON DUPLICATE KEY UPDATE QUANTITY = QUANTITY + 1, AMOUNT = AMOUNT + :AMOUNT";

	@Override
	public void doHandler(CartItem item) {
		Map<String, Object> param = Maps.newHashMap();
		param.put("PRODUCT_ID", item.getProductId());
		param.put("PRODUCT_NAME", item.getProduct().getName());
		param.put("QUANTITY", item.getQuantity());
		param.put("AMOUNT", item.getPrice());
		param.put("REPORT_DATE", DateUtils.getFormateDate(item.getCreateDate(), "yyyy-MM-dd HH"));
		JdbcSqlExecutor.insert(sql, param);
	}

	@Override
	public void setNextHandler(ReportHandler<CartItem> handler) {

	}

}
