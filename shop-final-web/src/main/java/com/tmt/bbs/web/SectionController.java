package com.tmt.bbs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.bbs.entity.Section;
import com.tmt.bbs.service.SectionServiceFacade;
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
 * 主题分类 管理
 * 
 * @author 超级管理员
 * @date 2017-04-12
 */
@Controller("bbsSectionController")
@RequestMapping(value = "${adminPath}/bbs/section")
public class SectionController extends BaseController {

	@Autowired
	private SectionServiceFacade sectionService;

	/**
	 * 列表初始化页面
	 * 
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Section section, Model model) {
		return "/bbs/SectionList";
	}

	/**
	 * 列表页面的数据
	 * 
	 * @param section
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Section section, Model model, Page page) {
		QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		this.initQc(section, c);
		qc.setOrderByClause("SORT");
		return sectionService.queryForPage(qc, param);
	}

	/**
	 * 表单
	 * 
	 * @param section
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Section section, Model model) {
		if (section != null && !IdGen.isInvalidId(section.getId())) {
			section = this.sectionService.get(section.getId());
		} else {
			if (section == null) {
				section = new Section();
			}
			section.setId(IdGen.INVALID_ID);
			section.setIsShow(Section.YES);
			section.setSort(1);
		}
		model.addAttribute("section", section);
		return "/bbs/SectionForm";
	}

	/**
	 * 保存
	 * 
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Section section, Model model, RedirectAttributes redirectAttributes) {
		this.sectionService.save(section);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改栏目", section.getName(), "成功"));
		redirectAttributes.addAttribute("id", section.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/bbs/section/form");
	}

	/**
	 * 删除
	 * 
	 * @param idList
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete")
	public AjaxResult delete(Long[] idList) {
		List<Section> sections = Lists.newArrayList();
		for (Long id : idList) {
			Section section = new Section();
			section.setId(id);
			sections.add(section);
		}
		this.sectionService.delete(sections);
		return AjaxResult.success();
	}

	// 查询条件
	private void initQc(Section section, Criteria c) {
		if (StringUtils.isNotBlank(section.getName())) {
			c.andEqualTo("NAME", section.getName());
		}
	}
}
