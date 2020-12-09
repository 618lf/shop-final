package com.tmt.shop.utils;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.tmt.core.utils.CacheUtils;
import com.tmt.core.utils.SpringContextHolder;
import com.tmt.shop.entity.Coupon;
import com.tmt.shop.entity.CouponProduct;
import com.tmt.shop.entity.ShopConstant;
import com.tmt.shop.service.CouponServiceFacade;

/**
 * 优惠券 -- 相关
 * @author root
 */
public class CouponUtils {

	/**
	 * 缓存优惠券
	 * @param couponId
	 * @return
	 */
	public static Coupon getCache(Long couponId) {
		String key = new StringBuilder(ShopConstant.COUPON_KEY).append(couponId).toString();
		Coupon coupon = CacheUtils.getSysCache().get(key);
		if (null == coupon){
			CouponServiceFacade couponService = SpringContextHolder.getBean(CouponServiceFacade.class);
			coupon = couponService.get(couponId);
			
			if (coupon == null) {return null;}
			
			List<CouponProduct> products = couponService.queryProductByCouponId(couponId);
			Set<Long> ps = Sets.newHashSet();
    		for(CouponProduct p: products) {
    			ps.add(p.getProducts());
    		}
    		// 装载支持的商品
    		coupon.setPs(ps);
    		
    		long min = ShopConstant.MAX_SECONDS_PROMOTIONS > coupon.getExpirySeconds()? coupon.getExpirySeconds(): ShopConstant.MAX_SECONDS_PROMOTIONS;
    		
    		CacheUtils.getSysCache().put(key, coupon, (int)min);
		}
		return coupon;
	}
	
	/**
	 * 清除缓存
	 * @param couponId
	 */
	public static void clearCache(Long couponId) {
		String key = new StringBuilder(ShopConstant.COUPON_KEY).append(couponId).toString();
		CacheUtils.getSysCache().delete(key);
	}
}
