package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.GoodsAttribute;

/**
 * 产品属性 管理
 * @author 超级管理员
 * @date 2016-01-23
 */
@Repository("shopGoodsAttributeDao")
public class GoodsAttributeDao extends BaseDaoImpl<GoodsAttribute,Long> {}
