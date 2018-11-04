package com.tmt.wechat.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.common.config.Globals;
import com.tmt.common.entity.AjaxResult;
import com.tmt.common.persistence.Page;
import com.tmt.common.persistence.PageParameters;
import com.tmt.common.persistence.QueryCondition;
import com.tmt.common.persistence.QueryCondition.Criteria;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.staticize.StaticUtils;
import com.tmt.common.utils.JsonMapper;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.StringUtil3;
import com.tmt.common.utils.WebUtils;
import com.tmt.common.web.BaseController;
import com.tmt.wechat.entity.App;
import com.tmt.wechat.entity.MetaRich;
import com.tmt.wechat.entity.MetaRichRela;
import com.tmt.wechat.service.AppServiceFacade;
import com.tmt.wechat.service.MetaRichServiceFacade;
import com.tmt.wechat.utils.WechatUtils;

/**
 * 图文回复 管理
 * @author 超级管理员
 * @date 2016-09-30
 */
@Controller("wechatMetaRichController")
@RequestMapping(value = "${adminPath}/wechat/meta/rich")
public class MetaRichController extends BaseController{
	
	@Autowired
	private MetaRichServiceFacade metaRichService;
	@Autowired
	private AppServiceFacade appService;
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(MetaRich metaRich, Model model){
		return "/wechat/MetaRichList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param metaRich
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(MetaRich metaRich, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(metaRich, c);
		qc.setOrderByClause(param.orderBy("CREATE_DATE DESC"));
		return metaRichService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param metaRich
	 * @param model
	 */
	@RequestMapping("form")
	public String form(MetaRich metaRich, Model model) {
	    if(metaRich != null && !IdGen.isInvalidId(metaRich.getId())) {
		   metaRich = this.metaRichService.getWithRelas(metaRich.getId());
		} else {
		   if(metaRich == null) {
			  metaRich = new MetaRich();
		   }
		   metaRich.setId(IdGen.INVALID_ID);
		   metaRich.setTopImage(MetaRich.YES);
		}
		model.addAttribute("metaRich", metaRich);
		model.addAttribute("apps", appService.getAll());
		return "/wechat/MetaRichForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(MetaRich metaRich, Model model, RedirectAttributes redirectAttributes) {
		String postData = WebUtils.getCleanParam("postData");
		List<MetaRichRela> relas = JsonMapper.fromJsonToList(postData, MetaRichRela.class);
		metaRich.setRelas(relas);
		this.metaRichService.save(metaRich);
		addMessage(redirectAttributes, StringUtil3.format("%s'%s'%s", "修改图文回复", metaRich.getKeyword(), "成功"));
		redirectAttributes.addAttribute("id", metaRich.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/wechat/meta/rich/form");
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
		List<MetaRich> metaRichs = Lists.newArrayList();
		for(Long id: idList) {
			MetaRich metaRich = new MetaRich();
			metaRich.setId(id);
			metaRichs.add(metaRich);
		}
		this.metaRichService.delete(metaRichs);
		return AjaxResult.success();
	}
	


	//查询条件
	private void initQc(MetaRich metaRich, Criteria c) {
        if(StringUtil3.isNotBlank(metaRich.getKeyword())) {
           c.andEqualTo("KEYWORD", metaRich.getKeyword());
        }
        if(StringUtil3.isNotBlank(metaRich.getTitle())) {
           c.andEqualTo("TITLE", metaRich.getTitle());
        }
	}
	
	/**
	 * 表组件支持
	 * 需要排除的ID
	 */
	@RequestMapping("tableSelect")
	public String tableSelect(Long extId, Model model) {
	   model.addAttribute("extId", extId);
	   return "/wechat/MetaRichTableSelect";
	}
	
	/**
	 * 选择的数据
	 * @param metaRich
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("tableSelect/data")
	public Page tableSelect_data(MetaRich metaRich, Long extId, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(metaRich, c);
		if (extId != null && !IdGen.isInvalidId(extId)) {
			c.andNotEqualTo("ID", extId);
		}
		qc.setOrderByClause(param.orderBy("CREATE_DATE DESC"));
		return metaRichService.queryForPage(qc, param);
	}
	
	/**
	 * 阅览地址
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("view/{id}")
	public AjaxResult view(@PathVariable Long id) {
		MetaRich metaRich = this.metaRichService.getWithRelas(id);
    	App app = WechatUtils.get(metaRich.getAppId());
    	metaRich.setApp(app);
		String url = StaticUtils.touchStaticizePage(app, "metaRich", metaRich);
		return AjaxResult.success(url);
	}
	
	/**
	 * 获取图文
	 * @return
	 */
	@ResponseBody
	@RequestMapping("get/{id}")
	public AjaxResult get(@PathVariable Long id) {
		MetaRich metaRich = this.metaRichService.getWithRelas(id);
		return AjaxResult.success(metaRich);
	}
}
