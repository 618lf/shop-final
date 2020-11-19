package com.tmt.shop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.ScorePage;

/**
 * 商品查找
 * @author root
 */
@Service
public interface GoodsSearcherFacade {

	/**
	 * 关键字查询
	 * @param query
	 * @return
	 */
	public Page searchText(String query, ScorePage page);
	
	/**
	 * 分类查询
	 * @param query
	 * @return
	 */
	public Page searchByCategory(String categoryId, ScorePage page);
	
	
	/**
	 * 首页数据
	 * @param query
	 * @return
	 */
	public Page index_goods(ScorePage page);
	
	
	/**
	 * 重建索引
	 */
	public void goodss_build();

	/**
	 * 刷新索引(批量更新，效率更高)
	 */
	public void refresh(List<Long> updates);
	
	/**
	 * 删除
	 * @param deletes
	 */
	public void _delete(List<Long> deletes);
}