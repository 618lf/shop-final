package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.Category;

/**
 * 商品分类 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Repository("shopCategoryDao")
public class CategoryDao extends BaseDaoImpl<Category,Long> {}
