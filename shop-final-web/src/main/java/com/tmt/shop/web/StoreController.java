package com.tmt.shop.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.core.config.Globals;
import com.tmt.core.entity.LabelVO;
import com.tmt.core.staticize.StaticUtils;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.Store;
import com.tmt.shop.service.StoreServiceFacade;
import com.tmt.shop.utils.StoreUtils;

/**
 * 店铺管理 管理
 * @author 超级管理员
 * @date 2017-01-10
 */
@Controller("shopStoreController")
@RequestMapping(value = "${adminPath}/shop/store")
public class StoreController extends BaseController{
	
	@Autowired
	private StoreServiceFacade storeService;
	
	/**
	 * 表单
	 * @param store
	 * @param model
	 */
	@RequestMapping("")
	public String form(Model model) {
		Store store = this.storeService.get(Store.DEFAULT_STORE);
		model.addAttribute("store", store);
		return "/shop/StoreForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Store store, Model model, RedirectAttributes redirectAttributes) {
		this.storeService.save(store);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改店铺", store.getName(), "成功"));
		redirectAttributes.addAttribute("id", store.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/store");
	}
	
	/**
	 * 可用的页面
	 * @return
	 */
	@ResponseBody
	@RequestMapping("pages")
	public List<LabelVO> pages() {
		Store store = StoreUtils.getDefaultStore();
		List<LabelVO> pages = Lists.newArrayList();
		pages.add(LabelVO.newLabel("首页", StaticUtils.staticDomain(store).append("/front/shop/store/index.html").toString()));
		pages.add(LabelVO.newLabel("搜索", StaticUtils.staticDomain(store).append("/front/shop/store/search.html").toString()));
		pages.add(LabelVO.newLabel("分类", StaticUtils.staticDomain(store).append("/front/shop/store/category.html").toString()));
		pages.add(LabelVO.newLabel("购物车", StaticUtils.staticDomain(store).append("/f/shop/cart/list.html").toString()));
		pages.add(LabelVO.newLabel("会员中心", StaticUtils.staticDomain(store).append("/f/member/index.html").toString()));
		pages.add(LabelVO.newLabel("社区", StaticUtils.staticDomain(store).append("/f/member/bbs/hotspot/index.html").toString()));
		return pages;
	}
}