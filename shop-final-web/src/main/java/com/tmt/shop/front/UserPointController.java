package com.tmt.shop.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.shop.entity.UserRank;
import com.tmt.shop.service.UserPointServiceFacade;
import com.tmt.shop.service.UserRankServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 用户积分
 * @author lifeng
 *
 */
@Controller("frontUserPointController")
@RequestMapping(value = "${frontPath}/member/point")
public class UserPointController {

	@Autowired
	private UserPointServiceFacade userPointService;
	@Autowired
	private UserRankServiceFacade userRankService;
	
	/**
	 * 列表
	 * @return
	 */
	@RequestMapping("/list.html")
	public String list(Model model) {
		UserRank rank = userRankService.touch(UserUtils.getUser().getId());
		model.addAttribute("rank", rank);
		return "/front/rank/PointList";
	}
	
	/**
	 * 列表数据
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/list/data.json")
	public Page page(String queryStatus, Page page) {
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria();
		c.andEqualTo("USER_ID", UserUtils.getUser().getId());
		qc.setOrderByClause("CREATE_DATE DESC");
		PageParameters param = page.getParam();
		param.setPageSize(15);
		return this.userPointService.queryForPage(qc, param);
	}
}