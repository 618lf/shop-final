package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.Returns;

/**
 * 退货管理 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Repository("shopReturnsDao")
public class ReturnsDao extends BaseDaoImpl<Returns,Long> {}