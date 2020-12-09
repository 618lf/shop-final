package com.tmt.cms.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.cms.entity.MpageField;
import com.tmt.cms.entity.Mtemplate;
import com.tmt.cms.service.MtemplateServiceFacade;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.JsonMapper;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.web.BaseController;

/**
 * 页面模板 管理
 * @author 超级管理员
 * @date 2016-11-11
 */
@Controller("cmsMtemplateController")
@RequestMapping(value = "${adminPath}/cms/mtemplate")
public class MtemplateController extends BaseController{
	
	@Autowired
	private MtemplateServiceFacade mtemplateService;
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Mtemplate mtemplate, Model model){
		return "/cms/MtemplateList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param mtemplate
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Mtemplate mtemplate, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(mtemplate, c);
		return mtemplateService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param mtemplate
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Mtemplate mtemplate, Model model) {
	    if(mtemplate != null && !IdGen.isInvalidId(mtemplate.getId())) {
		   mtemplate = this.mtemplateService.get(mtemplate.getId());
		} else {
		   if(mtemplate == null) {
			  mtemplate = new Mtemplate();
		   }
		   mtemplate.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("mtemplate", mtemplate);
		return "/cms/MtemplateForm";
	}
	
	/**
	 * 加载组件
	 * @return
	 */
	@ResponseBody
	@RequestMapping("load_fields")
	public AjaxResult loadFields(Long pageId) {
		// 所有的字段
		List<MpageField> fields = this.mtemplateService.queryFieldsByTemplateId(pageId);
		return AjaxResult.success(fields);
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
	public AjaxResult save(HttpServletRequest request) {
		String postData = request.getParameter("postData");
		Mtemplate mtemplate = JsonMapper.fromJson(postData, Mtemplate.class);
		this.mtemplateService.save(mtemplate);
		// 清空组件
		mtemplate.setFields(null);
		return AjaxResult.success(mtemplate);
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
		List<Mtemplate> mtemplates = Lists.newArrayList();
		for(Long id: idList) {
			Mtemplate mtemplate = new Mtemplate();
			mtemplate.setId(id);
			mtemplates.add(mtemplate);
		}
		this.mtemplateService.delete(mtemplates);
		return AjaxResult.success();
	}

	//查询条件
	private void initQc(Mtemplate mtemplate, Criteria c) {
        if (StringUtils.isNotBlank(mtemplate.getName())) {
            c.andEqualTo("NAME", mtemplate.getName());
        }
	}
	
	/**
	 * 表组件支持
	 */
	@RequestMapping("use_template")
	public String use_template() {
	   return "/cms/MtemplateSelect";
	}
}
