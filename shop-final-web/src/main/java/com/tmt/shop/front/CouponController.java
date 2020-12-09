package com.tmt.shop.front;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.config.Globals;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.utils.WebUtils;
import com.tmt.shop.entity.CouponCode;
import com.tmt.shop.entity.CouponFission;
import com.tmt.shop.service.CouponCodeServiceFacade;
import com.tmt.shop.service.CouponServiceFacade;
import com.tmt.system.entity.User;
import com.tmt.system.utils.UserUtils;

/**
 * 跳转到首页
 * @author root
 */
@Controller("frontCouponController")
@RequestMapping(value = "${frontPath}/member/coupon")
public class CouponController {
	
	@Autowired
	private CouponServiceFacade couponService;
	@Autowired
	private CouponCodeServiceFacade couponCodeService;
	
	/**
	 * 我的优惠券
	 * @return
	 */
	@RequestMapping(value="codeList.html")
	public String codeList() {
		return "/front/member/CouponCodeList";
	}
	
	/**
	 * 列表数据
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/codeList/data.json")
	public Page page(String queryStatus, Page page) {
		PageParameters param = page.getParam();
		param.setPageSize(10);
		return this.couponCodeService.queryUserPage(UserUtils.getUser(), param);
	}
	
	/**
	 * 根据优惠码获取优惠券
	 **/
	@ResponseBody
	@RequestMapping("/fetchByCode.json")
	public AjaxResult fetchByCode(String code){
		CouponCode coupon = couponService.assignOneCode(UserUtils.getUser(), code);
		if (coupon == null) {
			return AjaxResult.error("获取失败");
		}
		return AjaxResult.success(coupon.getVal());
	}
	
	/**
	 * 有需分享的则跳转页面
	 * 红包 -- 满赠送 
	 * 来自订单
	 * 查询裂变号
	 * @return
	 */
	@RequestMapping("/redenvelope_from_fission/{fissionId}.html")
	public String redenvelope(@PathVariable Long fissionId, HttpServletRequest request, HttpServletResponse response, Model model) {
		if (WebUtils.isNewUser(request)) {
			// 清除新人标识
			WebUtils.removeNewUser(request, response);
			// 新用户(直接跳转到新人礼包上)
			return WebUtils.redirectTo(Globals.getFrontPath(), "/member/promotion/newgift.html");
		}
		
		// 老用户
		User user = UserUtils.getUser();
		CouponFission fission = this.couponService.giveFissionRedenvelope(user, fissionId);
		CouponCode coupon = fission.getOneCode();
		if (coupon == null) {
			return "/front/member/FissionRedenvelopeError";
		}
		model.addAttribute("fission", fission);
		model.addAttribute("coupon", coupon);
		return "/front/member/FissionRedenvelope";
	}
	
	/**
	 * 大家分享的数据
	 * @param fissionId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/fission_redenvelopes/{fissionId}.json")
	public AjaxResult redenvelopes(@PathVariable Long fissionId) {
		List<CouponCode> giveds = this.couponCodeService.queryfissionRedenvelopes(fissionId);
		if (giveds.isEmpty()) {
			return AjaxResult.error("无数据");
		}
		return AjaxResult.success(giveds);
	}
}