package com.tmt.shop.front;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tmt.shop.entity.Promotion;
import com.tmt.shop.utils.PromotionUtils;

/**
 * 跳转到首页
 * @author root
 */
@Controller("frontPromotionViewController")
@RequestMapping(value = "${frontPath}/shop/promotion")
public class PromotionViewController {

	@RequestMapping("/{id}.html")
	public String view(@PathVariable Long id, Model model) {
		Promotion promotion = PromotionUtils.getCachedPromotion(id);
		model.addAttribute("promotion", promotion);
		return "/front/promotion/PromotionView";
	}
}
