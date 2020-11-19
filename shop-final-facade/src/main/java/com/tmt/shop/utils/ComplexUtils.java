package com.tmt.shop.utils;

import java.util.List;
import java.util.Map;

import com.tmt.common.utils.CacheUtils;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.Maps;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.shop.entity.Complex;
import com.tmt.shop.entity.ComplexProduct;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.ShopConstant;
import com.tmt.shop.service.ComplexServiceFacade;

/**
 * 商品组合
 * @author lifeng
 */
public class ComplexUtils {

	/**
	 * 商品相关的组合
	 * @param productId
	 * @return
	 */
	public static Map<Byte, List<Long>> queryProductEnabledComplexs(Long productId) {
		String key = new StringBuilder(ShopConstant.GOODS_COMPLEXS).append(productId).toString();
		Map<Byte, List<Long>> complexs = CacheUtils.getSysCache().get(key);
		if (complexs == null) {
			complexs = Maps.newHashMap();
			ComplexServiceFacade complexService = SpringContextHolder.getBean(ComplexServiceFacade.class);
			List<Long> _complexs = complexService.queryProductComplexs(productId);
			for(Long id: _complexs) {
				Complex _complex = getComplex(id);
				if (_complex == null) { continue;}
				
				List<Long> discounts = complexs.get(_complex.getType());
				if (discounts == null) {
					discounts = Lists.newArrayList();
					complexs.put(_complex.getType(), discounts);
				}
				discounts.add(id);
			}
			
			// 放入缓存
			CacheUtils.getSysCache().put(key, complexs);
		}
		return complexs;
	}
	
	/**
	 * 缓存组合
	 * @param id
	 * @return
	 */
	public static Complex getComplex(Long id) {
		String key = new StringBuilder(ShopConstant.SHOP_COMPLEXS).append(id).toString();
		Complex complex = CacheUtils.getSysCache().get(key);
		if (complex == null) {
			ComplexServiceFacade complexService = SpringContextHolder.getBean(ComplexServiceFacade.class);
			complex = complexService.get(id);
			if (complex != null) {
				List<ComplexProduct> products = complexService.queryRichProductsByComplexId(complex.getId());
				complex.setProducts(products);
				CacheUtils.getSysCache().put(key, complex);
			}
		}
		return complex;
	}
	
	/**
	 * 清除缓存
	 * @param complexId
	 */
	public static void clearComplex(Long complexId) {
		String key = new StringBuilder(ShopConstant.SHOP_COMPLEXS).append(complexId).toString();
		CacheUtils.getSysCache().evict(key);
	}
	
	/**
	 * 清除缓存
	 * @param complexId
	 */
	public static void clearProduct(Long product) {
		String key = new StringBuilder(ShopConstant.GOODS_COMPLEXS).append(product).toString();
		CacheUtils.getSysCache().evict(key);
	}
	
	/**
	 * 套装优惠
	 * @param order
	 * @param user
	 * @return
	 */
	public static Order products(Order order) {
		List<OrderItem> copys = Lists.newArrayList();
		List<OrderItem> items = order.getItems();
		for(OrderItem item: items) {
			if (item.getGoodsId() == null) {
				Complex complex = ComplexUtils.getComplex(item.getProductId());
				copys.addAll(OrderItem.builds(item, complex));
			} else {
				copys.add(item);
			}
		}
		order.setItems(copys);
		return order;
	}
}