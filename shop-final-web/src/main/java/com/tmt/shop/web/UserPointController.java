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
import com.tmt.shop.entity.UserPoint;
import com.tmt.shop.service.UserPointService;

/**
 * 积分明细 管理
 * @author 超级管理员
 * @date 2017-05-16
 */
@Controller("shopUserPointController")
@RequestMapping(value = "${adminPath}/shop/user/point")
public class UserPointController extends BaseController{
	
	@Autowired
	private UserPointService userPointService;
	
	
	/**
	 * 列表初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(UserPoint userPoint, Model model){
		return "/shop/UserPointList";
	}
	
	/**
	 * 列表页面的数据 
	 * @param userPoint
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(UserPoint userPoint, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(userPoint, c);
		return userPointService.queryForPage(qc, param);
	}
	
	//查询条件
	private void initQc(UserPoint userPoint, Criteria c) {
        if(StringUtils.isNotBlank(userPoint.getName())) {
           c.andEqualTo("NAME", userPoint.getName());
        }
	}
}
