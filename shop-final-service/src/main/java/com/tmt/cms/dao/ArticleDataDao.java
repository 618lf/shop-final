package com.tmt.cms.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.cms.entity.ArticleData;
import com.tmt.core.persistence.BaseDaoImpl;

/**
 * 文章内容 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
@Repository("cmsArticleDataDao")
public class ArticleDataDao extends BaseDaoImpl<ArticleData,Long> {}
