package com.tmt.shop.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.entity.AjaxResult;
import com.tmt.core.utils.StringUtils;
import com.tmt.shop.entity.CouponCode;
import com.tmt.shop.entity.GiveRedenvelopeResult;
import com.tmt.shop.entity.Promotion;
import com.tmt.shop.entity.ShopConstant;
import com.tmt.shop.service.PromotionServiceFacade;
import com.tmt.shop.utils.PromotionUtils;
import com.tmt.system.entity.User;
import com.tmt.system.utils.UserUtils;

/**
 * 跳转到首页
 * @author root
 */
@Controller("frontPromotionController")
@RequestMapping(value = "${frontPath}/member/promotion")
public class PromotionController {
	
	@Autowired
	private PromotionServiceFacade promotionService;
	
	/**
	 * 新人礼包
	 * @return
	 */
	@RequestMapping(value="newgift.html")
	public String newGift(String recommendId, Model model) {
		// 若已经领取新人红包，则直接返回已经领取页面
		if(this.isGetNewGift()){
			return  "/front/promotion/CouponError";
		}
		if (StringUtils.isNotBlank(recommendId)){
			model.addAttribute("recommendId", recommendId);
		}
		return "/front/promotion/NewGift";
	}
	
	/**
	 * 1. 第一步需要判断新人礼包是否在有效期内
	 * 2. 判断用户是否领取新人礼包
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="isGetNewGift.json")
	public Boolean isGetNewGift() {
		
		// 新人礼包的可用状态 -- 返回已领取状态
		Promotion promotion = PromotionUtils.getCachedPromotion(Promotion.XR_ID);
		if (promotion == null || promotion.getUseAble() != Promotion.NO) { // 状态设置问题，1不可用
			return Boolean.TRUE;
		}
		
		User user = UserUtils.getUser();
		Boolean fetch = UserUtils.getAttribute(ShopConstant.XR_KEY);
		if (fetch == null) {
			fetch = promotionService.userFetchXrGift(user);
			UserUtils.setAttribute(ShopConstant.XR_KEY, fetch);
		}
		return fetch;
	}
	
	/**
	 * 新人领取礼包
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="getNewGift.json")
	public AjaxResult getNewGift(String recommendId) {
		User user = UserUtils.getUser();
		
		// 先校验是否已获取
		Boolean fetch = UserUtils.getAttribute(ShopConstant.XR_KEY);
		if (fetch == null) {
			fetch = promotionService.userFetchXrGift(user);
			UserUtils.setAttribute(ShopConstant.XR_KEY, fetch);
		}
		
		// 是否已获取
		if (fetch) {
			return AjaxResult.error("已获取");
		}
		
		// 获取
		List<CouponCode> codes = promotionService.fetchXrGifts(user, recommendId);
		
		// 删除临时缓存
		UserUtils.removeAttribute(ShopConstant.XR_KEY);
		if (codes == null) {
			return AjaxResult.error("已获取");
		}
		return AjaxResult.success(codes);
	}
	
	/**
	 * 推荐有礼
	 * @return
	 */
	@RequestMapping(value="recommend.html")
	public String recommend(Model model) {
		model.addAttribute("recommendId", UserUtils.getUser().getId());
		return "/front/member/Recommend";
	}
	
	/**
	 * 发放红包
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/give_redenvelope_form_order.json", method = RequestMethod.POST)
	public GiveRedenvelopeResult giveRedenvelope(Long id) {
		// 基本参数验证
		if (id == null) {return GiveRedenvelopeResult.fail("拆红包失败");}
		
		// 当前用户
		User user = UserUtils.getUser();
		
		// 无需分享的则直接弹出
		GiveRedenvelopeResult result = this.promotionService.giveRedenvelope(user, id);
		
		if (result == null) {
			return GiveRedenvelopeResult.fail("拆红包失败");
		}
		return result;
	}
}
