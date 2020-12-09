package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.OrderLog;

/**
 * 订单记录 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Repository("shopOrderLogDao")
public class OrderLogDao extends BaseDaoImpl<OrderLog,Long> {}