package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.Epayment;

/**
 * 企业支付 管理
 * @author 超级管理员
 * @date 2015-12-21
 */
@Repository("shopEpaymentDao")
public class EpaymentDao extends BaseDaoImpl<Epayment,Long> {}
