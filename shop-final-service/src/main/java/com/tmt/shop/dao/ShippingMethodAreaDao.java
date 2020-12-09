package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.ShippingMethodArea;

/**
 * 送货区域 管理
 * @author 超级管理员
 * @date 2017-02-06
 */
@Repository("shopShippingMethodAreaDao")
public class ShippingMethodAreaDao extends BaseDaoImpl<ShippingMethodArea,Long> {}
