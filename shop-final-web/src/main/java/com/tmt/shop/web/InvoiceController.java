package com.tmt.shop.web;

import java.math.BigDecimal;
import java.util.Arrays;
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
import com.tmt.core.entity.ColumnMapper;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.BigDecimalUtil;
import com.tmt.core.utils.ExcelExpUtils;
import com.tmt.core.web.BaseController;
import com.tmt.shop.entity.Invoice;
import com.tmt.shop.entity.Order;
import com.tmt.shop.service.InvoiceServiceFacade;
import com.tmt.shop.service.OrderServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 发票申领 管理
 * @author 超级管理员
 * @date 2016-05-26
 */
@Controller("shopInvoiceController")
@RequestMapping(value = "${adminPath}/shop/invoice")
public class InvoiceController extends BaseController{
	
	@Autowired
	private InvoiceServiceFacade invoiceService;
	@Autowired
	private OrderServiceFacade orderService;
	
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Invoice invoice, Model model){
		return "/shop/InvoiceList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param invoice
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Invoice invoice, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(invoice, c);
		qc.setOrderByClause("CREATE_DATE DESC");
		return invoiceService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param invoice
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Invoice invoice, Model model) {
	    if(invoice != null && !IdGen.isInvalidId(invoice.getId())) {
			invoice = this.invoiceService.get(invoice.getId());
		} else {
		   if( invoice == null) {
			   invoice = new Invoice();
		   }
		   invoice.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("invoice", invoice);
		return "/shop/InvoiceForm";
	}
	
	/**
	 * 订单过来的开票
	 * @param invoice
	 * @param model
	 */
	@RequestMapping("order")
	public String order(Invoice invoice, Model model) {
		Order order = this.orderService.get(invoice.getOrderId());
		invoice.setOrderSns(order.getSn());
		invoice.setGoodsNum(order.getProductQuantity());
		invoice.setAmount(order.getAmountPaid());
		invoice.setCompany(order.getInvoiceTitle());
		invoice.setConsignee(order.getConsignee());
		invoice.setPhone(order.getPhone());
		invoice.setAddress(order.getAddress());
		invoice.setApplyId(order.getCreateId());
		invoice.setApplyName(order.getCreateName());
		model.addAttribute("invoice", invoice);
		return "/shop/InvoiceMinForm";
	}
	
	/**
	 * 订单-保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("order/save")
	public AjaxResult save(Invoice invoice) {
		invoice.userOptions(UserUtils.getUser());
		this.invoiceService.save(invoice, UserUtils.getUser());
		return AjaxResult.success();
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Invoice invoice, Model model, RedirectAttributes redirectAttributes) {
		//校验发票关联的订单的正确性
		String orderSns = invoice.getOrderSns();
		       orderSns = StringUtils.replaceP(orderSns, ",");
		String[] sns = orderSns.split(",");
		BigDecimal amount = BigDecimal.ZERO; boolean manualCheck = false;
		for(String sn: sns) {
			//自动校验订单
			Long orderId = null;
			if ((orderId = orderService.getIdBySn(sn)) == null) {
				addMessage(redirectAttributes, StringUtils.format("订单校验失败,订单号不存在.不存在的订单号为：%s", sn));
				redirectAttributes.addAttribute("id", invoice.getId());
				return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/invoice/form");
			}
			Order order = orderService.get(orderId);
			amount = BigDecimalUtil.add(amount, order.getAmountPaid());
		}
		//自动校验才能验证
		if(!manualCheck) {
			if (!BigDecimalUtil.valueEqual(amount, invoice.getAmount())) {
				addMessage(redirectAttributes, StringUtils.format("订单校验失败,订单实际支付金额和发票申请金额不一致,订单实际支付金额为：%s", amount.toPlainString()));
				redirectAttributes.addAttribute("id", invoice.getId());
				return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/invoice/form");
			}
		}
		if(Invoice.YES == invoice.getStatus() && BigDecimalUtil.equalZERO(invoice.getAmount())) {
			addMessage(redirectAttributes, "订单校验失败,订单不能设置为已开票，订单金额为0，请确认");
			redirectAttributes.addAttribute("id", invoice.getId());
			return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/invoice/form");
		}
		this.invoiceService.save(invoice, UserUtils.getUser());
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改发票申领", invoice.getOrderSns(), "成功"));
		redirectAttributes.addAttribute("id", invoice.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/invoice/form");
	}
	
	/**
	 * 发票 - 发送（电子发票才会发送消息-- 暂时）
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("send")
	public AjaxResult send(Invoice invoice) {
		invoice = this.invoiceService.get(invoice.getId());
		if(invoice != null && Invoice.YES == invoice.getStatus()
				&& StringUtils.isNotBlank(invoice.getInvoiceUrl()) ) {
		   String[] sns = invoice.getOrderSns().split(",");
		   invoice.setOrderId(this.orderService.getIdBySn(sns[0]));
		   this.invoiceService.send(invoice);
		   return AjaxResult.success();
		}
		return AjaxResult.error("只有已开票的电子发票才能发送消息,纸质发票暂时不能发送消息提醒");
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
		List<Invoice> invoices = Lists.newArrayList();
		for(Long id: idList) {
			Invoice invoice = new Invoice();
			invoice.setId(id);
			invoices.add(invoice);
		}
		this.invoiceService.delete(invoices);
		return AjaxResult.success();
	}

	//查询条件
	private void initQc(Invoice invoice, Criteria c) {
        if(StringUtils.isNotBlank(invoice.getOrderSns())) {
           c.andLike("ORDER_SNS", invoice.getOrderSns());
        }
        if(StringUtils.isNotBlank(invoice.getCreateName())) {
           c.andLike("CREATE_NAME", invoice.getCreateName());
        }
        if(StringUtils.isNotBlank(invoice.getApplyName())) {
           c.andLike("APPLY_NAME", invoice.getApplyName());
        }
        if(StringUtils.isNotBlank(invoice.getCompany())) {
           c.andLike("COMPANY", invoice.getCompany());
        }
        if(invoice.getStatus() != null) {
           c.andEqualTo("STATUS", invoice.getStatus());
        }
        if(invoice.getSend() != null) {
           c.andEqualTo("SEND", invoice.getSend());
        }
	}

	// 导出
	public Map<String, Object> doExport(Invoice param, HttpServletRequest request) {
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria(); this.initQc(param, c);
		qc.setOrderByClause("CREATE_DATE DESC");
		String[] ids = request.getParameterValues("export.idList");
		if(null != ids && ids.length != 0){
		   List<String> idLst = Lists.newArrayList();
		   idLst = Arrays.asList(ids);
		   c.andIn("ID", idLst);
		}
		List<Invoice> invoices = this.invoiceService.queryByCondition(qc);
		List<Map<String, Object>> values = Lists.newArrayList();
		for(Invoice invoice: invoices) {
			Map<String, Object> value = Maps.newHashMap();
			value.put("company", invoice.getCompany());
			value.put("taxpayerNumber", invoice.getTaxpayerNumber());
			value.put("consignee", invoice.getConsignee());
			value.put("phone", invoice.getPhone());
			value.put("address", invoice.getAddress());
			value.put("amount", BigDecimalUtil.toString(invoice.getAmount(), 2));
			values.add(value);
		}
		List<ColumnMapper> columns = Lists.newArrayList();
		columns.add(ColumnMapper.buildExpMapper("单位名称","B", DataType.STRING, "company"));
		columns.add(ColumnMapper.buildExpMapper("纳税人识别号","C", DataType.STRING, "taxpayerNumber"));
		columns.add(ColumnMapper.buildExpMapper("发票金额","G", DataType.STRING, "amount"));
		columns.add(ColumnMapper.buildExpMapper("收货人姓名","D", DataType.STRING, "consignee"));
		columns.add(ColumnMapper.buildExpMapper("手机","E", DataType.STRING, "phone"));
		columns.add(ColumnMapper.buildExpMapper("收货地址","F", DataType.STRING, "address"));
		return ExcelExpUtils.buildExpParams("发票申领导出", "发票申领导出", columns, values);
	}
}