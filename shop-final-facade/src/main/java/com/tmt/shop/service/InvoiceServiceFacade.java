package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Invoice;
import com.tmt.system.entity.User;

/**
 * 发票申领 管理
 * @author 超级管理员
 * @date 2016-05-26
 */
public interface InvoiceServiceFacade extends BaseServiceFacade<Invoice,Long> {
	
	/**
	 * 保存
	 */
	public void save(Invoice invoice, User user);
	
	/**
	 * 保存
	 */
	public void send(Invoice invoice);
	
	/**
	 * 删除
	 */
	public void delete(List<Invoice> invoices);
	
}
