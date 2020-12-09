package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.UserOrders;

/**
 * 下单统计 管理
 * @author 超级管理员
 * @date 2016-12-08
 */
@Repository("shopUserOrdersDao")
public class UserOrdersDao extends BaseDaoImpl<UserOrders,Long> {}
