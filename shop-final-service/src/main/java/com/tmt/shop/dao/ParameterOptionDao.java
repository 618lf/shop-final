package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.ParameterOption;

/**
 * 商品参数选项 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Repository("shopParameterOptionDao")
public class ParameterOptionDao extends BaseDaoImpl<ParameterOption,Long> {}