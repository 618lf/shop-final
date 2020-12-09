package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.Complex;

/**
 * 商品组合 管理
 * @author 超级管理员
 * @date 2016-11-28
 */
@Repository("shopComplexDao")
public class ComplexDao extends BaseDaoImpl<Complex,Long> {}
