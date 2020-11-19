package com.tmt.shop.service;

import com.tmt.system.entity.User;

/**
 * 订单相关的促销
 * @author lifeng
 */
public interface OrderPromotionServiceFacade {

	/**
	 * 可赠送的促销活动， 每次只获取一个
	 * @return
	 */
	public int hasOrderGiveAbleQgPromotion(User user, Long promotionId);
}
