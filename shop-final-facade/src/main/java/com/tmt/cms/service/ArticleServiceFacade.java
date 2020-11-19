package com.tmt.cms.service;

import java.util.List;

import com.tmt.cms.entity.Article;
import com.tmt.core.service.BaseServiceFacade;

/**
 * 文章管理 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
public interface ArticleServiceFacade extends BaseServiceFacade<Article,Long> {
	
	/**
	 * 保存
	 */
	public void save(Article article);
	
	/**
	 * 删除
	 */
	public void delete(List<Article> articles);
}