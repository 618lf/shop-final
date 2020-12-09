package com.tmt.shop.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tmt.shop.service.DeliveryCenterServiceFacade;
import com.tmt.shop.service.DeliveryTemplateServiceFacade;

/**
 * 打印管理 管理
 * @author 超级管理员
 * @date 2015-11-04
 */
@Controller("shopPrintController")
@RequestMapping(value = "${adminPath}/shop/print")
public class PrintController {

	@Autowired
	private DeliveryCenterServiceFacade centerService;
	@Autowired
	private DeliveryTemplateServiceFacade templateService;
	
	/**
	 * 批量打印订单 -- 导出PDF
	 * @return
	 */
	@RequestMapping("batch_order")
	public String batchOrder(Long[] idList, Model model) {
		model.addAttribute("templates", templateService.getAll());
		model.addAttribute("centers", centerService.getAll());
		model.addAttribute("orders", idList);
		return "/shop/OrderBatchPrintDelivery"; 
	}
}