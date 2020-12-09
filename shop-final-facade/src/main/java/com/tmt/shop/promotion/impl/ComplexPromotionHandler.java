package com.tmt.shop.promotion.impl;

import java.math.BigDecimal;

import com.tmt.core.utils.BigDecimalUtil;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.Promotion;
import com.tmt.shop.promotion.PromotionResult;
import com.tmt.shop.promotion.PromotionWrap;

/**
 * 套餐优惠
 * 
 * @author lifeng
 */
public class ComplexPromotionHandler extends DefaultPromotionHandler {

	@Override
	protected PromotionResult doInnerHandler(PromotionWrap wrap, Promotion promotion, Order order, OrderItem item,
			BigDecimal amount, int quantity) {
		if (promotion.getType() == 11) {
			if (item.getQuantity() > 0) {
				BigDecimal reduce = BigDecimalUtil.mul(promotion.getReduce(), BigDecimal.valueOf(item.getQuantity()));
				order.setPromotionDiscount(BigDecimalUtil.add(reduce, order.getPromotionDiscount()));
				return PromotionResult.success(reduce);
			}
			return PromotionResult.success("套装错误");
		}
		return null;
	}

}
