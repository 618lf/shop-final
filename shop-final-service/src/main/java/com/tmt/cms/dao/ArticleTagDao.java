package com.tmt.cms.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.cms.entity.ArticleTag;
import com.tmt.core.persistence.BaseDaoImpl;

/**
 * 文章标签 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
@Repository("cmsArticleTagDao")
public class ArticleTagDao extends BaseDaoImpl<ArticleTag,Long> {}
