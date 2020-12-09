package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.GoodsDelivery;

/**
 * 商品发货方案 管理
 * @author 超级管理员
 * @date 2016-11-06
 */
@Repository("shopGoodsDeliveryDao")
public class GoodsDeliveryDao extends BaseDaoImpl<GoodsDelivery,Long> {}
