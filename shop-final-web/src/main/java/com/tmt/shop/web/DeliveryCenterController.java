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
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.DeliveryCenter;
import com.tmt.shop.service.DeliveryCenterServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 送货中心 管理
 * @author 超级管理员
 * @date 2016-01-20
 */
@Controller("shopDeliveryCenterController")
@RequestMapping(value = "${adminPath}/shop/deliveryCenter")
public class DeliveryCenterController extends BaseController{
	
	@Autowired
	private DeliveryCenterServiceFacade deliveryCenterService;
	
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(DeliveryCenter deliveryCenter, Model model){
		return "/shop/DeliveryCenterList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param deliveryCenter
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(DeliveryCenter deliveryCenter, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(deliveryCenter, c);
		return deliveryCenterService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param deliveryCenter
	 * @param model
	 */
	@RequestMapping("form")
	public String form(DeliveryCenter deliveryCenter, Model model) {
	    if(deliveryCenter != null && !IdGen.isInvalidId(deliveryCenter.getId())) {
		   deliveryCenter = this.deliveryCenterService.get(deliveryCenter.getId());
		} else {
		   if( deliveryCenter == null) {
			   deliveryCenter = new DeliveryCenter();
		   }
		   deliveryCenter.setId(IdGen.INVALID_ID);
		   deliveryCenter.setIsDefault(DeliveryCenter.NO);
		}
		model.addAttribute("deliveryCenter", deliveryCenter);
		return "/shop/DeliveryCenterForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(DeliveryCenter deliveryCenter, Model model, RedirectAttributes redirectAttributes) {
		deliveryCenter.userOptions(UserUtils.getUser());
		this.deliveryCenterService.save(deliveryCenter);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改送货中心", deliveryCenter.getName(), "成功"));
		redirectAttributes.addAttribute("id", deliveryCenter.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/deliveryCenter/form");
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
		List<DeliveryCenter> deliveryCenters = Lists.newArrayList();
		for(Long id:idList) {
			DeliveryCenter deliveryCenter = new DeliveryCenter();
			deliveryCenter.setId(id);
			deliveryCenters.add(deliveryCenter);
		}
		this.deliveryCenterService.delete(deliveryCenters);
		return AjaxResult.success();
	}
	


	//查询条件
	private void initQc(DeliveryCenter deliveryCenter, Criteria c) {
        if(StringUtils.isNotBlank(deliveryCenter.getName())) {
           c.andEqualTo("NAME", deliveryCenter.getName());
        }
        if(StringUtils.isNotBlank(deliveryCenter.getAreaName())) {
           c.andEqualTo("AREA_NAME", deliveryCenter.getAreaName());
        }
        if(StringUtils.isNotBlank(deliveryCenter.getContact())) {
           c.andEqualTo("CONTACT", deliveryCenter.getContact());
        }
        if(StringUtils.isNotBlank(deliveryCenter.getMobile())) {
           c.andEqualTo("MOBILE", deliveryCenter.getMobile());
        }
        if(StringUtils.isNotBlank(deliveryCenter.getPhone())) {
           c.andEqualTo("PHONE", deliveryCenter.getPhone());
        }
	}
    
    /**
	 * 树组件支持
	 */
	@ResponseBody
	@RequestMapping("treeSelect")
	public List<Map<String, Object>> treeSelect(@RequestParam(required=false)String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<DeliveryCenter> deliveryCenters = this.deliveryCenterService.getAll();
		for (int i=0; i< deliveryCenters.size(); i++){
			DeliveryCenter e = deliveryCenters.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", "O_-1");
			map.put("name", e.getName());
			mapList.add(map);
		}
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", "O_-1");
		map.put("pId", "O_-2");
		map.put("name", "送货中心");
		mapList.add(map);
		return mapList;
	}
	
	/**
	 * 表组件支持
	 */
	@RequestMapping(value = {"tableSelect"})
	public String tableSelect() {
	   return "/shop/DeliveryCenterTableSelect";
	}
}
