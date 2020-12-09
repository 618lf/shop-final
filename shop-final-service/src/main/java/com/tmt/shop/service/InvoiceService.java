package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.shop.dao.InvoiceDao;
import com.tmt.shop.entity.Invoice;
import com.tmt.shop.entity.Order;
import com.tmt.system.entity.User;

/**
 * 发票申领 管理
 * @author 超级管理员
 * @date 2016-05-26
 */
@Service("shopInvoiceService")
public class InvoiceService extends BaseService<Invoice,Long> implements InvoiceServiceFacade{
	
	@Autowired
	private InvoiceDao invoiceDao;
	@Autowired
	private OrderService orderService;
	
	@Override
	protected BaseDaoImpl<Invoice, Long> getBaseDao() {
		return invoiceDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Invoice invoice, User user) {
		if(IdGen.isInvalidId(invoice.getId()) ) {
			this.insert(invoice);
		} else {
			this.update(invoice);
			//开票后将信息添加到订单中
			if(Invoice.YES == invoice.getStatus()) {
			   String orderSns = invoice.getOrderSns();
			   String[] sns = orderSns.split(",");
			   for(String sn: sns) {
				   Long orderId = orderService.getIdBySn(sn);
				   Order order = new Order();
				   order.setId(orderId);
				   order.setInvoiceTitle(invoice.getCompany());
				   order.setInvoiceUrl(invoice.getInvoiceUrl());
				   orderService.mdinvoice(order, user);
			   }
			}
		}
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void send(Invoice invoice) {
		invoice.setSend(Invoice.YES);
		this.update("updateSend", invoice);
	}
	
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Invoice> invoices) {
		this.batchDelete(invoices);
	}
}