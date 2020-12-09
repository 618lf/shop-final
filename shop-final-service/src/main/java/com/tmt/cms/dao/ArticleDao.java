package com.tmt.cms.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.cms.entity.Article;
import com.tmt.core.persistence.BaseDaoImpl;

/**
 * 文章管理 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
@Repository("cmsArticleDao")
public class ArticleDao extends BaseDaoImpl<Article,Long> {}
