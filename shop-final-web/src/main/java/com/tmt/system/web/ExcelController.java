package com.tmt.system.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.common.config.Globals;
import com.tmt.common.entity.AjaxResult;
import com.tmt.common.persistence.Page;
import com.tmt.common.persistence.PageParameters;
import com.tmt.common.persistence.QueryCondition;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.utils.ExcelExpUtils;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.Maps;
import com.tmt.common.utils.WebUtils;
import com.tmt.common.web.BaseImpExportController;
import com.tmt.system.entity.ExcelItem;
import com.tmt.system.entity.ExcelTemplate;
import com.tmt.system.service.ExcelTemplateServiceFacade;
import com.tmt.system.utils.ExcelImpUtil;

@Controller
@RequestMapping(value = "${spring.application.web.admin}/system/excel")
public class ExcelController extends BaseImpExportController<ExcelTemplate>{
	
	@Autowired
	private ExcelTemplateServiceFacade templateService;
	
	@RequestMapping("list")
	public String list(){
		return "/system/ExcelList";
	}
	
	/**
	 * 列表数据
	 * @param model
	 * @param page
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Model model, Page page, HttpServletResponse response) {
		QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		page  = this.templateService.queryForPage(qc, param);
		return page;
	}
	
	/**
	 * 表单
	 * @param template
	 * @param model
	 * @return
	 */
	@RequestMapping("form")
	public String form(ExcelTemplate template,Model model) {
		if( template != null && template.getId() != null) {
			template = this.templateService.getWithItems(template.getId());
		} else {
			template = new ExcelTemplate();
			template.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("template", template);
		return "/system/ExcelForm";
	}
	
	/**
	 * 保存
	 * @param template
	 * @param model
	 * @param request
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(ExcelTemplate template, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		List<ExcelItem> items = WebUtils.fetchItemsFromRequest(request, ExcelItem.class, "items.");
		template.setItems(items);
		this.templateService.save(template);
		addMessage(redirectAttributes, "保存模版'" + template.getName() + "'成功");
		redirectAttributes.addAttribute("id", template.getId());
		return "redirect:"+Globals.adminPath+"/system/excel/form";
	}
	
	/**
	 * 删除
	 * @param idList
	 * @param model
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete")
	public AjaxResult delete(Long[] idList, Model model, HttpServletResponse response) {
		List<ExcelTemplate> templates = Lists.newArrayList();
		for(Long id: idList) {
			ExcelTemplate template = new ExcelTemplate();
			template.setId(id);
			templates.add(template);
		}
		this.templateService.delete(templates);
		return AjaxResult.success();
	}
	/**
	 * 导出数据封装
	 */
	@Override
	public Map<String, Object> doExport(ExcelTemplate param, HttpServletRequest request) {
		Map<String, Object> datas = Maps.newHashMap();
		param = this.templateService.getWithItems(param.getId());
		if( param != null ) {
			datas = ExcelExpUtils.buildExpParams(param.getName(), param.getName(), param.getItemsMapper(), null);
		}
		return datas;
	}

	/**
	 * Controller 中获取泛型的实际类型有问题
	 */
	@Override
	protected Class<ExcelTemplate> getTargetClass() {
		return ExcelTemplate.class;
	}
	
	/**
	 * 导入验证
	 * @param templateId
	 * @param file
	 * @return
	 */
	protected  AjaxResult doImport(Long templateId, HttpServletRequest request, MultipartFile file) {
		AjaxResult result = ExcelImpUtil.fetchObjectFromTemplate(templateId, file);
		return result;
	}
}
