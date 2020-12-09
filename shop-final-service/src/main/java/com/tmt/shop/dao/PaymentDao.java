package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.Payment;

/**
 * 收款 管理
 * @author 超级管理员
 * @date 2015-11-05
 */
@Repository("shopPaymentDao")
public class PaymentDao extends BaseDaoImpl<Payment,Long> {}