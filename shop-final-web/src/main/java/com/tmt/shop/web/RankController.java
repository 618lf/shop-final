package com.tmt.shop.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
import com.tmt.shop.entity.Rank;
import com.tmt.shop.entity.RankCoupon;
import com.tmt.shop.service.RankServiceFacade;

/**
 * 等级设置 管理
 * @author 超级管理员
 * @date 2017-02-18
 */
@Controller("shopRankController")
@RequestMapping(value = "${adminPath}/shop/rank")
public class RankController extends BaseController{
	
	@Autowired
	private RankServiceFacade rankService;
	
	/**
	 * 列表初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Rank rank, Model model){
		return "/shop/RankList";
	}
	
	/**
	 * 列表页面的数据 
	 * @param rank
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Rank rank, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(rank, c);
		qc.setOrderByClause("GRADE, MIN_POINTS");
		return rankService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param rank
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Rank rank, Model model) {
	    if(rank != null && !IdGen.isInvalidId(rank.getId())) {
		   rank = this.rankService.getWithCoupons(rank.getId());
		} else {
		   if(rank == null) {
			  rank = new Rank();
		   }
		   rank.setId(IdGen.INVALID_ID);
		   rank.setAutoUpgrade(Rank.NO);
		}
		model.addAttribute("rank", rank);
		return "/shop/RankForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Rank rank, Model model, RedirectAttributes redirectAttributes) {
		// 赠送设置 
		String _coupons = WebUtils.getCleanParam("_coupons");
		if (StringUtils.isNotBlank(_coupons)) {
			List<RankCoupon> coupons = JsonMapper.fromJsonToList(_coupons, RankCoupon.class);
			rank.setCoupons(coupons);
		}
		this.rankService.save(rank);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改等级", rank.getName(), "成功"));
		redirectAttributes.addAttribute("id", rank.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/rank/form");
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
		List<Rank> ranks = Lists.newArrayList();
		for(Long id: idList) {
			Rank rank = new Rank();
			rank.setId(id);
			ranks.add(rank);
		}
		this.rankService.delete(ranks);
		return AjaxResult.success();
	}
	
	//查询条件
	private void initQc(Rank rank, Criteria c) {}
}