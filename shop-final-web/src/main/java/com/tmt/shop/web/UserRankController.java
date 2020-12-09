package com.tmt.shop.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.core.config.Globals;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.Payment;
import com.tmt.shop.entity.UserRank;
import com.tmt.shop.service.PaymentServiceFacade;
import com.tmt.shop.service.UserRankServiceFacade;

/**
 * 用户等级 管理
 * @author 超级管理员
 * @date 2017-02-18
 */
@Controller("shopUserRankController")
@RequestMapping(value = "${adminPath}/shop/user/rank")
public class UserRankController extends BaseController{
	
	@Autowired
	private UserRankServiceFacade userRankService;
	@Autowired
	private PaymentServiceFacade paymentService;
	
	/**
	 * 列表初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(UserRank userRank, Model model){
		return "/shop/UserRankList";
	}
	
	/**
	 * 列表页面的数据 
	 * @param userRank
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(UserRank userRank, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(userRank, c);
		qc.setOrderByClause("IS_ENABLED DESC");
		return userRankService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param userRank
	 * @param model
	 */
	@RequestMapping("form")
	public String form(UserRank userRank, Model model) {
	    if (userRank != null && !IdGen.isInvalidId(userRank.getUserId())) {
		    userRank = this.userRankService.get(userRank.getUserId());
		} else {
		   if(userRank == null) {
			  userRank = new UserRank();
		   }
		   userRank.setPoints(0);
		}
		model.addAttribute("userRank", userRank);
		return "/shop/UserRankForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(UserRank userRank, Model model, RedirectAttributes redirectAttributes) {
		this.userRankService.save(userRank);
		addMessage(redirectAttributes, StringUtils.format("%s%s", "修改用户等级", "成功"));
		redirectAttributes.addAttribute("userId", userRank.getUserId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/user/rank/form");
	}
	
	/**
	 * 列表初始化页面
	 * @param model
	 */
	@RequestMapping("payment")
	public String payment(Payment payment, Model model){
		model.addAttribute("payment", payment);
		return "/shop/RankPaymentList";
	}
	
	/**
	 * 列表页面的数据 
	 * @param userRank
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("payment/page")
	public Page payment(Payment payment, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		c.andEqualTo("CREATE_ID", payment.getCreateId());
		c.andEqualTo("MODULE", Payment.rank_module);
		if (payment.getPayFlag() != null) {
			c.andEqualTo("PAY_FLAG", payment.getPayFlag());
		}
		qc.setOrderByClause(param.orderBy("CREATE_DATE DESC"));
		return paymentService.queryForPage(qc, param);
	} 
	
	//查询条件
	private void initQc(UserRank userRank, Criteria c) {}
}