package com.tmt.shop.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.core.config.Globals;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.FreemarkerUtils;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.DeliveryCenter;
import com.tmt.shop.entity.DeliveryTemplate;
import com.tmt.shop.entity.Epay;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderEvent;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.Payment;
import com.tmt.shop.entity.PaymentMethod.Method;
import com.tmt.shop.entity.Product;
import com.tmt.shop.entity.Shipping;
import com.tmt.shop.enums.PayStatus;
import com.tmt.shop.event.EventHandler;
import com.tmt.shop.event.impl.NoticeEventHandler;
import com.tmt.shop.script.DeliveryScriptExecutor;
import com.tmt.shop.service.DeliveryCenterServiceFacade;
import com.tmt.shop.service.DeliveryTemplateServiceFacade;
import com.tmt.shop.service.OrderServiceFacade;
import com.tmt.shop.service.PaymentServiceFacade;
import com.tmt.shop.service.ProductServiceFacade;
import com.tmt.shop.service.ReturnsServiceFacade;
import com.tmt.shop.service.ShippingServiceFacade;
import com.tmt.shop.service.UserRankServiceFacade;
import com.tmt.shop.utils.EpayUtils;
import com.tmt.shop.utils.PaymentMethodUtils;
import com.tmt.shop.utils.ShippingMethodUtils;
import com.tmt.system.utils.UserUtils;
import com.tmt.wechat.entity.App;
import com.tmt.wechat.utils.WechatUtils;

/**
 * 订单管理 管理
 * @author 超级管理员
 * @date 2015-11-04
 */
@Controller("shopOrderController")
@RequestMapping(value = "${adminPath}/shop/order")
public class OrderController extends BaseController{
	
	@Autowired
	private OrderServiceFacade orderService;
	@Autowired
	private ShippingServiceFacade shippingService;
	@Autowired
	private ReturnsServiceFacade returnsService;
	@Autowired
	private DeliveryTemplateServiceFacade templateService;
	@Autowired
	private DeliveryCenterServiceFacade centerService;
	@Autowired
	private PaymentServiceFacade paymentService;
	@Autowired
	private ProductServiceFacade productService;
	@Autowired
	private UserRankServiceFacade userRankService;
	private DeliveryScriptExecutor scriptExecutor = new DeliveryScriptExecutor();
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Order order, Model model){
		return "/shop/OrderList";
	}
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("star_list")
	public String star_list(Order order, Model model){
		model.addAttribute("type", 2);
		return "/shop/OrderStarList";
	}
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("rights_list")
	public String rights_list(Order order, Model model){
		model.addAttribute("type", 3);
		return "/shop/OrderRightsList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param order
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Order order, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(order, c);
		qc.setOrderByClause("CREATE_DATE DESC");
		return orderService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param order
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Order order, Model model) {
		order = this.orderService.getWithOrderItems(order.getId());
		model.addAttribute("order", order);
		//会员信息
		model.addAttribute("member", UserUtils.getUser(order.getCreateId()));
		//会员积分信息
		model.addAttribute("userRank", userRankService.get(order.getCreateId()));
		//物流信息
		List<Shipping> shippings = shippingService.queryShippingsByOrderId(order.getId());
		model.addAttribute("shipping", shippings!= null&& shippings.size()>0?shippings.get(0):null);
		return "/shop/OrderForm";
	}
	
	/**
	 * 表单
	 * @param order
	 * @param model
	 */
	@RequestMapping("form/product")
	public String formProduct(Order order, Model model) {
		order = this.orderService.getWithOrderItems(order.getId());
		model.addAttribute("order", order);
		return "/shop/OrderProduct";
	}
	
	/**
	 * 表单
	 * @param order
	 * @param model
	 */
	@RequestMapping("form/payment")
	public String formPayment(Order order, Model model) {
		order = this.orderService.get(order.getId());
		if (order != null) {
			order.setPayments(this.paymentService.queryPaymentsByOrderId(order.getId()));
		}
		model.addAttribute("order", order);
		return "/shop/OrderPayment";
	}
	
	/**
	 * 表单
	 * @param order
	 * @param model
	 */
	@RequestMapping("form/refunds")
	public String formRefunds(Order order, Model model) {
		order = this.orderService.getWithRefunds(order.getId());
		model.addAttribute("order", order);
		return "/shop/OrderRefunds";
	}
	
	/**
	 * 表单
	 * @param order
	 * @param model
	 */
	@RequestMapping("form/shipping")
	public String formShippings(Order order, Model model) {
		model.addAttribute("order", order);
		model.addAttribute("shippings", this.shippingService.queryShippingsByOrderId(order.getId()));
		return "/shop/OrderShippings";
	}
	
	/**
	 * 表单
	 * @param order
	 * @param model
	 */
	@RequestMapping("form/returns")
	public String formReturns(Order order, Model model) {
		model.addAttribute("order", order);
		model.addAttribute("returns", this.returnsService.queryReturnsByOrderId(order.getId()));
		return "/shop/OrderReturns";
	}
	
	/**
	 * 新建一个订单
	 * @return
	 */
	@RequestMapping("create")
	public String create(Model model) {
		Order order = new Order(); order.setId(IdGen.INVALID_ID);
		model.addAttribute("order", order);
		model.addAttribute("paymentMethods", PaymentMethodUtils.getPaymentMethods());
		model.addAttribute("shippingMethods", ShippingMethodUtils.getShippingMethods());
		return "/shop/OrderCreate";
	}
	
	/**
	 * 保存 (创建订单时)
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Order order, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
		// 商品明细
		List<OrderItem> copys = Lists.newArrayList();
		List<OrderItem> items = WebUtils.fetchItemsFromRequest(request, OrderItem.class, "items.");
		for(OrderItem item: items) {
			Product product = this.productService.get(item.getProductId());
			if (product != null) {
				copys.add(OrderItem.build(product, item.getQuantity()));
			}
		}
		order.setItems(copys);
		
		// 支付方式 - 和配送方式
		order.initPaymentMethodObj(PaymentMethodUtils.getPaymentMethod(order.getPaymentMethod()));
	    order.initShippingMethodObj(ShippingMethodUtils.getShippingMethod(order.getShippingMethod()));
		
	    // 配送时间
	    String shippingAlert = scriptExecutor.calculateDeliveryTimes(order);
		order.setConsigneeTime(shippingAlert);
		
		// 构建订单
		Order.defaultOrder(order);
		
		// 计算运费
		order.setFreight(ShippingMethodUtils.calculateFreight(order));
		
		// 只能有一个APP
		String domain = request.getServerName();
		App app = WechatUtils.getDomainApp(domain);
		if (app != null) {
			order.setShopId(app.getId());
			String orderDesc = StringUtils.abbr(StringUtils.format("%s-%s", app.getName(), order.getProductName()), 128);
			order.setOrderDesc(orderDesc);
		}
		
		// 下订单
		this.orderService.book(order);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "新建订单", order.getSn(), "成功"));
		redirectAttributes.addAttribute("id", order.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/order/form");
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
	public AjaxResult delete(Long[] idList) {
		return AjaxResult.error("不提供删除操作");
	}
	
	/**
	 * 确认订单
	 * @param order
	 * @return
	 */
	@ResponseBody
	@RequestMapping("confirm")
	public AjaxResult confirm(Order order) {
		this.orderService.confirm(order, UserUtils.getUser());
		return AjaxResult.success();
	}
	
	/**
	 * 特殊处理
	 * @param order
	 * @return
	 */
	@ResponseBody
	@RequestMapping("special")
	public AjaxResult special(Order order) {
		this.orderService.updateSpecial(order, UserUtils.getUser());
		return AjaxResult.success();
	}
	
	/**
	 * 确认收货
	 * @param order
	 * @return
	 */
	@ResponseBody
	@RequestMapping("receipt")
	public AjaxResult receipt(Order order) {
		this.orderService.receipt(order, UserUtils.getUser());
		return AjaxResult.success();
	}
	
	/**
	 * 取消订单
	 * @param order
	 * @return
	 */
	@ResponseBody
	@RequestMapping("cancel")
	public AjaxResult cancel(Order order) {
		order = this.orderService.get(order.getId());
		this.orderService.cancel(order, UserUtils.getUser());
		return AjaxResult.success();
	}
	
	/**
	 * 完成订单
	 * @param order
	 * @return
	 */
	@ResponseBody
	@RequestMapping("complete")
	public AjaxResult complete(Order order) {
		order = this.orderService.get(order.getId());
		this.orderService.complete(order, UserUtils.getUser());
		return AjaxResult.success();
	}
	
	/**
	 * 校验订单 - 订单入账
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("check")
	public AjaxResult check(Order order) {
		order = this.orderService.get(order.getId());
		if (order != null && order.getPaymentMethodMethod() != null 
				&& order.getPaymentMethodMethod() == Method.ON_LINE) {
			List<Payment> payments = this.paymentService.queryPaymentsByOrderId(order.getId());
			for(Payment payment: payments) {
				Epay epay = EpayUtils.get(payment.getEpayId());
				Payment _payment = EpayUtils.wxCheckPayment(epay, payment);
				if (_payment != null) {
					if (order.getPaymentStatus() == PayStatus.unpaid || order.getPaymentStatus() == PayStatus.partialpaid) {
						this.paymentService.confirmPay(_payment);
					}
					return AjaxResult.success();
				}
			}
			return AjaxResult.error("网上对账没查到订单");
		}
		return AjaxResult.error("只有网上支付的才能对账");
	}
	
	/**
	 * 订单改类型
	 * @param order
	 * @return
	 */
	@ResponseBody
	@RequestMapping("mdtype")
	public AjaxResult mdtype(Order order) {
		this.orderService.mdtype(order);
		return AjaxResult.success(this.orderService.getType(order.getId()));
	}
	
	/**
	 * 订单改价格
	 * @param order
	 * @return
	 */
	@ResponseBody
	@RequestMapping("mdamount")
	public AjaxResult mdamount(Order order) {
		if(UserUtils.isPermitted("admin:order:md-amount")) {
		   this.orderService.mdamount(order, UserUtils.getUser());
		   return AjaxResult.success();
		}
		return AjaxResult.error("没有权限修改价格");
	}
	
	/**
	 * 订单改发票信息
	 * @param order
	 * @return
	 */
	@ResponseBody
	@RequestMapping("mdinvoice")
	public AjaxResult mdinvoice(Order order) {
		if(UserUtils.isPermitted("admin:order:md-invoice")) {
		   this.orderService.mdinvoice(order, UserUtils.getUser());
		   return AjaxResult.success();
		}
		return AjaxResult.error("没有权限修改发票信息");
	}
	
	/**
	 * 订单改发票信息
	 * @param order
	 * @return
	 */
	@ResponseBody
	@RequestMapping("mdshipping")
	public AjaxResult mdshipping(Order order) {
		if(UserUtils.isPermitted("admin:order:md-shipping")) {
		   this.orderService.mdshipping(order, UserUtils.getUser());
		   return AjaxResult.success();
		}
		return AjaxResult.error("没有权限修改发货信息");
	}
	
	/**
	 * 打印订单
	 * -- 来至订单的数据
	 * @return
	 */
	@RequestMapping("print")
	public String printOrder(Long id, Model model) {
		Order order = this.orderService.getWithOrderItems(id);
		model.addAttribute("order", order);
		model.addAttribute("member", UserUtils.getUser(order.getCreateId()));
		model.addAttribute("date", DateUtils.getTodayStr());
		return "/shop/OrderPrint";
	}
	
	/**
	 * 打印购货单
	 * -- 来至订单的数据
	 * @return
	 */
	@RequestMapping("print/goods")
	public String printGoods(Long id, Model model) {
		Order order = this.orderService.getWithOrderItems(id);
		model.addAttribute("order", order);
		model.addAttribute("member", UserUtils.getUser(order.getCreateId()));
		model.addAttribute("date", DateUtils.getTodayStr());
		return "/shop/OrderPrintGoods";
	}
	
	/**
	 * 打印发货单
	 * -- 来至发货的数据
	 * @return
	 */
	@RequestMapping("print/shipping")
	public String printShip(Long id, Model model) {
		Order order = this.orderService.get(id);
		List<Shipping> shippings = this.shippingService.queryShippingsByOrderId(order.getId());
		List<Shipping> copys = Lists.newArrayList();
		for(Shipping shipping: shippings) {
			Shipping _shipping = this.shippingService.getWithItems(shipping.getId());
			copys.add(_shipping);
		}
		model.addAttribute("order", order);
		model.addAttribute("shippings", copys);
		model.addAttribute("member", UserUtils.getUser(order.getCreateId()));
		model.addAttribute("date", DateUtils.getTodayStr());
		return "/shop/OrderPrintShipping";
	}
	
	/**
	 * 打印快递单
	 * @return
	 */
	@RequestMapping("print/delivery")
	public String printDelivery(Long id, Model model) {
		Order order = this.orderService.get(id);
		model.addAttribute("order", order);
		model.addAttribute("member", UserUtils.getUser(order.getCreateId()));
		model.addAttribute("date", DateUtils.getTodayStr());
		model.addAttribute("templates", templateService.getAll());
		model.addAttribute("centers", centerService.getAll());
		return "/shop/OrderPrintDelivery";
	}
	
	/**
	 * 打印快递单
	 * @return
	 */
	@ResponseBody
	@RequestMapping("print/delivery/render")
	public AjaxResult printDeliveryRender(Long id, Long templateId, Long centerId) {
		Map<String, Object> param = Maps.newHashMap();
		Order order = this.orderService.getWithOrderItems(id);
		param.put("order", order);
		param.put("member", UserUtils.getUser(order.getCreateId()));
		param.put("date", DateUtils.getTodayStr("yyyy/MM/dd"));
		param.put("date_y", DateUtils.getTodayStr("yyyy"));
		param.put("date_m", DateUtils.getTodayStr("MM"));
		param.put("date_d", DateUtils.getTodayStr("dd"));
		DeliveryTemplate deliveryTemplate = this.templateService.get(templateId);
		DeliveryCenter deliveryCenter = this.centerService.get(centerId);
		if (deliveryTemplate != null && deliveryCenter != null) {
		    param.put("center", deliveryCenter);
		    param.put("template", deliveryTemplate);
		    String template = StringUtils.format("<div class=\"template\" style=\"width:${template.width}px; height:${template.height}px;\">%s%s</div>", "[#if template.background??]<img src=\"${template.background}\">[/#if]", deliveryTemplate.getContent());
		    return AjaxResult.success(FreemarkerUtils.processNoTemplate(template, param));
		}
		return AjaxResult.error("错误", Boolean.FALSE);
	}
	
	/**
	 * 发送通知
	 * @return
	 */
	@ResponseBody
	@RequestMapping("notice/{id}")
	public AjaxResult notice(@PathVariable Long id, String type, String first, String remarks) {
		if(UserUtils.isPermitted("admin:order:send-notice")) {
		   if (id != null && type != null && StringUtils.isNotBlank(type)
				   && first != null && StringUtils.isNotBlank(first)
				   && remarks != null && StringUtils.isNotBlank(remarks)) {
			   EventHandler handler = new NoticeEventHandler(type, first, remarks);
			   OrderEvent event = new OrderEvent();
			   event.setOrderId(id);
			   handler.doHandler(event);
			   return AjaxResult.success("操作成功！");
		   }
		   return AjaxResult.error("消息不能为空");
		}
		return AjaxResult.error("没有权限");
	}
	
	/**
	 * 订单折扣
	 * @return
	 */
	@RequestMapping("discounts/{id}")
	public String discounts(@PathVariable Long id, Model model) {
		List<Map<String,Object>> discounts = this.orderService.discounts(id);
		model.addAttribute("discounts", discounts);
		return "/shop/OrderDiscounts"; 
	}

	//查询条件
	private void initQc(Order order, Criteria c) {
		if (order.getType() != null) {
			c.andEqualTo("TYPE", order.getType());
		}
		if (StringUtils.isNotBlank(order.getSn())) {
			c.andEqualTo("SN", order.getSn());
		}
		if (StringUtils.isNotBlank(order.getConsignee())) {
			c.andLike("CONSIGNEE", order.getConsignee());
		}
		if (StringUtils.isNotBlank(order.getCreateName())) {
			c.andLike("CREATE_NAME", order.getCreateName());
		}
		if (StringUtils.isNotBlank(order.getInvoiceTitle())) {
			c.andLike("INVOICE_TITLE", order.getInvoiceTitle());
		}
		if (StringUtils.isNotBlank(order.getRemarks())) {
			c.andLike("REMARKS", order.getRemarks());
		}
		//下单时间的查询
		if (order.getQueryStartDate() != null && order.getQueryEndDate() == null) {
			c.andDateGreaterThanOrEqualTo("CREATE_DATE", order.getQueryStartDate());
		} else if(order.getQueryStartDate() == null && order.getQueryEndDate() != null) {
			c.andDateLessThanOrEqualTo("CREATE_DATE", order.getQueryEndDate());
		} else if(order.getQueryStartDate() != null && order.getQueryEndDate() != null) {
			c.andDateBetween("CREATE_DATE", order.getQueryStartDate(), order.getQueryEndDate());
		}
		
		// 订单状态
		if (StringUtils.isNoneBlank(order.getFlowState())) {
			c.andEqualTo("FLOW_STATE", order.getFlowState());
		}
		if("1".equals(order.getQueryPaymethod())) {
			c.andEqualTo("PAYMENT_METHOD_NAME", "网上支付");
		}
		else if("6".equals(order.getQueryPaymethod())) {
			c.andEqualTo("PAYMENT_METHOD_NAME", "银行汇款");
		}
		else if("7".equals(order.getQueryPaymethod())) {
			c.andEqualTo("PAYMENT_METHOD_NAME", "货到付款");
		}
	}
}