package com.tmt.shop.promotion.impl;

import java.math.BigDecimal;

import com.tmt.core.utils.BigDecimalUtil;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.Promotion;
import com.tmt.shop.promotion.PromotionResult;
import com.tmt.shop.promotion.PromotionWrap;

/**
 * 包邮处理器
 * 
 * @author root
 */
public class ByPromotionHandler extends DefaultPromotionHandler {

	/**
	 * 只处理type = 1的数据
	 */
	@Override
	protected PromotionResult doInnerHandler(PromotionWrap wrap, Promotion promotion, Order order, OrderItem item,
			BigDecimal amount, int quantity) {
		if (promotion.getType() == 5) {
			// 是否满足需求
			if (promotion.getIsPrice() != null && promotion.getIsPrice() == 1
					&& !BigDecimalUtil.biggerThen(promotion.getOrderPrice(), amount)) {
				BigDecimal reduce = order.getFreight();
				order.setPromotionDiscount(BigDecimalUtil.add(reduce, order.getPromotionDiscount()));
				order.setPostageAble(Boolean.FALSE);
				return PromotionResult.success(reduce);

			} else if (promotion.getIsQuantity() != null && promotion.getIsQuantity() == 1
					&& quantity >= promotion.getOrderQuantity()) {
				BigDecimal reduce = order.getFreight();
				order.setPromotionDiscount(BigDecimalUtil.add(reduce, order.getPromotionDiscount()));
				order.setPostageAble(Boolean.FALSE);
				return PromotionResult.success(reduce);
			}
			String reason = "促销错误";
			if (promotion.getIsPrice() != null && promotion.getIsPrice() == 1) {
				reason = new StringBuilder("相关商品金额未满").append(promotion.getOrderPrice().toPlainString()).append("元")
						.toString();
			} else if (promotion.getIsQuantity() != null && promotion.getIsQuantity() == 1) {
				reason = new StringBuilder("相关商品数量未满").append(promotion.getOrderQuantity()).append("件").toString();
			}
			return PromotionResult.success(reason);
		}
		return null;
	}
}
