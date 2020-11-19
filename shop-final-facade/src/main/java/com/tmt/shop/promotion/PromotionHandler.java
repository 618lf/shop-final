package com.tmt.shop.promotion;


/**
 * 优惠促销处理器
 * @author root
 */
public interface PromotionHandler {

	/**
	 * 事件处理器
	 * @param event
	 */
	public Boolean doHandler(PromotionWrap promotion);
	
	/**
	 * 下一个处理器
	 * @param handler
	 */
	public void setNextHandler(PromotionHandler handler);
}
