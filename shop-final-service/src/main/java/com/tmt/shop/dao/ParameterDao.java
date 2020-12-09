package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.Parameter;

/**
 * 商品参数 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Repository("shopParameterDao")
public class ParameterDao extends BaseDaoImpl<Parameter,Long> {}