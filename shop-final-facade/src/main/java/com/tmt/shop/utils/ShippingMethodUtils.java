package com.tmt.shop.utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.tmt.common.utils.CacheUtils;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.Maps;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.common.utils.StringUtil3;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.PaymentMethod;
import com.tmt.shop.entity.ShippingMethod;
import com.tmt.shop.entity.ShippingMethodArea;
import com.tmt.shop.entity.ShopConstant;
import com.tmt.shop.service.ShippingMethodServiceFacade;

/**
 * 配送方式支持
 * @author root
 */
public class ShippingMethodUtils {

	/**
	 * 得到指定的支付方式
	 * @param id
	 * @return
	 */
	public static ShippingMethod getShippingMethod(Long id) {
		String key = new StringBuilder(ShopConstant.SHOP_SHIPPING_METHODS).append(id).toString();
		ShippingMethod shippingMethod = CacheUtils.getSysCache().get(key);
		if (shippingMethod == null) {
			ShippingMethodServiceFacade methodService = SpringContextHolder.getBean(ShippingMethodServiceFacade.class);
			shippingMethod = methodService.getWithAreas(id);
			if (shippingMethod != null) {
				
				// 转为Map
				if (!Lists.isEmpty(shippingMethod.getAreas())) {
					Map<String, ShippingMethodArea> mareas = Maps.newHashMap();
					List<ShippingMethodArea> areas = shippingMethod.getAreas();
					for(ShippingMethodArea area : areas) {
						mareas.put(area.getAreaIds(), area);
					}
					shippingMethod.setMareas(mareas);
					shippingMethod.setAreas(null);
				}
				
				CacheUtils.getSysCache().put(key, shippingMethod);
			}
		}
		return shippingMethod;
	}
	
	/**
	 * 得到支付方式
	 * @return
	 */
	public static List<ShippingMethod> getShippingMethods() {
		String key = new StringBuilder(ShopConstant.SHOP_SHIPPING_METHODS).append("ALL").toString();
		List<ShippingMethod> methods = CacheUtils.getSysCache().get(key);
		if (methods == null) {
			ShippingMethodServiceFacade methodService = SpringContextHolder.getBean(ShippingMethodServiceFacade.class);
			methods = methodService.queryShippingMethods();
			if (methods != null) {
				CacheUtils.getSysCache().put(key, methods);
			}
		}
		return methods;
	}
	
	/**
	 * 清除缓存
	 */
	public static void clearCache() {
		String key = new StringBuilder(ShopConstant.SHOP_SHIPPING_METHODS).append("*").toString();
		CacheUtils.getSysCache().delete(key);
	}
	
	/**
	 * 动态设置配送方式（根据选择的支付方式）
	 * @param paymentMethod
	 * @return
	 */
	public static ShippingMethod getDefaultShippingMethod(PaymentMethod paymentMethod) {
		List<ShippingMethod> methods = getShippingMethods();
		for(ShippingMethod method: methods) {
			if (method.isSupport(paymentMethod)) {
			    return method;
			}
		}
		return null;
	}
	
	/**
	 * 计算运费
	 * @param weight
	 * @return
	 */
	public static BigDecimal calculateFreight(Order order) {
		
		// 获取具体的送货方案(这里获取的是有区域设置的)
		ShippingMethod smethod = null;
		if (order.getShippingMethod() == null || (smethod = ShippingMethodUtils.getShippingMethod(order.getShippingMethod())) == null) {
			return BigDecimal.ZERO;
		}
		
		// 支持按照区域的方式
		String areaId = order.getAreaId(); Double weight = order.getProductWeight();
		Map<String, ShippingMethodArea> mareas = smethod.getMareas();
		
		// 无区域设置
		if (StringUtil3.isBlank(areaId)
				|| mareas == null || mareas.isEmpty()) {
			return smethod.calcula(weight);
		}
		
		// 有区域设置 (最多四次)
		for(int i = 0; i<4; i++) {
			
			// 第一次不需要
			if (i != 0) {
				areaId = StringUtil3.substringBeforeLast(areaId, "/");
			}
			
			// 找到这个地区的配置
			if (mareas.containsKey(areaId)) {
				return mareas.get(areaId).calcula(weight);
			}
			
			// 如果没有“/” 不需要再往下找了
			if (!StringUtil3.contains(areaId, "/")) {
				break;
			}
		}
		
		// 默认使用这个
		return smethod.calcula(weight);
	}
}