package com.tmt.shop.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
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
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.DeliveryCorp;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.Product;
import com.tmt.shop.entity.ReturnItem;
import com.tmt.shop.entity.Returns;
import com.tmt.shop.entity.ShippingMethod;
import com.tmt.shop.service.DeliveryCorpServiceFacade;
import com.tmt.shop.service.OrderServiceFacade;
import com.tmt.shop.service.ProductServiceFacade;
import com.tmt.shop.service.ReturnsServiceFacade;
import com.tmt.shop.service.ShippingMethodServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 退货管理 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Controller("shopReturnsController")
@RequestMapping(value = "${adminPath}/shop/returns")
public class ReturnsController extends BaseController{
	
	@Autowired
	private ReturnsServiceFacade returnsService;
	@Autowired
	private OrderServiceFacade orderService;
	@Autowired
	private DeliveryCorpServiceFacade deliveryCorpService;
	@Autowired
	private ShippingMethodServiceFacade shippingMethodService;
	@Autowired
	private ProductServiceFacade productService;
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Returns returns, Model model){
		return "/shop/ReturnsList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param returns
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Returns returns, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(returns, c);
		return returnsService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param returns
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Returns returns, Model model) {
	    if(returns != null && !IdGen.isInvalidId(returns.getId())) {
			returns = this.returnsService.getWithItems(returns.getId());
		} else {
		   if( returns == null) {
			   returns = new Returns();
		   }
		   returns.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("returns", returns);
		return "/shop/ReturnsForm";
	}
	
	/**
	 * 订单-退货
	 * @param payment
	 * @param model
	 */
	@RequestMapping("order")
	public String order(Returns returns, Model model) {
		Order order = orderService.getWithOrderItems(returns.getOrderId());
		if (order != null) {
			List<OrderItem> items = order.getItems();
			for(OrderItem item: items) {
				Product product = this.productService.getStore(item.getProductId());
				item.setProductStore(product.getStore());
			}
			returns.setOrderSn(order.getSn());
			returns.setShipper(order.getConsignee());
			model.addAttribute("returns", returns);
			model.addAttribute("order", order);
			model.addAttribute("shippingMethods", shippingMethodService.queryShippingMethods());
			model.addAttribute("deliveryCorps", deliveryCorpService.queryDeliveryCorps());
		}
		return "/shop/ReturnsMinForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("save")
	public AjaxResult save(Returns returns, HttpServletRequest request) {
		Order order = this.orderService.getWithOrderItems(returns.getOrderId());
		ShippingMethod shippingMethod  = shippingMethodService.getShippingMethod(returns.getShippingMethodId());
		DeliveryCorp deliveryCorp = deliveryCorpService.get(returns.getDeliveryCorpId());
		returns.setShippingMethod(shippingMethod.getName());
		returns.setDeliveryCorp(deliveryCorp.getName());
		returns.setDeliveryCorpCode(deliveryCorp.getCode());
		returns.setDeliveryCorpUrl(deliveryCorp.getUrl());
		List<ReturnItem> items = WebUtils.fetchItemsFromRequest(request, ReturnItem.class, "sitems.");
		List<ReturnItem> copys = Lists.newArrayList();
		Integer quantity = 0;
		for(ReturnItem item: items) {
			OrderItem _item = null;
			if(item != null && StringUtils.isNotBlank(item.getSn()) && item.getQuantity() != null
			    && ((_item = order.getOrderItem(item.getItemId())) != null) && item.getQuantity() <= _item.getShippedQuantity() - _item.getReturnQuantity()) {
				copys.add(item);
			} else {
				return AjaxResult.error("申请退货出错");
			}
			quantity = Ints.addI(quantity, item.getQuantity());
		}
		if(copys.size() == 0 || quantity == 0) {
		   return AjaxResult.error("商品退货数量设置错误，请检查库存和已发货数量");
		}
		returns.setItems(copys);
		returns.userOptions(UserUtils.getUser());
		this.returnsService.save(order, UserUtils.getUser(), returns); 
		return AjaxResult.success();
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
		List<Returns> returnss = Lists.newArrayList();
		for(Long id: idList) {
			Returns returns = new Returns();
			returns.setId(id);
			returnss.add(returns);
		}
		this.returnsService.delete(returnss);
		return AjaxResult.success();
	}
	


	//查询条件
	private void initQc(Returns returns, Criteria c) {
        if(StringUtils.isNotBlank(returns.getOrderSn())) {
           c.andEqualTo("ORDER_SN", returns.getOrderSn());
        }
	}
}