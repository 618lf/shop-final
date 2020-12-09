package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.OrderPostage;

/**
 * 订单包邮 管理
 * @author 超级管理员
 * @date 2017-05-26
 */
@Repository("shopOrderPostageDao")
public class OrderPostageDao extends BaseDaoImpl<OrderPostage,Long> {}
