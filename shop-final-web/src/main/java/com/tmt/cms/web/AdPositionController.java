package com.tmt.cms.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.cms.entity.AdPosition;
import com.tmt.cms.service.AdPositionServiceFacade;
import com.tmt.core.config.Globals;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;
import com.tmt.core.web.BaseController;

/**
 * 广告管理 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
@Controller("cmsAdPositionController")
@RequestMapping(value = "${adminPath}/cms/adPosition")
public class AdPositionController extends BaseController{
	
	@Autowired
	private AdPositionServiceFacade adPositionService;
	
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(AdPosition adPosition, Model model){
		return "/cms/AdPositionList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param adPosition
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(AdPosition adPosition, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(adPosition, c);
		return adPositionService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param adPosition
	 * @param model
	 */
	@RequestMapping("form")
	public String form(AdPosition adPosition, Model model) {
	    if(adPosition != null && !IdGen.isInvalidId(adPosition.getId())) {
		   adPosition = this.adPositionService.get(adPosition.getId());
		} else {
		   if(adPosition == null) {
			  adPosition = new AdPosition();
		   }
		   adPosition.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("adPosition", adPosition);
		return "/cms/AdPositionForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(AdPosition adPosition, Model model, RedirectAttributes redirectAttributes) {
		this.adPositionService.save(adPosition);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改广告管理", adPosition.getName(), "成功"));
		redirectAttributes.addAttribute("id", adPosition.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/cms/adPosition/form");
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
	public AjaxResult delete(Long[] idList , Model model, HttpServletResponse response) {
		List<AdPosition> adPositions = Lists.newArrayList();
		for(Long id: idList) {
			AdPosition adPosition = new AdPosition();
			adPosition.setId(id);
			adPositions.add(adPosition);
		}
		this.adPositionService.delete(adPositions);
		return AjaxResult.success();
	}
	
	/**
	 * 设置
	 * @return
	 */
	@RequestMapping("config")
	public String config(AdPosition adPosition, Model model) {
		adPosition = this.adPositionService.getWithAds(adPosition.getId());
		
		model.addAttribute("ad", adPosition);
		return "/cms/AdPositionConfig"; 
	}
	
	//查询条件
	private void initQc(AdPosition adPosition, Criteria c) {
        if(StringUtils.isNotBlank(adPosition.getName())) {
           c.andEqualTo("NAME", adPosition.getName());
        }
	}
}