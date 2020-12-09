package com.tmt.shop.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.ScorePage;
import com.tmt.shop.service.GoodsSearcherFacade;

/**
 * 商品预览
 * @author root
 */
@Controller
@RequestMapping(value = "${frontPath}/shop/search")
public class SearchController {

	@Autowired
	private GoodsSearcherFacade goodsSearcher;
	
	/**
	 * 商品查询页面数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/goods/data.json")
	public Page goods(String keywork, ScorePage page){
		return this.goodsSearcher.searchText(keywork, page);
	}
	
	/**
	 * 首页显示的商品
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/goods/index.json")
	public Page index_goods(ScorePage page) {
		return goodsSearcher.index_goods(page);
	}
	
	/**
	 * 分类显示的商品
	 * @param categoryId
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/goods/category.json")
	public Page category_goods(String categoryId, ScorePage page) {
		return goodsSearcher.searchByCategory(categoryId, page);
	}
}