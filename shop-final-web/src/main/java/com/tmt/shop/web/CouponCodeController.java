package com.tmt.shop.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.Coupon;
import com.tmt.shop.entity.CouponCode;
import com.tmt.shop.service.CouponCodeServiceFacade;

/**
 * 优惠码 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
@Controller("shopCouponCodeController")
@RequestMapping(value = "${adminPath}/shop/coupon/code")
public class CouponCodeController extends BaseController{
	
	@Autowired
	private CouponCodeServiceFacade couponCodeService;
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(CouponCode couponCode, Model model){
		Coupon coupon = new Coupon();
		coupon.setId(couponCode.getCoupon());
		model.addAttribute("coupon", coupon);
		return "/shop/CouponCodeList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param couponCode
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(CouponCode couponCode, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(couponCode, c);
		return couponCodeService.queryForPage(qc, param);
	}
	
	//查询条件
	private void initQc(CouponCode couponCode, Criteria c) {
        if(couponCode.getIsEnabled() != null) {
           c.andEqualTo("IS_ENABLED", couponCode.getIsEnabled());
        }
        if(couponCode.getIsUsed() != null) {
           c.andEqualTo("IS_USED", couponCode.getIsUsed());
        }
        if(StringUtils.isNotBlank(couponCode.getUserName())) {
           c.andEqualTo("USER_NAME", couponCode.getUserName());
        }
        if(couponCode.getBeginDate() != null) {
           c.andDateEqualTo("BEGIN_DATE", couponCode.getBeginDate());
        }
        if (couponCode.getQtype() != null && couponCode.getQtype() == 1) {
        	c.andIsNull("USER_ID");
        	c.andEqualTo("IS_USED", Coupon.NO);
        } else if (couponCode.getQtype() != null && couponCode.getQtype() == 2) {
        	c.andIsNotNull("USER_ID");
        	c.andEqualTo("IS_USED", Coupon.NO);
        } else if (couponCode.getQtype() != null && couponCode.getQtype() == 3) {
        	c.andIsNotNull("USER_ID");
        	c.andEqualTo("IS_USED", Coupon.YES);
        }
        c.andEqualTo("COUPON", couponCode.getCoupon());
	}
}