package com.tmt.shop.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.entity.AjaxResult;
import com.tmt.core.web.BaseController;
import com.tmt.shop.service.DeliveryNotesServiceFacade;

/**
 * 物流信息 管理
 * @author 超级管理员
 * @date 2016-03-12
 */
@Controller("shopDeliveryNotesController")
@RequestMapping(value = "${adminPath}/shop/delivery/notes")
public class DeliveryNotesController extends BaseController{
	
	@Autowired
	private DeliveryNotesServiceFacade deliveryNotesService;
	
	/**
	 * 快递信息(调用接口获取数据)
	 * @param corpCode
	 * @param trackingNo
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/{corpCode}/{trackingNo}")
	public AjaxResult list(@PathVariable String corpCode,@PathVariable String trackingNo) {
		return AjaxResult.success(this.deliveryNotesService.findByCorpTrackingNo(corpCode, trackingNo));
	}
}