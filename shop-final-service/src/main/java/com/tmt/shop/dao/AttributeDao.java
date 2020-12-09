package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.Attribute;

/**
 * 商品属性 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Repository("shopAttributeDao")
public class AttributeDao extends BaseDaoImpl<Attribute,Long> {}