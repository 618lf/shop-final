package com.tmt.shop.utils;

import java.util.List;

import com.tmt.core.utils.CacheUtils;
import com.tmt.core.utils.SpringContextHolder;
import com.tmt.shop.entity.PaymentMethod;
import com.tmt.shop.entity.ShopConstant;
import com.tmt.shop.service.PaymentMethodServiceFacade;

public class PaymentMethodUtils {

	/**
	 * 排在第一位的是默认的支付方式(后台可以设置顺序) 得到默认的支付方式 默认使用在线支付（先付款在发货）
	 * 
	 * @return
	 */
	public static PaymentMethod getDefaultPaymentMethod() {
		String key = new StringBuilder(ShopConstant.SHOP_PAYMENT_METHODS).append("DEF").toString();
		PaymentMethod paymentMethod = CacheUtils.getSysCache().get(key);
		if (paymentMethod == null) {
			List<PaymentMethod> methods = getPaymentMethods();
//			for(PaymentMethod method: methods) {
//				if (method.getMethod() == PaymentMethod.Method.ON_LINE && method.getType() == PaymentMethod.Type.BEFORE_PAYED) {
//				    paymentMethod = method; break;
//				}
//			}
			paymentMethod = methods.get(0);
			if (paymentMethod != null) {
				CacheUtils.getSysCache().put(key, paymentMethod);
			}
		}
		return paymentMethod;
	}

	/**
	 * 得到指定的支付方式
	 * 
	 * @param id
	 * @return
	 */
	public static PaymentMethod getPaymentMethod(Long id) {
		String key = new StringBuilder(ShopConstant.SHOP_PAYMENT_METHODS).append(id).toString();
		PaymentMethod paymentMethod = CacheUtils.getSysCache().get(key);
		if (paymentMethod == null) {
			PaymentMethodServiceFacade methodService = SpringContextHolder.getBean(PaymentMethodServiceFacade.class);
			paymentMethod = methodService.get(id);
			if (paymentMethod != null) {
				CacheUtils.getSysCache().put(key, paymentMethod);
			}
		}
		return paymentMethod;
	}

	/**
	 * 得到支付方式
	 * 
	 * @return
	 */
	public static List<PaymentMethod> getPaymentMethods() {
		String key = new StringBuilder(ShopConstant.SHOP_PAYMENT_METHODS).append("ALL").toString();
		List<PaymentMethod> methods = CacheUtils.getSysCache().get(key);
		if (methods == null) {
			PaymentMethodServiceFacade methodService = SpringContextHolder.getBean(PaymentMethodServiceFacade.class);
			methods = methodService.queryPaymentMethods();
			if (methods != null) {
				CacheUtils.getSysCache().put(key, methods);
			}
		}
		return methods;
	}

	/**
	 * 清楚缓存
	 */
	public static void clearCache() {
		String key = new StringBuilder(ShopConstant.SHOP_PAYMENT_METHODS).append("*").toString();
		CacheUtils.getSysCache().delete(key);
		ShippingMethodUtils.clearCache();
	}
}
