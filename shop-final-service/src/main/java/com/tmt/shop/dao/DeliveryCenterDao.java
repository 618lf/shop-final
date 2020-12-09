package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.DeliveryCenter;

/**
 * 送货中心 管理
 * @author 超级管理员
 * @date 2016-01-20
 */
@Repository("shopDeliveryCenterDao")
public class DeliveryCenterDao extends BaseDaoImpl<DeliveryCenter,Long> {}
