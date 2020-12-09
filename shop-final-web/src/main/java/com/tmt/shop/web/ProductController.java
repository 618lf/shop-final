package com.tmt.shop.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.core.config.Globals;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.staticize.StaticUtils;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.Goods;
import com.tmt.shop.entity.Product;
import com.tmt.shop.entity.Store;
import com.tmt.shop.service.GoodsServiceFacade;
import com.tmt.shop.service.ProductServiceFacade;
import com.tmt.shop.utils.StoreUtils;

/**
 * 商品库存 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Controller("shopProductController")
@RequestMapping(value = "${adminPath}/shop/product")
public class ProductController extends BaseController{
	
	@Autowired
	private ProductServiceFacade productService;
	@Autowired
	private GoodsServiceFacade goodsService;
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Product product, Model model){
		return "/shop/ProductList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param product
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Product product, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(product, c);
		return productService.queryForPage(qc, param);
	}
	
	/**
	 * 基本信息修改(可以单独修改基本信息)
	 * @param goods
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Product product, Model model) {
		product = productService.get(product.getId());
		model.addAttribute("product", product);
		return "/shop/ProductForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Product product, Model model, RedirectAttributes redirectAttributes) {
		this.productService.save(product);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改货品信息", product.getName(), "成功"));
		redirectAttributes.addAttribute("id", product.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/product/form");
	}
	
	/**
	 * 预览一个库存商品
	 * @return
	 */
	@RequestMapping("view/{id}")
	public String view(@PathVariable Long id, Model model){
		Product product = productService.get(id);
		Goods goods = goodsService.get(product.getGoodsId());
		model.addAttribute("product", product);
		model.addAttribute("goods", goods);
		return "/shop/ProductView";
	}
	
	/**
	 * 前台预览
	 * @return
	 */
	@ResponseBody
	@RequestMapping("f_view/{id}")
	public AjaxResult f_view(@PathVariable Long id, Model model){
		Store store = StoreUtils.getDefaultStore();
		Product product = productService.get(id);
		Goods goods = this.goodsService.getShowGoods(product.getGoodsId());
		product.setGoods(goods);
		return AjaxResult.success(StaticUtils.touchStaticizePage(store, "product", product));
	} 
	
	/**
	 * 入库
	 * @return
	 */
	@ResponseBody
	@RequestMapping("store/in")
	public AjaxResult inStore(Product product) {
		Product _product = this.productService.get(product.getId());
		if(_product != null) {
		   _product.setStore(product.getStore());
		   this.productService.inStroe(_product);
		   _product = this.productService.get(product.getId());
		   return AjaxResult.success(_product);
		}
		return AjaxResult.error("商品不存在");
	}
	
	/**
	 * 出库
	 * @return
	 */
	@ResponseBody
	@RequestMapping("store/out")
	public AjaxResult outStore(Product product) {
		Product _product = this.productService.get(product.getId());
		if(_product != null && Integer.compare(product.getStore(), _product.getStore()) <= 0
				&& Integer.compare(Ints.subI(_product.getStore(), product.getStore()), _product.getFreezeStore()) >=0 ) {
		   _product.setStore(product.getStore());
		   this.productService.outStroe(_product);
		   _product = this.productService.get(product.getId());
		   return AjaxResult.success(_product);
		}
		return AjaxResult.error("商品不存在或出库数不能大于库存数，并保证库存数大于冻结商品");
	}
	
	//查询条件
	private void initQc(Product product, Criteria c) {
        if(StringUtils.isNotBlank(product.getSn())) {
           c.andLike("SN", product.getSn());
        }
        if(StringUtils.isNotBlank(product.getName())) {
           c.andLike("NAME", product.getName());
        }
        // 可销售的(删除商品会将货品设置为不可售)
        c.andEqualTo("IS_SALESTATE", Product.YES);
	}
	
	/**
	 * 可选择的商品
	 * @return
	 */
	@RequestMapping("table_select")
	public String table_select() {
		return "/shop/ProductTableSelect";
	}
	
	/**
	 * 商品相关信息(简单信息)
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("get/{id}")
	public Product get(@PathVariable Long id) {
		Store store = StoreUtils.getDefaultStore();
		Product product = productService.get(id);
		Goods goods = this.goodsService.getShowGoods(product.getGoodsId());
		product.setGoods(goods);
		product.setUrl(StaticUtils.touchStaticizePage(store, "product", product));
		return product;
	}
}