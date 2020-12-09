package com.tmt.shop.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

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
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.staticize.StaticUtils;
import com.tmt.core.utils.JsonMapper;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.Brand;
import com.tmt.shop.entity.Category;
import com.tmt.shop.entity.Goods;
import com.tmt.shop.entity.GoodsAttribute;
import com.tmt.shop.entity.GoodsEvaluate;
import com.tmt.shop.entity.GoodsImage;
import com.tmt.shop.entity.GoodsLimit;
import com.tmt.shop.entity.GoodsParameterGroup;
import com.tmt.shop.entity.GoodsSpecification;
import com.tmt.shop.entity.GoodsTag;
import com.tmt.shop.entity.Product;
import com.tmt.shop.entity.Store;
import com.tmt.shop.service.BrandServiceFacade;
import com.tmt.shop.service.CategoryServiceFacade;
import com.tmt.shop.service.GoodsServiceFacade;
import com.tmt.shop.service.ProductServiceFacade;
import com.tmt.shop.utils.StoreUtils;
import com.tmt.system.utils.UserUtils;

/**
 * 商品管理 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Controller("shopGoodsController")
@RequestMapping(value = "${adminPath}/shop/goods")
public class GoodsController extends BaseController{
	
	@Autowired
	private GoodsServiceFacade goodsService;
	@Autowired
	private BrandServiceFacade brandService;
	@Autowired
	private ProductServiceFacade productService;
	@Autowired
	private CategoryServiceFacade categoryService;
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Goods goods, Model model){
		return "/shop/GoodsList";
	}
	
	/**
	 * 表格选择(不能使用 -- 页面显示时需要选择)
	 * @return
	 * @see com.tmt.shop.web.ProductController
	 */
	@RequestMapping("table_select")
	public String table_select() {
		return "/shop/GoodsTableSelect";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param goods
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Goods goods, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		this.initQc(goods, c);
		return goodsService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param goods
	 * @param model
	 */
	@RequestMapping("form/base")
	public String baseForm(Goods goods, Model model) {
	    goods = this.goodsService.initModify(goods.getId());
	    Brand brand = null;
	    if (goods != null && (brand = brandService.get(goods.getBrand())) != null) {
		    goods.setBrandName(brand.getName());   
	    }
	    // 图片
	    StringBuilder _images = new StringBuilder();
	    List<GoodsImage> images = goods.getImages();
	    for(GoodsImage image: images) {
	    	_images.append("|").append(image.getLarge());
	    }
	    
	    model.addAttribute("pictures", _images);
	    model.addAttribute("goods", goods);
		return "/shop/GoodsBaseForm";
	}
	
	/**
	 * 表单
	 * @param goods
	 * @param model
	 */
	@RequestMapping("form/evaluate")
	public String evaluateForm(Goods goods, Model model) {
		goods = this.goodsService.getWithEvaluate(goods.getId());
		GoodsEvaluate evaluate = goods.getEvaluate();
	    if (evaluate == null) {
	    	evaluate = new GoodsEvaluate();
	    	evaluate.setId(IdGen.INVALID_ID);
	    	evaluate.setIsEnabled(GoodsEvaluate.YES);
	    }
	    evaluate.setGoodsId(goods.getId());
	    model.addAttribute("goods", goods);
		model.addAttribute("evaluate", evaluate);
		return "/shop/GoodsEvaluateForm";
	}
	
	/**
	 * 表单
	 * @param goods
	 * @param model
	 */
	@RequestMapping("form/specification")
	public String specificationForm(Goods goods, Model model) {
		goods = this.goodsService.get(goods.getId());
	    goods.setSpecifications(this.goodsService.querySpecifications(goods.getId()));
	    goods.setProducts(this.productService.queryProductsByGoodsId(goods.getId()));
		model.addAttribute("goods", goods);
		return "/shop/GoodsSpecificationForm";
	}
	
	/**
	 * 删除
	 * @param idList
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete")
	public AjaxResult delete(Long[] idList , Model model, HttpServletResponse response) {
		List<Goods> goodss = Lists.newArrayList();
		for(Long id: idList) {
			Goods goods = new Goods();
			goods.setId(id);
			goodss.add(goods);
		}
		this.goodsService.delete(goodss);
		return AjaxResult.success();
	}
	
	/**
	 * 阅览地址
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("view/{id}")
	public AjaxResult view(@PathVariable Long id) {
		Goods goods = this.goodsService.getShowGoods(id);
		// 默认的商品
		Product defaultProduct = null;
		List<GoodsSpecification> specifications = goods.getSpecifications();
		List<Product> products = goods.getProducts();
		if(specifications != null && specifications.size() != 0 && products != null && products.size() != 0) {
		   for(Product product: products) {
			   if(Product.YES == product.getIsDefault()) {
				  defaultProduct =  product; 
				  break;
			   }
		   }
		} else if(products != null && products.size() != 0) {
		   defaultProduct = products.get(0);
		}
		
		// 生成静态页面
		Store store = StoreUtils.getDefaultStore();
		return AjaxResult.success(StaticUtils.touchStaticizePage(store, "product", defaultProduct));
	}
	
	/**
	 * 商品相关信息(简单信息)
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("get/{id}")
	public Goods get(@PathVariable Long id) {
		return this.goodsService.getShowGoods(id);
	}
	
	/**
	 * 取消限购
	 * @return
	 */
	@ResponseBody
	@RequestMapping("cancel_limit/{id}")
	public AjaxResult cancel_limit(@PathVariable Long id) {
		this.goodsService.cancelLimit(id);
		return AjaxResult.success();
	}
	
	/**
	 * 开启限购
	 * @return
	 */
	@ResponseBody
	@RequestMapping("open_limit/{id}")
	public AjaxResult cancel_limit(@PathVariable Long id, Byte num) {
		GoodsLimit limit = new GoodsLimit();
		limit.setGoodsId(id);
		limit.setBuyLimit(num);
		this.goodsService.openLimit(limit);
		return AjaxResult.success();
	}

	//查询条件
	private void initQc(Goods goods, Criteria c) {
		if(goods != null && StringUtils.isNotBlank(goods.getName())) {
		   c.andLike("NAME", goods.getName());
		}
		if(goods != null && StringUtils.isNotBlank(goods.getSn())) {
		   c.andEqualTo("SN", goods.getSn());
		}
		if(goods.getCategoryId() != null) {
           String sql = StringUtils.format("EXISTS(SELECT 1 FROM SHOP_CATEGORY TC WHERE ( CONCAT(CONCAT(',',TC.PARENT_IDS),',')  LIKE CONCAT(CONCAT('%,','%s'),',%') OR TC.ID = '%s') AND CONCAT(CONCAT(',',T.CATEGORY_ID),',') LIKE CONCAT(CONCAT('%,',TC.ID),',%'))", goods.getCategoryId(), goods.getCategoryId());
           c.andConditionSql(sql);
        }
		c.andEqualTo("DEL_FLAG", Goods.NO);
	}
	
	// 向导
	@RequestMapping("guide/init")
	public String guide() {
		return "/shop/GoodsGuideInit";
	}
	
	/**
	 * 初始化向导
	 * @param categoryId
	 * @param model
	 * @return
	 */
	@RequestMapping("guide")
	public String guide(Long categoryId, Model model) {
	    Category category = categoryService.get(categoryId);
		Goods goods = this.goodsService.initAdd(category);
		model.addAttribute("goods", goods);
		return "/shop/GoodsGuide";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("guide/save")
	public String guideSave(Goods goods, Model model, RedirectAttributes redirectAttributes) {
		//标签
		List<GoodsTag> tags = WebUtils.fetchItemsFromRequest(null, GoodsTag.class, "tags.");
		goods.setTags(tags);
		
		// 相册
		String pictures = WebUtils.getCleanParam("pictures");
		if (StringUtils.isNotBlank(pictures)) {
			String[] _pictures = pictures.split("\\|");
			List<GoodsImage> images = Lists.newArrayList();
			for(String image: _pictures) {
				if (StringUtils.isBlank(image)) {continue;}
				GoodsImage _image = new GoodsImage();
				_image.setLarge(image);
				images.add(_image);
			}
			goods.setImages(images);
		}
		
		// 商品属性
		List<GoodsAttribute> attributes = WebUtils.fetchItemsFromRequest(null, GoodsAttribute.class, "attributes.");
		goods.setAttributes(attributes);
		
		// 商品参数
		String postData = WebUtils.getCleanParam("parameterData");
		List<GoodsParameterGroup> groups = JsonMapper.fromJsonToList(postData, GoodsParameterGroup.class);
		goods.setParameterGroups(groups);
		
		// 商品规格
		postData = WebUtils.getCleanParam("specificationData");
		List<GoodsSpecification> specifications = JsonMapper.fromJsonToList(postData, GoodsSpecification.class);
		goods.setSpecifications(specifications);
		
		goods.userOptions(UserUtils.getUser());
		this.goodsService.add(goods);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改商品信息", goods.getName(), "成功"));
		redirectAttributes.addAttribute("id", goods.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/goods/form/specification");
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save/base")
	public String baseSave(Goods goods, Model model, RedirectAttributes redirectAttributes) {
		//标签
		List<GoodsTag> tags = WebUtils.fetchItemsFromRequest(null, GoodsTag.class, "tags.");
		goods.setTags(tags);
		
		// 相册
		String pictures = WebUtils.getCleanParam("pictures");
		if (StringUtils.isNotBlank(pictures)) {
			String[] _pictures = pictures.split("\\|");
			List<GoodsImage> images = Lists.newArrayList();
			for(String image: _pictures) {
				if (StringUtils.isBlank(image)) {continue;}
				GoodsImage _image = new GoodsImage();
				_image.setLarge(image);
				images.add(_image);
			}
			goods.setImages(images);
		}
		
		// 商品属性
		List<GoodsAttribute> attributes = WebUtils.fetchItemsFromRequest(null, GoodsAttribute.class, "attributes.");
		goods.setAttributes(attributes);
		
		// 商品参数
		String postData = WebUtils.getCleanParam("parameterData");
		List<GoodsParameterGroup> groups = JsonMapper.fromJsonToList(postData, GoodsParameterGroup.class);
		goods.setParameterGroups(groups);
		
		goods.userOptions(UserUtils.getUser());
		this.goodsService.modify(goods);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改商品信息", goods.getName(), "成功"));
		redirectAttributes.addAttribute("id", goods.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/goods/form/base");
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save/evaluate")
	public String evaluateSave(GoodsEvaluate evaluate, Model model, RedirectAttributes redirectAttributes) {
		this.goodsService.saveEvaluate(evaluate);
		addMessage(redirectAttributes, StringUtils.format("%s%s", "修改商品评价", "成功"));
		redirectAttributes.addAttribute("id", evaluate.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/goods/form/evaluate");
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save/specification")
	public String specificationSave(Goods goods, Model model, RedirectAttributes redirectAttributes) {
		goods.userOptions(UserUtils.getUser());
		this.goodsService.saveSpecification(goods);
		addMessage(redirectAttributes, StringUtils.format("%s%s", "修改商品详细信息", "成功"));
		redirectAttributes.addAttribute("id", goods.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/goods/form/specification");
	}
}