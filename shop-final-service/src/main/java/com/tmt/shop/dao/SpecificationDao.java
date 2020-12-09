package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.Specification;

/**
 * 商品规格 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Repository("shopSpecificationDao")
public class SpecificationDao extends BaseDaoImpl<Specification,Long> {}