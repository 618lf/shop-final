package com.tmt.shop.promotion.impl;

import java.math.BigDecimal;

import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.Promotion;
import com.tmt.shop.promotion.PromotionResult;
import com.tmt.shop.promotion.PromotionWrap;

/**
 * 满减处理器
 * @author root
 */
public class MjPromotionHandler extends MultiPromotionHandler{

	@Override
	protected PromotionResult doInnerHandler(PromotionWrap wrap, Promotion promotion, Order order, OrderItem item, BigDecimal amount, int quantity) {
		if (promotion.getType() == 2) {
			return this.doMulti(promotion, order, item, amount, quantity);
		}
		return null;
	}

	/**
	 * 1 为金额计算
	 */
	@Override
	protected Byte getType() {
		return 1;
	}
}