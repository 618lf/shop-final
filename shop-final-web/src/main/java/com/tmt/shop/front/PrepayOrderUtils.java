package com.tmt.shop.front;

import com.tmt.core.utils.CacheUtils;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.ShopConstant;

/**
 * 预支付的订单
 * 1. 下单之后如果是在线支付则生成欲支付订单， 存活 10 秒 只能获取一次
 * @author lifeng
 */
public class PrepayOrderUtils {

	/**
	 * 获取预支付的订单
	 * @param orderId
	 * @return
	 */
	public static Order get(Long orderId) {
		if (orderId == null) {return null;}
		return CacheUtils.getSysCache().get(orderId);
	}
	
	/**
	 * 存储预支付的订单
	 * @param order
	 */
	public static void put(Order order) {
		 String key = new StringBuilder(ShopConstant.SHOP_PRE_ORDER_CACHE).append(order.getId()).toString();
		 CacheUtils.getSysCache().put(key, order, 10);
	}
}