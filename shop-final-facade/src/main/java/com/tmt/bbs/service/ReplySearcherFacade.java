package com.tmt.bbs.service;

import java.util.List;

import com.tmt.bbs.entity.TopicReply;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.ScorePage;

public interface ReplySearcherFacade {

	/**
	 * 得到单个
	 * @param id
	 * @return
	 */
	public TopicReply get(Long id);
	
	/**
	 * 列表
	 * @param topicId
	 * @param page
	 * @return
	 */
	public Page page(Long topicId, ScorePage page);
	
	/**
	 * 前三
	 * @param topicId
	 * @param page
	 * @return
	 */
	public List<TopicReply> top3(Long topicId);
	
	
	/**
	 * 刷新索引(批量更新，效率更高)
	 */
	public void refresh(List<Long> updates);
	
	/**
	 * 索引重建
	 */
	public void replys_build();
	
	/**
	 * 删除
	 * @param deletes
	 */
	public void _delete(List<Long> deletes);
}
