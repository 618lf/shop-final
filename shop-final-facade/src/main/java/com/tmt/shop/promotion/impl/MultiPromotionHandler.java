package com.tmt.shop.promotion.impl;

import java.math.BigDecimal;

import com.tmt.core.utils.BigDecimalUtil;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.Promotion;
import com.tmt.shop.entity.PromotionExt;
import com.tmt.shop.promotion.PromotionResult;

/**
 * 支持多级促销
 * @author root
 */
public abstract class MultiPromotionHandler extends DefaultPromotionHandler{

	/**
	 * 执行多级促销
	 * @return
	 */
	public PromotionResult doMulti(Promotion promotion, Order order, OrderItem item, BigDecimal amount, int quantity) {
		BigDecimal reduce = null;
		if (promotion.getIsPrice() != null && promotion.getIsPrice() == 1) {
			reduce = this.doPriceMulti(promotion, order, item, amount);
		} else if(promotion.getIsQuantity() != null && promotion.getIsQuantity() == 1) {
			reduce = this.doQuantityMulti(promotion, order, item, amount, quantity);
		}
		// 错误原因
		if (reduce == null) {
			String reason = "促销错误";
			if (promotion.getIsPrice() != null && promotion.getIsPrice() == 1) {
				reason = new StringBuilder("相关商品金额未满").append(promotion.getOrderPrice().toPlainString()).append("元").toString();
			} else if(promotion.getIsQuantity() != null && promotion.getIsQuantity() == 1){
				reason = new StringBuilder("相关商品数量未满").append(promotion.getOrderQuantity()).append("件").toString();
			}
			return PromotionResult.success(reason);
		}
		return PromotionResult.success(reduce);
	}
	
	// 金额多级
	private BigDecimal doPriceMulti(Promotion promotion, Order order, OrderItem item, BigDecimal amount) {
		// 不满足第一层促销
		if (BigDecimalUtil.biggerThen(promotion.getOrderPrice(), amount)) {
			return null;
		}
		// 折扣 和直减
		BigDecimal reduce = null;
		Double discount = null;
		PromotionExt ext = promotion.getExt();
		if (ext != null && ext.getOrderPrice2() != null
				&& !BigDecimalUtil.biggerThen(ext.getOrderPrice2(), amount)) {
			reduce = ext.getReduce2(); discount = ext.getDiscount2();
		} else if(ext != null && ext.getOrderPrice1() != null
				&& !BigDecimalUtil.biggerThen(ext.getOrderPrice1(), amount)) {
			reduce = ext.getReduce1(); discount = ext.getDiscount1();
		} else {
			reduce = promotion.getReduce(); discount = promotion.getDiscount();
		}
		return this.doDiscount(order, item, reduce, amount, discount);
	}
	
	// 数量多级
	private BigDecimal doQuantityMulti(Promotion promotion, Order order, OrderItem item, BigDecimal amount, int quantity) {
		if (quantity < promotion.getOrderQuantity()) {
			return null;
		}
		// 折扣 和直减
		BigDecimal reduce = null;
		Double discount = null;
		PromotionExt ext = promotion.getExt();
		if (ext != null && ext.getOrderPrice2() != null && quantity >= ext.getOrderQuantity2()) {
			reduce = ext.getReduce2(); discount = ext.getDiscount2();
		} else if(ext != null && ext.getOrderPrice1() != null && quantity >= ext.getOrderQuantity1()) {
			reduce = ext.getReduce1(); discount = ext.getDiscount1();
		} else {
			reduce = promotion.getReduce(); discount = promotion.getDiscount();
		}
		return this.doDiscount(order, item, reduce, amount, discount);
	}
	
	// 计算减少的金额 -- 默认直接返回
	protected BigDecimal doDiscount(Order order, OrderItem item, BigDecimal reduce, BigDecimal amount, Double discount) {
		Byte type = this.getType(); BigDecimal freduce = null;
		if (type == 1) {
			order.setPromotionDiscount(BigDecimalUtil.add(reduce, order.getPromotionDiscount()));
			freduce = reduce;
		} else if(type == 2) {
			BigDecimal _reduce = BigDecimalUtil.sub(amount, BigDecimalUtil.mul(amount, BigDecimal.valueOf(discount)));
			order.setPromotionDiscount(BigDecimalUtil.add(_reduce, order.getPromotionDiscount()));
			freduce = _reduce;
		}
		return freduce;
	}
	
	// 直减 或 打折
	protected abstract Byte getType();
}
