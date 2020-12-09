package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.AttributeOption;

/**
 * 属性选项 管理
 * @author 超级管理员
 * @date 2016-01-23
 */
@Repository("shopAttributeOptionDao")
public class AttributeOptionDao extends BaseDaoImpl<AttributeOption,Long> {}