package com.tmt.shop.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
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
import com.tmt.core.utils.JsonMapper;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.DeliveryCorp;
import com.tmt.shop.service.DeliveryCorpServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 物流公司 管理
 * @author 超级管理员
 * @date 2016-01-20
 */
@Controller("shopDeliveryCorpController")
@RequestMapping(value = "${adminPath}/shop/deliveryCorp")
public class DeliveryCorpController extends BaseController{
	
	@Autowired
	private DeliveryCorpServiceFacade deliveryCorpService;
	
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(DeliveryCorp deliveryCorp, Model model){
		return "/shop/DeliveryCorpList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param deliveryCorp
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(DeliveryCorp deliveryCorp, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(deliveryCorp, c);
		return deliveryCorpService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param deliveryCorp
	 * @param model
	 */
	@RequestMapping("form")
	public String form(DeliveryCorp deliveryCorp, Model model) {
	    if(deliveryCorp != null && !IdGen.isInvalidId(deliveryCorp.getId())) {
			deliveryCorp = this.deliveryCorpService.get(deliveryCorp.getId());
		} else {
		   if( deliveryCorp == null) {
			   deliveryCorp = new DeliveryCorp();
		   }
		   deliveryCorp.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("deliveryCorp", deliveryCorp);
		return "/shop/DeliveryCorpForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(DeliveryCorp deliveryCorp, Model model, RedirectAttributes redirectAttributes) {
		deliveryCorp.userOptions(UserUtils.getUser());
		this.deliveryCorpService.save(deliveryCorp);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改物流公司", deliveryCorp.getName(), "成功"));
		redirectAttributes.addAttribute("id", deliveryCorp.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/deliveryCorp/form");
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
		List<DeliveryCorp> deliveryCorps = Lists.newArrayList();
		for(Long id: idList) {
			DeliveryCorp deliveryCorp = new DeliveryCorp();
			deliveryCorp.setId(id);
			deliveryCorps.add(deliveryCorp);
		}
		this.deliveryCorpService.delete(deliveryCorps);
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
			List<DeliveryCorp> menus = Lists.newArrayList();
			for(Map<String,String> map: maps) {
				DeliveryCorp menu = new DeliveryCorp();
				menu.setId(Long.parseLong(map.get("id")));
				menu.setSort(Integer.parseInt(map.get("sort")));
				menus.add(menu);
			}
			this.deliveryCorpService.updateSort(menus);
			return AjaxResult.success();
		}
		return AjaxResult.error("没有需要保存的数据");
	}

	//查询条件
	private void initQc(DeliveryCorp deliveryCorp, Criteria c) {
        if(StringUtils.isNotBlank(deliveryCorp.getName())) {
           c.andEqualTo("NAME", deliveryCorp.getName());
        }
	}
    
    /**
	 * 树组件支持
	 */
	@ResponseBody
	@RequestMapping("treeSelect")
	public List<Map<String, Object>> treeSelect(@RequestParam(required=false)String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<DeliveryCorp> deliveryCorps = this.deliveryCorpService.getAll();
		for (int i=0; i< deliveryCorps.size(); i++){
			DeliveryCorp e = deliveryCorps.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", "O_-1");
			map.put("name", e.getName());
			mapList.add(map);
		}
		Map<String, Object> map = Maps.newHashMap();
		map.put("id", "O_-1");
		map.put("pId", "O_-2");
		map.put("name", "物流公司");
		mapList.add(map);
		return mapList;
	}
	
}
