package com.tmt.shop.promotion.impl;

import java.math.BigDecimal;

import com.tmt.common.utils.BigDecimalUtil;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.Promotion;
import com.tmt.shop.entity.PromotionProduct;
import com.tmt.shop.promotion.PromotionResult;
import com.tmt.shop.promotion.PromotionWrap;

public class ZkPromotionHandler extends DefaultPromotionHandler{

	/**
	 * 只处理type = 4的数据
	 */
	@Override
	protected PromotionResult doInnerHandler(PromotionWrap wrap, Promotion promotion, Order order, OrderItem item, BigDecimal amount, int quantity) {
		if (promotion.getType() == 4) {
			if (promotion.getPs() != null && promotion.getPs().containsKey(item.getProductId())) {
				PromotionProduct product = promotion.getPs().get(item.getProductId());
				BigDecimal reduce = this.doDiscount(order, item, product.getDiscount(), product.getScale());
				return PromotionResult.success(reduce);
			}
			return PromotionResult.success("促销错误");
		}
		return null;
	}
	
	// 计算减少的金额 -- 默认直接返回
	protected BigDecimal doDiscount(Order order, OrderItem item, Double discount, int scale) {
		BigDecimal _reduce = BigDecimalUtil.sub(item.getTotal(), BigDecimalUtil.floor(BigDecimalUtil.mul(item.getTotal(), BigDecimal.valueOf(discount)), scale));
		order.setPromotionDiscount(BigDecimalUtil.add(_reduce, order.getPromotionDiscount()));
		return _reduce;
	}
}