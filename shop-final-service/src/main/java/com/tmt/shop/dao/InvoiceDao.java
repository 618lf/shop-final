package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.Invoice;

/**
 * 发票申领 管理
 * @author 超级管理员
 * @date 2016-05-26
 */
@Repository("shopInvoiceDao")
public class InvoiceDao extends BaseDaoImpl<Invoice,Long> {}