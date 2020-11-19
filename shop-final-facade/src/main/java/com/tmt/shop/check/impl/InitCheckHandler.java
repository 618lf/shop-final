package com.tmt.shop.check.impl;

import java.util.List;
import java.util.Map;

import com.tmt.core.utils.BigDecimalUtil;
import com.tmt.core.utils.Ints;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.Maps;
import com.tmt.shop.check.CheckItem;
import com.tmt.shop.check.CheckWrap;
import com.tmt.shop.check.OrderCheckResult;
import com.tmt.shop.entity.Complex;
import com.tmt.shop.entity.ComplexProduct;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.Product;
import com.tmt.shop.entity.Salable;
import com.tmt.shop.utils.ComplexUtils;
import com.tmt.shop.utils.ProductUtils;

/**
 * 初始化
 * 
 * @author lifeng
 */
public class InitCheckHandler extends DefaultCheckHandler {

	@Override
	protected OrderCheckResult doInnerHandler(CheckWrap check) {
		Order order = check.getOrder();
		List<OrderItem> items = order.getItems();
		if (!(items != null && items.size() != 0)) {
			return OrderCheckResult.orderInvalid();
		}
		Map<Long, CheckItem> megers = Maps.newHashMap();
		for (OrderItem item : items) {
			Salable salable = null;
			Complex complex = null;
			Product product = null;
			if (item.getGoodsId() == null) {
				complex = ComplexUtils.getComplex(item.getProductId());
				salable = complex;
			} else {
				product = ProductUtils.getProduct(item.getProductId());
				salable = product;
			}

			// 校验信息变动
			OrderCheckResult result = this.check(salable, item);
			if (result != null) {
				return result;
			}

			// 合并信息
			if (complex != null && complex.getProducts() != null) {
				List<ComplexProduct> cps = complex.getProducts();
				for (ComplexProduct cp : cps) {
					CheckItem _item = megers.get(cp.getProductId());
					if (_item == null) {
						_item = new CheckItem();
						megers.put(cp.getProductId(), _item);
					}
					_item.setId(item.getProductId());
					_item.setProductId(cp.getProductId());
					_item.setGoodsId(cp.getProduct().getGoodsId());
					_item.setName(cp.getProduct().getName());
					_item.setQuantity(Ints.addI(_item.getQuantity(), item.getQuantity() * cp.getQuantity()));
				}
			} else if (product != null) {
				CheckItem _item = megers.get(item.getProductId());
				if (_item == null) {
					_item = new CheckItem();
					megers.put(item.getProductId(), _item);
				}
				_item.setId(item.getProductId());
				_item.setProductId(item.getProductId());
				_item.setGoodsId(product.getGoodsId());
				_item.setName(product.getName());
				_item.setQuantity(Ints.addI(_item.getQuantity(), item.getQuantity()));
			}
		}
		check.setItems(Lists.newArrayList(megers.values()));
		return null;
	}

	private OrderCheckResult check(Salable salable, OrderItem item) {
		if (salable == null || salable.getIsMarketable() != Product.YES || salable.getIsSalestate() != Product.YES
				|| !BigDecimalUtil.valueEqual(salable.getPrice(), item.getPrice())
				|| salable.getWeight().compareTo(item.getWeight()) != 0) {
			return OrderCheckResult.productModify(salable.getName());
		}
		return null;
	}
}