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
import com.tmt.shop.entity.PaymentShiopingMethod;
import com.tmt.shop.entity.ShippingMethod;
import com.tmt.shop.entity.ShippingMethodArea;
import com.tmt.shop.service.ShippingMethodServiceFacade;
import com.tmt.shop.utils.ShippingMethodUtils;
import com.tmt.system.utils.UserUtils;

/**
 * 配送方式 管理
 * @author 超级管理员
 * @date 2016-01-20
 */
@Controller("shopShippingMethodController")
@RequestMapping(value = "${adminPath}/shop/shippingMethod")
public class ShippingMethodController extends BaseController{
	
	@Autowired
	private ShippingMethodServiceFacade shippingMethodService;
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(ShippingMethod shippingMethod, Model model){
		return "/shop/ShippingMethodList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param shippingMethod
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(ShippingMethod shippingMethod, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(shippingMethod, c);
		qc.setOrderByClause("SORT");
		return shippingMethodService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param shippingMethod
	 * @param model
	 */
	@RequestMapping("form")
	public String form(ShippingMethod shippingMethod, Model model) {
	    if (shippingMethod != null && !IdGen.isInvalidId(shippingMethod.getId())) {
			shippingMethod = this.shippingMethodService.getWithAreas(shippingMethod.getId());
		} else {
		   if( shippingMethod == null) {
			   shippingMethod = new ShippingMethod();
		   }
		   shippingMethod.setId(IdGen.INVALID_ID);
		}
	    shippingMethod.setPaymentMethods(this.shippingMethodService.queryByShiopingMethodId(shippingMethod.getId()));
		model.addAttribute("shippingMethod", shippingMethod);
		return "/shop/ShippingMethodForm";
	}
	
	/**
	 * 选择区域
	 * @return
	 */
	@RequestMapping("area_form")
	public String area_form() {
		return "/shop/ShippingMethodAreaForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(ShippingMethod shippingMethod, Model model, RedirectAttributes redirectAttributes) {
		List<PaymentShiopingMethod> paymentMethods = WebUtils.fetchItemsFromRequest(null, PaymentShiopingMethod.class, "items.");
		shippingMethod.setPaymentMethods(paymentMethods);
		List<ShippingMethodArea> areas = WebUtils.fetchItemsFromRequest(null, ShippingMethodArea.class, "areas.");
		List<ShippingMethodArea> _areas = Lists.newArrayList();
		for(ShippingMethodArea area: areas) {
			if (StringUtils.isNotBlank(area.getAreaIds())) {
				_areas.add(area);
			}
		}
		shippingMethod.setAreas(_areas);
		shippingMethod.userOptions(UserUtils.getUser());
		this.shippingMethodService.save(shippingMethod);
		ShippingMethodUtils.clearCache();
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改配送方式", shippingMethod.getName(), "成功"));
		redirectAttributes.addAttribute("id", shippingMethod.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/shippingMethod/form");
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
		List<ShippingMethod> shippingMethods = Lists.newArrayList();
		for(Long id: idList) {
			ShippingMethod shippingMethod = new ShippingMethod();
			shippingMethod.setId(id);
			shippingMethods.add(shippingMethod);
		}
		this.shippingMethodService.delete(shippingMethods);
		ShippingMethodUtils.clearCache();
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
		if (maps != null && maps.size() != 0){
			List<ShippingMethod> menus = Lists.newArrayList();
			for(Map<String,String> map: maps) {
				ShippingMethod menu = new ShippingMethod();
				menu.setId(Long.parseLong(map.get("id")));
				menu.setSort(Integer.parseInt(map.get("sort")));
				menus.add(menu);
			}
			this.shippingMethodService.updateSort(menus);
			ShippingMethodUtils.clearCache();
			return AjaxResult.success();
		}
		return AjaxResult.error("没有需要保存的数据");
	}

	//查询条件
	private void initQc(ShippingMethod shippingMethod, Criteria c) {
        if(StringUtils.isNotBlank(shippingMethod.getName())) {
           c.andEqualTo("NAME", shippingMethod.getName());
        }
	}
}
