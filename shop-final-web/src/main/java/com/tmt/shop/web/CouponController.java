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
import com.tmt.core.utils.JsonMapper;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.Coupon;
import com.tmt.shop.entity.CouponCode;
import com.tmt.shop.entity.CouponProduct;
import com.tmt.shop.service.CouponServiceFacade;
import com.tmt.system.entity.User;
import com.tmt.system.utils.UserUtils;

/**
 * 优惠券 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
@Controller("shopCouponController")
@RequestMapping(value = "${adminPath}/shop/coupon")
public class CouponController extends BaseController{
	
	@Autowired
	private CouponServiceFacade couponService;
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Coupon coupon, Model model){
		return "/shop/CouponList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param coupon
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Coupon coupon, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(coupon, c);
		return couponService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param coupon
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Coupon coupon, Model model) {
	    if(coupon != null && !IdGen.isInvalidId(coupon.getId())) {
		   coupon = this.couponService.get(coupon.getId());
		   List<CouponProduct> products = this.couponService.queryRichProductByCouponId(coupon.getId());
		   coupon.setProducts(products);
		} else {
		   if(coupon == null) {
			  coupon = new Coupon();
		   }
		   coupon.setIsEnabled(Coupon.YES);
		   coupon.setIsExchange(Coupon.YES);
		   coupon.setIsFission(Coupon.NO);
		   coupon.setIsPrice(Coupon.NO);
		   coupon.setExpireType((byte)0);
		   coupon.setGetno((byte)1);
		   coupon.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("coupon", coupon);
		return "/shop/CouponForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Coupon coupon, Model model, RedirectAttributes redirectAttributes) {
		String _products = WebUtils.getCleanParam("_products");
		List<CouponProduct> products = JsonMapper.fromJsonToList(_products, CouponProduct.class);
		coupon.setProducts(products);
		this.couponService.save(coupon);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改优惠券", coupon.getName(), "成功"));
		redirectAttributes.addAttribute("id", coupon.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/coupon/form");
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
		List<Coupon> coupons = Lists.newArrayList();
		for(Long id: idList) {
			Coupon coupon = new Coupon();
			coupon.setId(id);
			coupons.add(coupon);
		}
		this.couponService.delete(coupons);
		return AjaxResult.success();
	}
	
	//查询条件
	private void initQc(Coupon coupon, Criteria c) {
        if(StringUtils.isNotBlank(coupon.getName())) {
           c.andEqualTo("NAME", coupon.getName());
        }
        if(coupon.getBeginDate() != null) {
           c.andDateEqualTo("BEGIN_DATE", coupon.getBeginDate());
        }
        if(coupon.getVal() != null) {
           c.andEqualTo("VAL", coupon.getVal());
        }
	}
	
	/**
	 * 表组件支持
	 */
	@RequestMapping("table_select")
	public String tableSelect() {
	   return "/shop/CouponTableSelect";
	}
	
	/**
	 * 商品相关信息(简单信息)
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("get/{id}")
	public Coupon get(@PathVariable Long id) {
		Coupon coupon = this.couponService.get(id);
		return coupon;
	}
	
	/**
	 * 赠送优惠券
	 **/
	@ResponseBody
	@RequestMapping("/give")
	public AjaxResult give(Long userId, String code){
		User user = UserUtils.getUser(userId);
		CouponCode coupon = couponService.assignOneCode(user, code);
		if (coupon == null) {
			return AjaxResult.error("赠送失败");
		}
		return AjaxResult.success(coupon.getVal());
	}
}
