package com.tmt.cms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.cms.dao.ArticleDao;
import com.tmt.cms.dao.ArticleDataDao;
import com.tmt.cms.entity.Article;
import com.tmt.cms.entity.ArticleData;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;

/**
 * 文章管理 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
@Service("cmsArticleService")
public class ArticleService extends BaseService<Article,Long> implements ArticleServiceFacade{
	
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private ArticleDataDao dataDao;
	
	@Override
	protected BaseDao<Article, Long> getBaseDao() {
		return articleDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Article article) {
		if(StringUtils.isBlank(article.getRemarks())) {
		   article.setRemarks(StringUtils.abbrHtml(article.getContent(), 255));
		}
		if(IdGen.isInvalidId(article.getId())) {
			this.insert(article);
			this.dataDao.insert(article.getData());
		} else {
			this.update(article);
			this.dataDao.update(article.getData());
		}
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Article> articles) {
		this.batchDelete(articles);
		List<ArticleData> datas = Lists.newArrayList();
		for(Article article: articles) {
			datas.add(article.getData());
		}
		this.dataDao.batchDelete(datas);
	}
}