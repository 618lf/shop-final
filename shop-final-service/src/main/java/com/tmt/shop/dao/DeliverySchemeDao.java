package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.DeliveryScheme;

/**
 * 配送方案 管理
 * @author 超级管理员
 * @date 2016-11-06
 */
@Repository("shopDeliverySchemeDao")
public class DeliverySchemeDao extends BaseDaoImpl<DeliveryScheme,Long> {}
