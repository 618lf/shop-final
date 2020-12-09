package com.tmt.shop.front;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.entity.AjaxResult;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.shop.entity.Epay;
import com.tmt.shop.entity.Payment;
import com.tmt.shop.entity.Rank;
import com.tmt.shop.entity.UserRank;
import com.tmt.shop.service.UserRankServiceFacade;
import com.tmt.shop.utils.EpayUtils;
import com.tmt.shop.utils.RankUtils;
import com.tmt.system.entity.User;
import com.tmt.system.utils.UserUtils;

/**
 * 用户等级
 * @author lifeng
 *
 */
@Controller("frontUserRankController")
@RequestMapping(value = "${frontPath}/member/rank")
public class UserRankController extends PaymentController {

	@Autowired
	private UserRankServiceFacade userRankService;
	
	/**
	 * 用户等级
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/get.json")
	public AjaxResult get() {
		User user = UserUtils.getUser();
		UserRank rank = userRankService.touch(user.getId());
		if (rank != null && rank.getRankId() != null) {
			Rank _rank = RankUtils.getRank(rank.getRankId());
			if (_rank != null) {
				rank.setRankImage(_rank.getImage());
				rank.setRankName(_rank.getName());
			}
		}
		return AjaxResult.success(rank);
	}
	
	/**
	 * 会员特权
	 * @return
	 */
	@RequestMapping("/right.html")
	public String right(Model model) {
		User user = UserUtils.getUser();
		UserRank urank = userRankService.touch(user.getId());
		if (urank != null && urank.getRankId() != null) {
			Rank rank = RankUtils.getRank(urank.getRankId());
			model.addAttribute("rank", rank);
		}
		model.addAttribute("urank", urank);
		return "/front/rank/RankRight";
	}
	
	/**
	 * 详情
	 * @return
	 */
	@RequestMapping("/centre.html")
	public String centre(Model model) {
		
		// 我的等级
		User user = UserUtils.getUser();
		UserRank urank = userRankService.touch(user.getId());
		if (urank != null && urank.getRankId() != null) {
			Rank rank = RankUtils.getRank(urank.getRankId());
			model.addAttribute("rank", rank);
		}
		model.addAttribute("urank", urank);
		
		// 所有的等级
		List<Rank> ranks = RankUtils.getRanks();
		model.addAttribute("ranks", ranks);
		return "/front/rank/RankCentre";
	}
	
	/**
	 * 开通会员
	 * @return
	 */
	@RequestMapping("/open/{id}.html")
	public String open(@PathVariable Long id, Model model) {
		Rank rank = RankUtils.getRank(id);
		Date expiryDate = DateUtils.addDays(DateUtils.getTodayOffsetDate(1), 365);
		model.addAttribute("rank", rank);
		model.addAttribute("expiryDate", expiryDate);
		return "/front/rank/RankOpen";
	}
	
	/**
	 * 支付初始化
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/payment/init.json", method = RequestMethod.POST)
	public PayResult payment_init(Long id, Long epayId, HttpServletRequest request) {
		
		// 要开通的权限
		Rank toRank = RankUtils.getRank(id);
		if (toRank == null) {
			return PayResult.fail("错误");
		}
		
		// 当前用户
		User user = UserUtils.getUser();
		UserRank urank = userRankService.get(user.getId());
		Rank rank = null;
		// 如果已经开通过权限，则不能开通低权限
		if (urank != null && urank.getUseAble() == 1) {
			rank = RankUtils.getRank(urank.getRankId());
			if (rank != null && rank.getGrade() >= toRank.getGrade()) {
				String error = StringUtils.format("您已经是%s,不能开通%s", rank.getName(), toRank.getName());
				return PayResult.fail(error);
			}
		}
		
		// 支付插件(epayId == null 会返回默认的插件)
		Epay epay = EpayUtils.get(epayId);
		if (epay == null) {
			return PayResult.fail("系统未开放支付");
		}
		
		// 支付对象
		Payment payment = this.paymentService.initPayment(epay, rank, toRank, user);
		if (payment == null) {
			return PayResult.fail("请返回重试！");
		}
		return PayResult.needPay(payment.getId());
	}
}