package com.tmt.shop.web;

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
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.Payment;
import com.tmt.shop.service.OrderServiceFacade;
import com.tmt.shop.service.PaymentMethodServiceFacade;
import com.tmt.shop.service.PaymentServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 收款 管理
 * @author 超级管理员
 * @date 2015-11-05
 */
@Controller("shopPaymentController")
@RequestMapping(value = "${adminPath}/shop/payment")
public class PaymentController extends BaseController{
	
	@Autowired
	private PaymentMethodServiceFacade paymentMethodService;
	@Autowired
	private PaymentServiceFacade paymentService;
	@Autowired
	private OrderServiceFacade orderService;
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Payment payment, Model model){
		return "/shop/PaymentList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param payment
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Payment payment, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(payment, c);
		return paymentService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param payment
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Payment payment, Model model) {
	    if (payment != null && !IdGen.isInvalidId(payment.getId())) {
			payment = this.paymentService.get(payment.getId());
		} else {
		   if( payment == null) {
			   payment = new Payment();
		   }
		   payment.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("payment", payment);
		return "/shop/PaymentForm";
	}
	
	/**
	 * 订单-收款(一般手动收款)
	 * @param payment
	 * @param model
	 */
	@RequestMapping("order")
	public String order(Payment payment, Model model) {
		if (payment != null && !IdGen.isInvalidId(payment.getId())) {
		    payment = this.paymentService.get(payment.getId());
		}
		Order order = orderService.get(payment.getOrderId());
		if (order != null && order.isPayable()) {
			payment.setAmount(order.getAnountPayAble());
			payment.setPayer(order.getCreateName());
			payment.setAccount("默认账户");
			model.addAttribute("payment", payment);
			model.addAttribute("order", order);
			model.addAttribute("paymentMethods", paymentMethodService.queryPaymentMethods());
		} 
		return "/shop/PaymentMinForm";
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
	public AjaxResult save(Payment payment) {
		payment.userOptions(UserUtils.getUser());
		payment.setModule(Payment.order_module);
		this.paymentService.manualPayment(payment, UserUtils.getUser());
		return AjaxResult.success();
	}
	
	//查询条件
	private void initQc(Payment payment, Criteria c) {
        if (StringUtils.isNotBlank(payment.getCreateName())) {
        	c.andLike("CREATE_NAME", payment.getCreateName());
        }
        if (payment.getPayFlag() != null) {
        	c.andEqualTo("PAY_FLAG", payment.getPayFlag());
        }
	}
}
