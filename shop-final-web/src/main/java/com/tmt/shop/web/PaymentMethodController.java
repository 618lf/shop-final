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
import com.tmt.shop.entity.PaymentMethod;
import com.tmt.shop.entity.PaymentMethod.Method;
import com.tmt.shop.entity.PaymentMethod.Type;
import com.tmt.shop.service.PaymentMethodServiceFacade;
import com.tmt.shop.utils.PaymentMethodUtils;
import com.tmt.system.utils.UserUtils;

/**
 * 支付方式 管理
 * @author 超级管理员
 * @date 2015-11-04
 */
@Controller("shopPaymentMethodController")
@RequestMapping(value = "${adminPath}/shop/paymentMethod")
public class PaymentMethodController extends BaseController{
	
	@Autowired
	private PaymentMethodServiceFacade paymentMethodService;
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(PaymentMethod paymentMethod, Model model){
		model.addAttribute("types", Type.values());
		model.addAttribute("methods", Method.values());
		return "/shop/PaymentMethodList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param paymentMethod
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(PaymentMethod paymentMethod, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(paymentMethod, c);
		qc.setOrderByClause("SORT");
		return paymentMethodService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param paymentMethod
	 * @param model
	 */
	@RequestMapping("form")
	public String form(PaymentMethod paymentMethod, Model model) {
	    if(paymentMethod != null && !IdGen.isInvalidId(paymentMethod.getId())) {
			paymentMethod = this.paymentMethodService.get(paymentMethod.getId());
		} else {
		   if( paymentMethod == null) {
			   paymentMethod = new PaymentMethod();
		   }
		   paymentMethod.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("types", Type.values());
		model.addAttribute("methods", Method.values());
		model.addAttribute("paymentMethod", paymentMethod);
		return "/shop/PaymentMethodForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(PaymentMethod paymentMethod, Model model, RedirectAttributes redirectAttributes) {
		paymentMethod.userOptions(UserUtils.getUser());
		this.paymentMethodService.save(paymentMethod);
		PaymentMethodUtils.clearCache();
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改支付方式", paymentMethod.getName(), "成功"));
		redirectAttributes.addAttribute("id", paymentMethod.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/paymentMethod/form");
	}
	
	/**
	 * 删除
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete")
	public AjaxResult delete(Long[] idList , Model model, HttpServletResponse response) {
		List<PaymentMethod> paymentMethods = Lists.newArrayList();
		for(Long id: idList) {
			PaymentMethod paymentMethod = new PaymentMethod();
			paymentMethod.setId(id);
			paymentMethods.add(paymentMethod);
		}
		this.paymentMethodService.delete(paymentMethods);
		PaymentMethodUtils.clearCache();
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
			List<PaymentMethod> menus = Lists.newArrayList();
			for(Map<String,String> map: maps) {
				PaymentMethod menu = new PaymentMethod();
				menu.setId(Long.parseLong(map.get("id")));
				menu.setSort(Integer.parseInt(map.get("sort")));
				menus.add(menu);
			}
			this.paymentMethodService.updateSort(menus);
			PaymentMethodUtils.clearCache();
			return AjaxResult.success();
		}
		return AjaxResult.error("没有需要保存的数据");
	}

	//查询条件
	private void initQc(PaymentMethod paymentMethod, Criteria c) {
        if(StringUtils.isNotBlank(paymentMethod.getName())) {
           c.andEqualTo("NAME", paymentMethod.getName());
        }
	}
}
