package com.tmt.shop.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.core.config.Globals;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.entity.BaseEntity;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.DeliveryScheme;
import com.tmt.shop.script.DeliveryScriptExecutor;
import com.tmt.shop.service.DeliverySchemeServiceFacade;

/**
 * 配送方案 管理
 * @author 超级管理员
 * @date 2016-11-06
 */
@Controller("shopDeliverySchemeController")
@RequestMapping(value = "${adminPath}/shop/delivery/scheme")
public class DeliverySchemeController extends BaseController{
	
	@Autowired
	private DeliverySchemeServiceFacade deliverySchemeService;
	private DeliveryScriptExecutor scriptExecutor = new DeliveryScriptExecutor();
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(DeliveryScheme deliveryScheme, Model model){
		return "/shop/DeliverySchemeList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param deliveryScheme
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(DeliveryScheme deliveryScheme, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(deliveryScheme, c);
		return deliverySchemeService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param deliveryScheme
	 * @param model
	 */
	@RequestMapping("form")
	public String form(DeliveryScheme deliveryScheme, Model model) {
	    if(deliveryScheme != null && !IdGen.isInvalidId(deliveryScheme.getId())) {
		   deliveryScheme = this.deliverySchemeService.get(deliveryScheme.getId());
		} else {
		   if(deliveryScheme == null) {
			  deliveryScheme = new DeliveryScheme();
		   }
		   deliveryScheme.setId(IdGen.INVALID_ID);
		   deliveryScheme.setIsDefault(BaseEntity.NO);
		}
		model.addAttribute("deliveryScheme", deliveryScheme);
		return "/shop/DeliverySchemeForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(DeliveryScheme deliveryScheme, Model model, RedirectAttributes redirectAttributes) {
		// 解析方案
		Map<String, Object> context = Maps.newHashMap();
		context.put("orderDate", DateUtils.getTodayTime());
		context.put("currDay", DateUtils.getTodayDate());
		context.put("currDate", DateUtils.getTodayTime());
		Object result1 = scriptExecutor.execute(deliveryScheme.getShippingExpression(), context);
		Object result2 = scriptExecutor.execute(deliveryScheme.getDeliveryExpression(), context);
		if (result1 == null || result2 == null) {
			addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改配送方案", deliveryScheme.getName(), "失败【表达式设置错误】"));
		} else {
			this.deliverySchemeService.save(deliveryScheme);
			addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改配送方案", deliveryScheme.getName(), "成功"));
		}
		redirectAttributes.addAttribute("id", deliveryScheme.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/delivery/scheme/form");
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
		List<DeliveryScheme> deliverySchemes = Lists.newArrayList();
		for(Long id: idList) {
			DeliveryScheme deliveryScheme = new DeliveryScheme();
			deliveryScheme.setId(id);
			deliverySchemes.add(deliveryScheme);
		}
		this.deliverySchemeService.delete(deliverySchemes);
		return AjaxResult.success();
	}
	
	/**
	 * 树组件支持
	 */
	@ResponseBody
	@RequestMapping("treeSelect")
	public List<Map<String, Object>> treeSelect(@RequestParam(required=false)String extId, HttpServletResponse response) {
		List<DeliveryScheme> schemes = deliverySchemeService.getAll();
		List<Map<String, Object>> mapList = Lists.newArrayList();
		for (DeliveryScheme scheme: schemes){
			 Map<String, Object> map = Maps.newHashMap();
			 map.put("id", scheme.getId());
			 map.put("pId", "O_-1");
			 map.put("name", scheme.getName());
			 mapList.add(map);
		}
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", "O_-1");
		map.put("pId", "O_-2");
		map.put("name", "配送方案");
		mapList.add(map);
		return mapList;
	}
	
	//查询条件
	private void initQc(DeliveryScheme deliveryScheme, Criteria c) {
		if (StringUtils.isNotBlank(deliveryScheme.getName())) {
			c.andLike("name", deliveryScheme.getName());
		}
	}
}