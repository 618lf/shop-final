package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.ProductAppraise;

/**
 * 商品评价 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
@Repository("shopProductAppraiseDao")
public class ProductAppraiseDao extends BaseDaoImpl<ProductAppraise,Long> {}
