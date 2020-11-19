package com.tmt.shop.promotion.impl;

import java.math.BigDecimal;

import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.Promotion;
import com.tmt.shop.promotion.PromotionResult;
import com.tmt.shop.promotion.PromotionWrap;

/**
 * 团购处理器
 * @author root
 */
public class TgPromotionHandler extends MultiPromotionHandler{

	/**
	 * 只处理type = 6的数据
	 */
	@Override
	protected PromotionResult doInnerHandler(PromotionWrap wrap, Promotion promotion, Order order, OrderItem item, BigDecimal amount, int quantity) {
		if (promotion.getType() == 6) {
			BigDecimal reduce = promotion.getReduce(); Double discount = promotion.getDiscount();
			reduce = this.doDiscount(order, item, reduce, amount, discount);
			return PromotionResult.success(reduce);
		}
		return null;
	}

	@Override
	protected Byte getType() {
		return 2;
	}
}
