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
import com.tmt.shop.entity.Complex;
import com.tmt.shop.entity.ComplexProduct;
import com.tmt.shop.service.ComplexServiceFacade;

/**
 * 商品组合 管理
 * @author 超级管理员
 * @date 2016-11-28
 */
@Controller("shopComplexController")
@RequestMapping(value = "${adminPath}/shop/complex")
public class ComplexController extends BaseController{
	
	@Autowired
	private ComplexServiceFacade complexService;
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Complex complex, Model model){
		return "/shop/ComplexList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param complex
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Complex complex, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(complex, c);
		return complexService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param complex
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Complex complex, Model model) {
	    if(complex != null && !IdGen.isInvalidId(complex.getId())) {
		   complex = this.complexService.get(complex.getId());
		   complex.setProducts(this.complexService.queryRichProductsByComplexId(complex.getId()));
		} else {
		   if(complex == null) {
			  complex = new Complex();
		   }
		   complex.setType((byte)1);
		   complex.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("complex", complex);
		return "/shop/ComplexForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Complex complex, Model model, RedirectAttributes redirectAttributes) {
		List<ComplexProduct> products = WebUtils.fetchItemsFromRequest(null, ComplexProduct.class, "items.");
		complex.setProducts(products);
		this.complexService.save(complex);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改商品组合", complex.getName(), "成功"));
		redirectAttributes.addAttribute("id", complex.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/complex/form");
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
		List<Complex> complexs = Lists.newArrayList();
		for(Long id: idList) {
			Complex complex = new Complex();
			complex.setId(id);
			complexs.add(complex);
		}
		this.complexService.delete(complexs);
		return AjaxResult.success();
	}

	//查询条件
	private void initQc(Complex complex, Criteria c) {
        if(StringUtils.isNotBlank(complex.getName())) {
           c.andEqualTo("NAME", complex.getName());
        }
	}
}