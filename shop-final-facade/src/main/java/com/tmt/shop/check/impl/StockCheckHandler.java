package com.tmt.shop.check.impl;

import java.util.List;

import com.tmt.shop.check.CheckItem;
import com.tmt.shop.check.CheckWrap;
import com.tmt.shop.check.OrderCheckResult;
import com.tmt.shop.entity.Product;

/**
 * 库存校验
 * @author lifeng
 */
public class StockCheckHandler extends DefaultCheckHandler{

	@Override
	protected OrderCheckResult doInnerHandler(CheckWrap check) {
		List<CheckItem> items = check.getItems();
		for(CheckItem item: items) {
			Product product = this.productService.lockStoreProduct(item.getProductId());
			Integer store = product.getStore() - product.getFreezeStore();
			item.setStore(store);
			if (store == null || store < 0 || store.compareTo(item.getQuantity())< 0) {
				return OrderCheckResult.outOfStock(item);
			}
		}
		return null;
	}
}