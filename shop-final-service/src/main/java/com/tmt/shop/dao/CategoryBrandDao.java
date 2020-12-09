package com.tmt.shop.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.shop.entity.CategoryBrand;

/**
 * 关联品牌 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Repository("shopCategoryBrandDao")
public class CategoryBrandDao extends BaseDaoImpl<CategoryBrand,Long> {}
