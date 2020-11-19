package com.tmt.shop.utils;

import com.tmt.common.utils.CacheUtils;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.shop.entity.Goods;
import com.tmt.shop.entity.Product;
import com.tmt.shop.entity.ShopConstant;
import com.tmt.shop.service.ProductServiceFacade;

/**
 * 商品缓存
 * @author lifeng
 */
public class ProductUtils {

	/**
	 * 获得商品。不一定是最新的，有一定的延迟，如果需要最新的数据，直接查询数据库
	 * @param id
	 * @return
	 */
	public static Product getProduct(Long id) {
		String key = new StringBuilder(ShopConstant.PRODUCTS).append(id).toString();
		Product product = CacheUtils.get(key);
		if (product == null) {
			ProductServiceFacade productService = SpringContextHolder.getBean(ProductServiceFacade.class);
			product = productService.get(id); 
			if (product != null) {
				Goods goods = GoodsUtils.get(product.getGoodsId());
				product.setIsMarketable(goods.getIsMarketable());
				CacheUtils.put(key, product);
			}
		}
		return product;
	}
	
	/**
	 * 删除缓存
	 * @param product
	 */
	public static void clear(Product product) {
		String key = new StringBuilder(ShopConstant.PRODUCTS).append(product.getId()).toString();
		CacheUtils.evict(key);
	}
}