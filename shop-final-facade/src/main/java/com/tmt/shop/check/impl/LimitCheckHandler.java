package com.tmt.shop.check.impl;

import java.util.List;

import com.tmt.shop.check.CheckItem;
import com.tmt.shop.check.CheckWrap;
import com.tmt.shop.check.OrderCheckResult;

/**
 * 限购
 * @author lifeng
 */
public class LimitCheckHandler extends DefaultCheckHandler{

	@Override
	protected OrderCheckResult doInnerHandler(CheckWrap check) {
		List<CheckItem> items = check.getItems();
		for(CheckItem item: items) {
			Boolean result = this.productService.checkGoodsLimit(item.getGoodsId(), check.getUser(), item.getQuantity());
		    if (!result) {
		    	return OrderCheckResult.beyondGoodsLimit(item.getName());
		    }
		}
		return null;
	}
}