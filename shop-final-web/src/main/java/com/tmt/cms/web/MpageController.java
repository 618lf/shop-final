package com.tmt.cms.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.cms.entity.Mpage;
import com.tmt.cms.entity.MpageField;
import com.tmt.cms.entity.Mtemplate;
import com.tmt.cms.service.MpageServiceFacade;
import com.tmt.cms.service.MtemplateServiceFacade;
import com.tmt.core.config.Globals;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.staticize.StaticUtils;
import com.tmt.core.utils.FreemarkerUtils;
import com.tmt.core.utils.JsonMapper;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.Store;
import com.tmt.shop.utils.StoreUtils;

/**
 * 页面 管理
 * @author 超级管理员
 * @date 2016-11-04
 */
@Controller("cmsMpageController")
@RequestMapping(value = "${adminPath}/cms/mpage")
public class MpageController extends BaseController{
	
	@Autowired
	private MpageServiceFacade mpageService;
	@Autowired
	private MtemplateServiceFacade templateService;
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Mpage mpage, Model model){
		return "/cms/MpageList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param mpage
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Mpage mpage, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(mpage, c);
		return mpageService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param mpage
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Mpage mpage, Model model) {
	    if (mpage != null && !IdGen.isInvalidId(mpage.getId())) {
		    mpage = this.mpageService.get(mpage.getId());
		} else {
		    if (mpage == null) {
			    mpage = new Mpage();
		    }
		    mpage.setId(IdGen.INVALID_ID);
		    mpage.setType((byte)0);
		}
		model.addAttribute("mpage", mpage);
		return "/cms/MpageForm";
	}
	
	/**
	 * 加载组件
	 * @return
	 */
	@ResponseBody
	@RequestMapping("load_fields")
	public AjaxResult loadFields(Long pageId) {
		// 所有的字段
		List<MpageField> fields = this.mpageService.queryFieldsByPageId(pageId);
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
		Mpage mpage = JsonMapper.fromJson(postData, Mpage.class);
		this.mpageService.save(mpage);
		// 清空组件
		mpage.setFields(null);
		return AjaxResult.success(mpage);
	}
	
	/**
	 * 模板创建
	 * @param mpage
	 * @param model
	 */
	@RequestMapping("use_template")
	public String use_template(Mtemplate template, Model model, RedirectAttributes redirectAttributes) {
		Mpage mpage = new Mpage();  mpage.setId(IdGen.INVALID_ID);
	    if (!IdGen.isInvalidId(template.getId()) && (template = templateService.getWithFields(template.getId())) != null) {
	    	mpage.setTitle(template.getTitle());
	    	mpage.setColor(template.getColor());
	    	mpage.setDescription(template.getDescription());
	    	mpage.setMfields(template.getMfields());
	    	this.mpageService.copy(mpage);
	    	mpage.setFields(null);
	    }
	    redirectAttributes.addAttribute("id", mpage.getId());
	    return WebUtils.redirectTo(Globals.getAdminPath(), "/cms/mpage/form");
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
		List<Mpage> mpages = Lists.newArrayList();
		for(Long id: idList) {
			Mpage mpage = new Mpage();
			mpage.setId(id);
			mpages.add(mpage);
		}
		this.mpageService.delete(mpages);
		return AjaxResult.success();
	}
	
    /**
	 * 批量修改栏目排序
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("sort")
	public AjaxResult updateSort(Model model, HttpServletRequest request, HttpServletResponse response) {
		String postData = request.getParameter("postData");
		List<Map<String,String>> maps = JsonMapper.fromJson(postData, ArrayList.class);
		if(maps != null && maps.size() != 0){
			List<Mpage> menus = Lists.newArrayList();
			for(Map<String,String> map: maps) {
				Mpage menu = new Mpage();
				menu.setId(Long.parseLong(map.get("id")));
				menu.setSort(Integer.parseInt(map.get("sort")));
				menus.add(menu);
			}
			this.mpageService.updateSort(menus);
			return AjaxResult.success();
		}
		return AjaxResult.error("没有需要保存的数据");
	}
	
	/**
	 * 阅览地址
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("view/{id}")
	public AjaxResult view(@PathVariable Long id) {
		Mpage mpage = this.mpageService.getWithFields(id);
		List<MpageField> fields = mpage.getMfields();
		for(MpageField field: fields) {
			Map<String, Object> context = FreemarkerUtils.escape(JsonMapper.fromJson(field.getConfig(), Map.class));
			field.setMconfig(context);
		}
		Store store = StoreUtils.getDefaultStore();
		return AjaxResult.success(StaticUtils.touchStaticizePage(store, "mpage", mpage));
	}

	//查询条件
	private void initQc(Mpage mpage, Criteria c) {
        if (StringUtils.isNotBlank(mpage.getName())) {
            c.andEqualTo("NAME", mpage.getName());
        }
	}
	
	/**
	 * 表组件支持
	 */
	@RequestMapping("table_select")
	public String tableSelect() {
	   return "/cms/MpageTableSelect";
	}
	
	/**
	 * 商品相关信息(简单信息)
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("get/{id}")
	public Mpage get(@PathVariable Long id) {
		Mpage page = mpageService.getWithFields(id);
		List<MpageField> fields = page.getMfields();
		for(MpageField field: fields) {
			Map<String, Object> context = FreemarkerUtils.escape(JsonMapper.fromJson(field.getConfig(), Map.class));
			field.setMconfig(context);
		}
		Store store = StoreUtils.getDefaultStore();
		page.setUrl(StaticUtils.touchStaticizePage(store, "mpage", page));
		return page;
	}
}