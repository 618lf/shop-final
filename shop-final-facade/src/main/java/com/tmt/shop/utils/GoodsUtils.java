package com.tmt.shop.utils;

import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.CollectionUtils;

import com.tmt.core.utils.CacheUtils;
import com.tmt.core.utils.SpringContextHolder;
import com.tmt.core.utils.StringUtils;
import com.tmt.shop.entity.Goods;
import com.tmt.shop.entity.Product;
import com.tmt.shop.entity.ShopConstant;
import com.tmt.shop.service.GoodsServiceFacade;

public class GoodsUtils {

	/**
	 * 获得
	 * @param id
	 * @return
	 */
	public static Goods get(Long id) {
		String key = new StringBuilder(ShopConstant.GOODS).append(id).toString();
		Goods product = CacheUtils.get(key);
		if (product == null) {
			GoodsServiceFacade productService = SpringContextHolder.getBean(GoodsServiceFacade.class);
			product = productService.getShowGoods(id); 
			if (product != null) {
				CacheUtils.put(key, product);
			}
		}
		return product;
	}
	
	/**
	 * 缓存
	 * @param product
	 */
	public static void cached(Goods goods) {
		String key = new StringBuilder(ShopConstant.GOODS).append(goods.getId()).toString();
		CacheUtils.put(key, goods);
	}
	
	/**
	 * 删除缓存
	 * @param product
	 */
	public static void clear(Goods goods) {
		String key = new StringBuilder(ShopConstant.GOODS).append(goods.getId()).toString();
		Goods cached = CacheUtils.get(key);
		if (cached != null) {
			List<Product> products = cached.getProducts();
			if (!CollectionUtils.isEmpty(products)) {
				for(Product product: products) {
					ProductUtils.clear(product);
				}
			}
		}
		CacheUtils.evict(key);
	}
	
	/**
	 * 图片异步处理
	 * @param goods
	 */
	public static void imagesAsync(Goods goods) {
		String gif = StringUtils.format("/static%s", "/img/loading.gif");
		String content = goods.getIntroduction();
		if (StringUtils.isNoneBlank(content)) {
			Document _doc = Jsoup.parse(content);
			Elements imgs =_doc.getElementsByTag("img");
			Iterator<Element>  it = imgs.iterator();
			while(it.hasNext()) {
				Element element = it.next();
				String src = element.attr("src");
				element.attr("src", gif);
				element.attr("data-url", src);
			}
			// 重新设置图片
			content = _doc.html();
			goods.setIntroduction(content);
		}
	}
}
