package com.tmt.shop.utils;

import java.util.List;

import com.tmt.common.utils.CacheUtils;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.shop.entity.DeliveryCenter;
import com.tmt.shop.entity.ShopConstant;
import com.tmt.shop.service.DeliveryCenterServiceFacade;

/**
 * 送货中心
 * @author lifeng
 *
 */
public class DeliveryCenterUtils {

	/**
	 * 获得默认的送货中心
	 * @return
	 */
	public static DeliveryCenter getDefaultDeliveryCenter() {
		String key = new StringBuilder(ShopConstant.DELIVERY_CENTER_KEY).append("DEF").toString();
		DeliveryCenter deliveryCenter = CacheUtils.getSysCache().get(key);
		if (deliveryCenter == null) {
			List<DeliveryCenter> methods = getDeliveryCenters();
			for(DeliveryCenter method: methods) {
				if (method.getIsDefault() == 1) {
					deliveryCenter = method; break;
				}
			}
			if (deliveryCenter == null && methods.size() != 0) {
				deliveryCenter = methods.get(0);
			}
			if (deliveryCenter != null) {
				CacheUtils.getSysCache().put(key, deliveryCenter);
			}
		}
		return deliveryCenter;
	}
	
	/**
	 * 得到送货中心
	 * @return
	 */
	public static List<DeliveryCenter> getDeliveryCenters() {
		String key = new StringBuilder(ShopConstant.DELIVERY_CENTER_KEY).append("ALL").toString();
		List<DeliveryCenter> deliverys = CacheUtils.getSysCache().get(key);
		if (deliverys == null) {
			DeliveryCenterServiceFacade methodService = SpringContextHolder.getBean(DeliveryCenterServiceFacade.class);
			deliverys = methodService.queryDeliveryCenters();
			if (deliverys != null) {
				CacheUtils.getSysCache().put(key, deliverys);
			}
		}
		return deliverys;
	}
	
	/**
	 * 清楚缓存
	 */
	public static void clearCache() {
		String key = new StringBuilder(ShopConstant.DELIVERY_CENTER_KEY).append("*").toString();
		CacheUtils.getSysCache().delete(key);
	}
}