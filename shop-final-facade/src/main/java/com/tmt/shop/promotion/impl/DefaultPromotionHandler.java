package com.tmt.shop.promotion.impl;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tmt.common.utils.BigDecimalUtil;
import com.tmt.common.utils.JsonMapper;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.Promotion;
import com.tmt.shop.entity.PromotionMini;
import com.tmt.shop.event.EventHandler;
import com.tmt.shop.promotion.PromotionHandler;
import com.tmt.shop.promotion.PromotionResult;
import com.tmt.shop.promotion.PromotionWrap;

/**
 * 默认的促销处理器
 * @author root
 */
public abstract class DefaultPromotionHandler implements PromotionHandler{

	protected Logger logger = LoggerFactory.getLogger(EventHandler.class);
	
	protected PromotionHandler handler;
	
	@Override
	public Boolean doHandler(PromotionWrap promotion) {
		PromotionResult flag = null;
		try{
		    flag = this.doInnerHandler(promotion, promotion.getPromotion(), promotion.getOrder(), promotion.getItem(), promotion.getAmount(), promotion.getQuantity());
		} catch(Exception e) {
		    logger.error(JsonMapper.toJson(promotion), e);
		}
		
		// 为true 则不执行下面的处理
		if (flag != null && flag.isResult()) {
			this.doPostHandler(promotion, flag);
			return true;
		}
		
		// 有下一个处理器，则处理
		if (this.handler != null) {
		    return this.handler.doHandler(promotion);
		}
		
		// 没有找到处理器
		return false;
	}

	@Override
	public void setNextHandler(PromotionHandler handler) {
		this.handler = handler;
	}
	
	/**
	 * 子类处理成功之后处理的数据
	 * @param wrap
	 * @param promotion
	 * @param order
	 * @param item
	 * @param amount
	 * @param quantity
	 */
	protected void doPostHandler(PromotionWrap wrap, PromotionResult flag) {
		
		// 有效 且设置为不能使用优惠券
		Promotion promotion = wrap.getPromotion(); Order order = wrap.getOrder();
		if (flag.getReduce() != null && promotion.getIsCouponAllowed() != null
				&& promotion.getIsCouponAllowed() == 0 ) {
			order.setCouponAble(Boolean.FALSE);
		}
		
		// 累计促销值、原因
		PromotionMini pm = order.getPromotions().get(promotion.getId());
		if (flag.getReduce() != null) {
			pm.setPromotionValid(Boolean.TRUE);
			pm.setReduce(BigDecimalUtil.add(pm.getReduce(), flag.getReduce()));
		} else {
			pm.setValidReason(flag.getReason());
		}
	}
	
	/**
	 * 子类需要实现的处理器
	 * @param event
	 * @return
	 */
	protected abstract PromotionResult doInnerHandler(PromotionWrap wrap, Promotion promotion, Order order, OrderItem item, BigDecimal amount, int quantity);
}
