package com.tmt.shop.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;
import com.tmt.shop.entity.Store;
import com.tmt.shop.service.CategoryServiceFacade;
import com.tmt.shop.utils.StoreUtils;

/**
 * 店铺
 * @author lifeng
 */
@Controller("frontStoreController")
@RequestMapping(value = "${frontPath}/shop/store")
public class StoreController {

	@Autowired
	private CategoryServiceFacade categoryService;
	
	/**
	 * 首页
	 * @return
	 */
	@RequestMapping("/index.html")
	public String index(Model model) {
		Store store = StoreUtils.getDefaultStore();
		if (store == null || StringUtils.isBlank(store.getStoreIndex())) {
			model.addAttribute("categorys", categoryService.queryUseAbleTopCategorys());
			return "/front/Index";
		}
		return WebUtils.redirectTo(store.getStoreIndex());
	}
	
	/**
	 * 搜索
	 * @return
	 */
	@RequestMapping("/search.html")
	public String search() {
		return "/front/goods/GoodsSearch"; 
	}
	
	/**
	 * 分类
	 * @return
	 */
	@RequestMapping("/category.html")
	public String category(Model model) {
		model.addAttribute("categorys", categoryService.queryUseAbleTopCategorys());
		return "/front/goods/CategoryView"; 
	}
}