package com.tmt.cms.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.cms.entity.Category;
import com.tmt.core.persistence.BaseDaoImpl;

/**
 * 文章分类 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
@Repository("cmsCategoryDao")
public class CategoryDao extends BaseDaoImpl<Category,Long> {}
