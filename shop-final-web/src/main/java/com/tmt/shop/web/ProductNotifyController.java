package com.tmt.shop.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.OrderEvent;
import com.tmt.shop.entity.ProductNotify;
import com.tmt.shop.event.EventHandler;
import com.tmt.shop.event.impl.ProductOrderedEventHandler;
import com.tmt.shop.service.ProductNotifyServiceFacade;

/**
 * 到货通知 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Controller("shopProductNotifyController")
@RequestMapping(value = "${adminPath}/shop/productNotify")
public class ProductNotifyController extends BaseController{
	
	@Autowired
	private ProductNotifyServiceFacade productNotifyService;
	private EventHandler orderedEventHandler = new ProductOrderedEventHandler();
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(ProductNotify productNotify, Model model){
		return "/shop/ProductNotifyList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param productNotify
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(ProductNotify productNotify, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(productNotify, c);
		return productNotifyService.queryForPage(qc, param);
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
		List<ProductNotify> productNotifys = Lists.newArrayList();
		for(Long id: idList) {
			ProductNotify productNotify = new ProductNotify();
			productNotify.setId(id);
			productNotifys.add(productNotify);
		}
		this.productNotifyService.delete(productNotifys);
		return AjaxResult.success();
	}
	
	/**
	 * 发送通知
	 * @param idList
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("send")
	public AjaxResult send(Long[] idList, Model model, HttpServletResponse response) {
		for(Long id: idList) {
			OrderEvent event = new OrderEvent();
			event.setId(id);
			orderedEventHandler.doHandler(event);
		}
		return AjaxResult.success();
	}

	//查询条件
	private void initQc(ProductNotify productNotify, Criteria c) {
        if (productNotify.getProductName() != null) {
            c.andEqualTo("PRODUCT_NAME", productNotify.getProductName());
        }
        if (productNotify.getCreateName() != null) {
            c.andEqualTo("CREATE_NAME", productNotify.getCreateName());
        }
	}
}