package com.tmt.gen.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.common.config.Globals;
import com.tmt.common.entity.AjaxResult;
import com.tmt.common.persistence.Page;
import com.tmt.common.persistence.PageParameters;
import com.tmt.common.persistence.QueryCondition;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.StringUtil3;
import com.tmt.common.web.BaseController;
import com.tmt.gen.entity.Scheme;
import com.tmt.gen.service.SchemeService;
import com.tmt.gen.utils.GenUtils;

/**
 * 文章管理
 * @author lifeng
 */
@Controller
@RequestMapping(value = "${adminPath}/gen/scheme")
public class SchemeController extends BaseController{

	@Autowired
	private SchemeService schemeService;
	
	/**
	 * 初始化页面的查询条件
	 * @param qa
	 * @param model
	 * @return
	 */
	@RequestMapping("list")
	public String list(Scheme scheme, Model model){
		return "/gen/SchemeList";
	}
	
	/**
	 * 初始化页面的数据
	 * @param qa
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Scheme scheme, Model model, Page page){
		QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		return schemeService.queryForPage(qc, param);
	}
	
	/**
	 * 业务表表单
	 * @param qa
	 * @param model
	 * @return
	 */
	@RequestMapping("form")
	public String form(Scheme scheme, Model model){
		if(scheme != null && !IdGen.isInvalidId(scheme.getId())) {
			scheme = this.schemeService.getWithTable(scheme.getId());
		} else {
			scheme.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("scheme", scheme);
		model.addAttribute("config", GenUtils.getConfig());
		return "/gen/SchemeForm";
	}
	
	/**
	 * 业务表保存
	 * @param businessType
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Scheme scheme, Model model, RedirectAttributes redirectAttributes) {
		this.schemeService.save(scheme);
		addMessage(redirectAttributes, StringUtil3.format("%s'%s'%s", "保存方案", scheme.getName(), "成功"));
		redirectAttributes.addAttribute("id", scheme.getId()); 
		return "redirect:"+Globals.getAdminPath()+"/gen/scheme/form";
	}
	
	/**
	 * 方案删除
	 * @param idList
	 * @param model
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete")
	public AjaxResult delete(Long[] idList) {
		List<Scheme> schemes = Lists.newArrayList();
		for(Long id: idList) {
			Scheme scheme = new Scheme();
			scheme.setId(id);
			schemes.add(scheme);
		}
		this.schemeService.delete(schemes);
		return AjaxResult.success();
	}
}