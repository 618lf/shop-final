package com.tmt.api.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.api.entity.Mock;
import com.tmt.api.service.MockService;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.Lists;
import com.tmt.core.web.BaseController;

/**
 * Mock 管理
 * @author 超级管理员
 * @date 2017-06-15
 */
@Controller("apiMockController")
@RequestMapping(value = "${adminPath}/api/mock")
public class MockController extends BaseController{
	
	@Autowired
	private MockService mockService;
	
	
	/**
	 * 列表初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Mock mock, Model model){
		return "/api/MockList";
	}
	
	/**
	 * 列表页面的数据 
	 * @param mock
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Mock mock, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(mock, c);
		return mockService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param mock
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Mock mock, Model model) {
	    if(mock != null && !IdGen.isInvalidId(mock.getId())) {
		   mock = this.mockService.get(mock.getId());
		} else {
		   if(mock == null) {
			  mock = new Mock();
		   }
		   mock.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("mock", mock);
		return "/api/MockForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("save")
	public AjaxResult save(Mock mock) {
		this.mockService.save(mock);
		return AjaxResult.success();
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
		List<Mock> mocks = Lists.newArrayList();
		for(Long id: idList) {
			Mock mock = new Mock();
			mock.setId(id);
			mocks.add(mock);
		}
		this.mockService.delete(mocks);
		return AjaxResult.success();
	}
	


	//查询条件
	private void initQc(Mock mock, Criteria c) {
	}
	
}
