package com.tmt.bbs.service;

import java.util.List;

import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.ScorePage;

/**
 * 主题数据
 * @author lifeng
 */
public interface TopicSearcherFacade {

	
	/**
	 * 得到数据 -- 详细的数据
	 * @param id
	 * @return
	 */
	public <T> T detail(Long id);
	
	/**
	 * 得到数据
	 * @param id
	 * @return
	 */
	public <T> T get(Long id);
	
	/**
	 * 产品最新评论
	 * @param sectionId
	 * @return
	 */
	public <T> T newestProduct(Long sectionId);
	
	/**
	 * 最新
	 * @param query
	 * @return
	 */
	public Page newestpage(ScorePage page);
	
	/**
	 * 产品
	 * @param query
	 * @return
	 */
	public Page productPage(ScorePage page);
	
	/**
	 * 主题列表
	 * @param query
	 * @return
	 */
	public Page productPage(Long sectionId, ScorePage page);
	
	/**
	 * 主题列表
	 * @param query
	 * @return
	 */
	public Page productPage(Long sectionId, String tag, ScorePage page);
	
	
	/**
	 * 刷新索引(批量更新，效率更高)
	 */
	public void refresh(List<Long> updates);
	
	/**
	 * 索引重建
	 */
	public void topics_build();
	
	/**
	 * 删除
	 * @param deletes
	 */
	public void _delete(List<Long> deletes);
	
	/**
	 * 产品的动态数目
	 * @return
	 */
	public int countProduct();
	
	/**
	 * 产品的动态数目
	 * @return
	 */
	public int countProduct(Long sectionId);
	
	/**
	 * 产品的动态数目
	 * @return
	 */
	public int countProduct(Long sectionId, String tag);
}
