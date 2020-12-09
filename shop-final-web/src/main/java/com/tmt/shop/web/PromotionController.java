package com.tmt.shop.web;

import java.util.List;

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
import com.tmt.shop.entity.Promotion;
import com.tmt.shop.entity.PromotionCoupon;
import com.tmt.shop.entity.PromotionExt;
import com.tmt.shop.entity.PromotionProduct;
import com.tmt.shop.entity.Store;
import com.tmt.shop.service.PromotionServiceFacade;
import com.tmt.shop.utils.StoreUtils;

/**
 * 促销 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
@Controller("shopPromotionController")
@RequestMapping(value = "${adminPath}/shop/promotion")
public class PromotionController extends BaseController{
	
	@Autowired
	private PromotionServiceFacade promotionService;
	
	/**
	 * 添加前导页
	 * @return
	 */
	@RequestMapping("add")
	public String add() {
		return "/shop/PromotionAdd";
	}
	
	/**
	 * 表单直降
	 * @param promotion
	 * @param model
	 */
	@RequestMapping("form_zj")
	public String form_zj(Promotion promotion, Model model) {
	    if(promotion != null && !IdGen.isInvalidId(promotion.getId())) {
		   promotion = this.promotionService.getWithProduct(promotion.getId());
		} else {
		   if(promotion == null) {
			  promotion = new Promotion();
		   }
		   promotion.setIsEnabled(Promotion.YES);
		   promotion.setIsCouponAllowed(Promotion.NO);
		   promotion.setType(Promotion.ZJ);
		   promotion.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("promotion", promotion);
		return "/shop/PromotionZjForm";
	}
	
	/**
	 * 表单满减
	 * @param promotion
	 * @param model
	 */
	@RequestMapping("form_mj")
	public String form_mj(Promotion promotion, Model model) {
	    if(promotion != null && !IdGen.isInvalidId(promotion.getId())) {
		   promotion = this.promotionService.getWithProduct(promotion.getId());
		} else {
		   if(promotion == null) {
			  promotion = new Promotion();
		   }
		   promotion.setIsPrice(Promotion.YES);
		   promotion.setIsEnabled(Promotion.YES);
		   promotion.setIsCouponAllowed(Promotion.NO);
		   promotion.setType(Promotion.MJ);
		   promotion.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("promotion", promotion);
		return "/shop/PromotionMjForm";
	}
	
	/**
	 * 表单满折
	 * @param promotion
	 * @param model
	 */
	@RequestMapping("form_mz")
	public String form_mz(Promotion promotion, Model model) {
	    if(promotion != null && !IdGen.isInvalidId(promotion.getId())) {
		   promotion = this.promotionService.getWithProduct(promotion.getId());
		} else {
		   if(promotion == null) {
			  promotion = new Promotion();
		   }
		   promotion.setIsPrice(Promotion.YES);
		   promotion.setIsEnabled(Promotion.YES);
		   promotion.setIsCouponAllowed(Promotion.NO);
		   promotion.setType(Promotion.MZ);
		   promotion.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("promotion", promotion);
		return "/shop/PromotionMzForm";
	}
	
	/**
	 * 表单折扣
	 * @param promotion
	 * @param model
	 */
	@RequestMapping("form_zk")
	public String form_zk(Promotion promotion, Model model) {
	    if(promotion != null && !IdGen.isInvalidId(promotion.getId())) {
		   promotion = this.promotionService.getWithProduct(promotion.getId());
		} else {
		   if(promotion == null) {
			  promotion = new Promotion();
		   }
		   promotion.setIsEnabled(Promotion.YES);
		   promotion.setIsCouponAllowed(Promotion.NO);
		   promotion.setType(Promotion.ZK);
		   promotion.setId(IdGen.INVALID_ID);
		}
	    promotion.setType(Promotion.ZK);
		model.addAttribute("promotion", promotion);
		return "/shop/PromotionZkForm";
	}
	
	/**
	 * 表单包邮
	 * @param promotion
	 * @param model
	 */
	@RequestMapping("form_by")
	public String form_by(Promotion promotion, Model model) {
	    if(promotion != null && !IdGen.isInvalidId(promotion.getId())) {
		   promotion = this.promotionService.getWithProduct(promotion.getId());
		} else {
		   if(promotion == null) {
			  promotion = new Promotion();
		   }
		   promotion.setIsPrice(Promotion.YES);
		   promotion.setIsEnabled(Promotion.YES);
		   promotion.setIsCouponAllowed(Promotion.NO);
		   promotion.setType(Promotion.BY);
		   promotion.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("promotion", promotion);
		return "/shop/PromotionByForm";
	}
	
	/**
	 * 表单团购
	 * @param promotion
	 * @param model
	 */
	@RequestMapping("form_tg")
	public String form_tg(Promotion promotion, Model model) {
	    if(promotion != null && !IdGen.isInvalidId(promotion.getId())) {
		   promotion = this.promotionService.getWithProduct(promotion.getId());
		} else {
		   if(promotion == null) {
			  promotion = new Promotion();
		   }
		   promotion.setIsEnabled(Promotion.YES);
		   promotion.setIsCouponAllowed(Promotion.NO);
		   promotion.setType(Promotion.TG);
		   promotion.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("promotion", promotion);
		return "/shop/PromotionTgForm";
	}
	
	/**
	 * 表单抢购
	 * @param promotion
	 * @param model
	 */
	@RequestMapping("form_qg")
	public String form_qg(Promotion promotion, Model model) {
	    if(promotion != null && !IdGen.isInvalidId(promotion.getId())) {
		   promotion = this.promotionService.getWithProduct(promotion.getId());
		} else {
		   if(promotion == null) {
			  promotion = new Promotion();
		   }
		   promotion.setIsEnabled(Promotion.YES);
		   promotion.setIsCouponAllowed(Promotion.NO);
		   promotion.setType(Promotion.QG);
		   promotion.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("promotion", promotion);
		return "/shop/PromotionQgForm";
	}
	
	/**
	 * 表单满赠
	 * @param promotion
	 * @param model
	 */
	@RequestMapping("form_mzs")
	public String form_mzs(Promotion promotion, Model model) {
	    if(promotion != null && !IdGen.isInvalidId(promotion.getId())) {
		   promotion = this.promotionService.getWithProduct(promotion.getId());
		} else {
		   if(promotion == null) {
			  promotion = new Promotion();
		   }
		   promotion.setIsPrice(Promotion.YES);
		   promotion.setIsEnabled(Promotion.YES);
		   promotion.setIsCouponAllowed(Promotion.NO);
		   promotion.setType(Promotion.MZS);
		   promotion.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("promotion", promotion);
		return "/shop/PromotionMzsForm";
	}
	
	/**
	 * 新人礼包
	 * @param promotion
	 * @param model
	 */
	@RequestMapping("form_xr")
	public String form_xr(Promotion promotion, Model model) {
		promotion = this.promotionService.getWithProduct(Promotion.XR_ID);
		model.addAttribute("promotion", promotion);
		return "/shop/PromotionXrForm";
	}
	
	/**
	 * 邀请有礼
	 * @param promotion
	 * @param model
	 */
	@RequestMapping("form_yq")
	public String form_yq(Promotion promotion, Model model) {
		promotion = this.promotionService.getWithProduct(Promotion.YQ_ID);
		model.addAttribute("promotion", promotion);
		return "/shop/PromotionYqForm";
	}
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Promotion promotion, Model model){
		return "/shop/PromotionList";
	}
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("table_select")
	public String tableSelect(Promotion promotion, Model model){
		return "/shop/PromotionTableSelect";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param promotion
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Promotion promotion, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(promotion, c);
		qc.setOrderByClause("BEGIN_DATE DESC");
		return promotionService.queryForPage(qc, param);
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Promotion promotion, Model model, RedirectAttributes redirectAttributes) {
		String _products = WebUtils.getCleanParam("_products");
		
		// 促销商品
		List<PromotionProduct> products = JsonMapper.fromJsonToList(_products, PromotionProduct.class);
		promotion.setProducts(products);
		
		// 促销设置
		String _multis = WebUtils.getCleanParam("_multis");
		if (StringUtils.isNotBlank(_multis)) {
			PromotionExt ext = JsonMapper.fromJson(_multis, PromotionExt.class);
			promotion.initExt(ext);
		}
		
		// 赠送设置 
		String _coupons = WebUtils.getCleanParam("_coupons");
		if (StringUtils.isNotBlank(_coupons)) {
			List<PromotionCoupon> coupons = JsonMapper.fromJsonToList(_coupons, PromotionCoupon.class);
			promotion.setCoupons(coupons);
		}
		
		// 修正(如果前端没设置这个参数，默认使用金额验证)
		if (promotion.getIsPrice() == null) {
			promotion.setIsPrice(Promotion.YES);
			promotion.setIsQuantity(Promotion.NO);
		}
		
		// 满X类型
		if (promotion.getIsPrice() != null) {
			promotion.setIsQuantity((byte)(promotion.getIsPrice()^1));
		}
		this.promotionService.save(promotion);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改促销", promotion.getName(), "成功"));
		return this.form(promotion, redirectAttributes);
	}
	
	/**
	 * 重定向到
	 * @param promotion
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Promotion promotion, RedirectAttributes redirectAttributes) {
		promotion = this.promotionService.get(promotion.getId());
		redirectAttributes.addAttribute("id", promotion.getId());
		if (promotion.getType() == Promotion.ZJ) {
			return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/promotion/form_zj");
		} else if (promotion.getType() == Promotion.MJ) {
			return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/promotion/form_mj");
		} else if (promotion.getType() == Promotion.MZ) {
			return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/promotion/form_mz");
		} else if (promotion.getType() == Promotion.ZK) {
			return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/promotion/form_zk");
		} else if (promotion.getType() == Promotion.BY) {
			return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/promotion/form_by");
		} else if (promotion.getType() == Promotion.TG) {
			return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/promotion/form_tg");
		} else if (promotion.getType() == Promotion.QG) {
			return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/promotion/form_qg");
		} else if (promotion.getType() == Promotion.XR) {
			return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/promotion/form_xr");
		} else if (promotion.getType() == Promotion.YQ) {
			return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/promotion/form_yq");
		} else if (promotion.getType() == Promotion.MZS) {
			return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/promotion/form_mzs");
		}
		return "/shop/PromotionAdd";
	}
	
	/**
	 * 重定向到
	 * @param promotion
	 * @param model
	 */
	@ResponseBody
	@RequestMapping("get/{id}")
	public Promotion get(@PathVariable Long id) {
		Store store = StoreUtils.getDefaultStore();
		Promotion promotion = this.promotionService.get(id);
		promotion.setUrl(StaticUtils.touchStaticizePage(store, "promotion", promotion));
		return promotion;
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
	public AjaxResult delete(Long[] idList) {
		List<Promotion> promotions = Lists.newArrayList();
		for(Long id: idList) {
			Promotion promotion = new Promotion();
			promotion.setId(id);
			promotions.add(promotion);
			if (id == Promotion.XR_ID || id == Promotion.YQ_ID) {
				return AjaxResult.error("新人礼包和邀请有礼不能删除");
			}
		}
		this.promotionService.delete(promotions);
		return AjaxResult.success();
	}

	//查询条件
	private void initQc(Promotion promotion, Criteria c) {
        if(StringUtils.isNotBlank(promotion.getName())) {
           c.andEqualTo("NAME", promotion.getName());
        }
	}
}