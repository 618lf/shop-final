package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.Epay;

/**
 * 支付账户 管理
 * @author 超级管理员
 * @date 2015-09-23
 */
@Repository("shopEpayDao")
public class EpayDao extends BaseDaoImpl<Epay,Long> {}
