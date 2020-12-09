package com.tmt.shop.promotion.impl;

import java.math.BigDecimal;

import com.tmt.core.utils.BigDecimalUtil;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.Promotion;
import com.tmt.shop.promotion.PromotionResult;
import com.tmt.shop.promotion.PromotionWrap;

/**
 * 满赠送
 * @author root
 */
public class MzsPromotionHandler extends DefaultPromotionHandler{

	/**
	 * 只处理type = 8的数据
	 */
	@Override
	protected PromotionResult doInnerHandler(PromotionWrap wrap, Promotion promotion, Order order, OrderItem item, BigDecimal amount, int quantity) {
		if (promotion.getType() == 8) {
			// 是否满足需求
			if (promotion.getIsPrice() != null && promotion.getIsPrice() == 1 && !BigDecimalUtil.biggerThen(promotion.getOrderPrice(), amount)) {
				return PromotionResult.success(BigDecimal.ZERO);
			} else if(promotion.getIsQuantity() != null && promotion.getIsQuantity() == 1 && quantity >= promotion.getOrderQuantity()) {
				return PromotionResult.success(BigDecimal.ZERO);
			}
			String reason = "促销错误";
			if (promotion.getIsPrice() != null && promotion.getIsPrice() == 1) {
				reason = new StringBuilder("订单金额未满").append(promotion.getOrderPrice().toPlainString()).append("元").toString();
			} else if(promotion.getIsQuantity() != null && promotion.getIsQuantity() == 1){
				reason = new StringBuilder("订单数量未满").append(promotion.getOrderQuantity()).append("件").toString();
			}
			return PromotionResult.success(reason);
		}
		return null;
	}
}
