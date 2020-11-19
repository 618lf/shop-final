package com.tmt.shop.promotion.impl;

import java.math.BigDecimal;

import com.tmt.common.utils.BigDecimalUtil;
import com.tmt.common.utils.Ints;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.Promotion;
import com.tmt.shop.promotion.PromotionResult;
import com.tmt.shop.promotion.PromotionWrap;
import com.tmt.shop.service.OrderPromotionServiceFacade;

/**
 * 抢购
 * @author lifeng
 */
public class QgPromotionHandler extends MultiPromotionHandler{

	/**
	 * 只处理type = 8的数据
	 * 主要是限购的数据
	 */
	@Override
	protected PromotionResult doInnerHandler(PromotionWrap wrap, Promotion promotion, Order order, OrderItem item, BigDecimal amount, int quantity) {
		if (promotion.getType() == 7) {
			// 订单相关促销
			OrderPromotionServiceFacade promotionService = SpringContextHolder.getBean(OrderPromotionServiceFacade.class);
			int getno = promotionService.hasOrderGiveAbleQgPromotion(wrap.getUser(), promotion.getId());
			if (getno == 0 || getno < promotion.getGetno()) {
				BigDecimal reduce = promotion.getReduce(); Double discount = promotion.getDiscount();
				
				// 如果购买个数大于促销限制的个数
				quantity = Ints.min(promotion.getGetno(), item.getQuantity());
				amount = BigDecimalUtil.mul(item.getPrice(), BigDecimal.valueOf(quantity));
				BigDecimal _reduce = this.doDiscount(order, item, reduce, amount, discount);
				return PromotionResult.success(_reduce);
			}
			String reason = new StringBuilder("每个用户限购").append(getno).append("件").toString();
			return PromotionResult.success(reason);
		}
		return null;
	}
	
	/**
	 * 2 为折扣计算
	 */
	@Override
	protected Byte getType() {
		return 2;
	}
}