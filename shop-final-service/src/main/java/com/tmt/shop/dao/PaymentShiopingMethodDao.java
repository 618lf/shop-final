package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.PaymentShiopingMethod;

/**
 * 支持的支付方式 管理
 * @author 超级管理员
 * @date 2016-01-20
 */
@Repository("shopPaymentShiopingMethodDao")
public class PaymentShiopingMethodDao extends BaseDaoImpl<PaymentShiopingMethod,Long> {}