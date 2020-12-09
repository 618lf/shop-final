package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.PaymentMethod;

/**
 * 支付方式 管理
 * @author 超级管理员
 * @date 2015-11-04
 */
@Repository("shopPaymentMethodDao")
public class PaymentMethodDao extends BaseDaoImpl<PaymentMethod,Long> {}