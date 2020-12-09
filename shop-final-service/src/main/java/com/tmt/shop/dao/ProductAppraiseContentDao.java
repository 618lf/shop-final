package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.ProductAppraiseContent;

/**
 * 评价内容 管理
 * @author 超级管理员
 * @date 2017-04-10
 */
@Repository("shopProductAppraiseContentDao")
public class ProductAppraiseContentDao extends BaseDaoImpl<ProductAppraiseContent,Long> {}
