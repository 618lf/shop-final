package com.tmt.shop.promotion.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.utils.BigDecimalUtil;
import com.tmt.common.utils.DateUtil3;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.Promotion;
import com.tmt.shop.entity.PromotionMini;
import com.tmt.shop.promotion.PromotionResult;
import com.tmt.shop.promotion.PromotionWrap;

/**
 * 初始化处理器
 * @author root
 */
public class InitpromotionHandler extends DefaultPromotionHandler{

	/**
	 * 实际是交给下一个处理器来处理
	 */
	@Override
	protected PromotionResult doInnerHandler(PromotionWrap wrap, Promotion promotion, Order order, OrderItem item, BigDecimal a, int q) {
		
		Date now = DateUtil3.getTimeStampNow();
		
		// 本身的有效性 -- 后面的不需要处理
		if (promotion.getIsEnabled() == 0 || !(promotion.getEndDate() != null && DateUtil3.after(promotion.getEndDate(), now))
				|| !(promotion.getBeginDate() != null && DateUtil3.before(promotion.getBeginDate(), now))) {
			return PromotionResult.success("促销已过期");
		}
		
		// 校验是否符合规格
		if (!(promotion.getPs() == null || promotion.getPs().isEmpty() || promotion.getPs().containsKey(item.getProductId()))) {
			return PromotionResult.success("促销变更");
		}
		
		// 需要计算额度(满X)
		BigDecimal _amount = BigDecimal.ZERO; int _quantity = 0;
		if (promotion.getType() == 2 || promotion.getType() == 3
				|| promotion.getType() == 5
				|| promotion.getType() == 8) {
			
			// 这些促销只能处理一次
			PromotionMini pm = order.getPromotions().get(promotion.getId());
			if (pm != null) {
				return PromotionResult.success("促销错误");
			}
			
			List<OrderItem> items = order.getItems();
			for(OrderItem _item: items) {
				// 满足这些条件才需要汇总
				if (!IdGen.isInvalidId(_item.getPromotions())
						&& promotion.getId().compareTo(_item.getPromotions()) == 0
						&& (promotion.getPs() == null || promotion.getPs().isEmpty() || promotion.getPs().containsKey(_item.getProductId()))) {
					_amount = BigDecimalUtil.add(_amount, _item.getTotal());
					_quantity = _quantity + _item.getQuantity();
				}
			}
		} 
		
		// 其他情况
		else {
			_amount = item.getTotal();
			_quantity = item.getQuantity();
		}
		
		// 初始化一个处理结果
		if (!order.getPromotions().containsKey(promotion.getId())) {
			PromotionMini pm = new PromotionMini();
			pm.setPromotion(promotion);
			order.getPromotions().put(promotion.getId(), pm);
		}
		
		// 设置计算的依据
		wrap.setAmount(_amount);
		wrap.setQuantity(_quantity);
		return null;
	}
}
