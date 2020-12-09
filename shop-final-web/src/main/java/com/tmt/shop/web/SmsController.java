package com.tmt.shop.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.core.config.Globals;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.ShopConstant;
import com.tmt.shop.entity.Sms;
import com.tmt.shop.service.SmsServiceFacade;

/**
 * 短信配置 管理
 * @author 超级管理员
 * @date 2017-09-12
 */
@Controller("shopSmsController")
@RequestMapping(value = "${adminPath}/shop/sms")
public class SmsController extends BaseController{
	
	@Autowired
	private SmsServiceFacade smsService;
	
	/**
	 * 列表初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Sms sms, Model model){
		return "/shop/SmsList";
	}
	
	/**
	 * 列表页面的数据 
	 * @param sms
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Sms sms, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(sms, c);
		return smsService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param sms
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Sms sms, Model model) {
	    if(sms != null && !IdGen.isInvalidId(sms.getId())) {
		   sms = this.smsService.get(sms.getId());
		} else {
		   if(sms == null) {
			  sms = new Sms();
		   }
		   sms.setScene(ShopConstant.SCENE_CODE);
		   sms.setSort((byte)1);
		   sms.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("sms", sms);
		return "/shop/SmsForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Sms sms, Model model, RedirectAttributes redirectAttributes) {
		this.smsService.save(sms);
		//SmsUtils.clearCache(sms.getScene());
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改短信配置", sms.getName(), "成功"));
		redirectAttributes.addAttribute("id", sms.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/sms/form");
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
		List<Sms> smss = Lists.newArrayList();
		for(Long id: idList) {
			Sms sms = new Sms();
			sms.setId(id);
			smss.add(sms);
		}
		this.smsService.delete(smss);
		return AjaxResult.success();
	}

	//查询条件
	private void initQc(Sms sms, Criteria c) {
        if(StringUtils.isNotBlank(sms.getName())) {
           c.andEqualTo("NAME", sms.getName());
        }
	}
}