package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.ComplexProduct;

/**
 * 商品组合 管理
 * @author 超级管理员
 * @date 2016-11-28
 */
@Repository("shopComplexProductDao")
public class ComplexProductDao extends BaseDaoImpl<ComplexProduct,Long> {}
