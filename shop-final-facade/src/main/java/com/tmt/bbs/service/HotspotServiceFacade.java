package com.tmt.bbs.service;

import java.util.List;

import com.tmt.bbs.entity.ReplyHits;
import com.tmt.bbs.entity.TopicHot;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;

/**
 * 
 * @author lifeng
 */
public interface HotspotServiceFacade {
	
	
	/**
	 * 动态的热度
	 * @param topicId
	 * @return
	 */
	public TopicHot getTopicHot(Long topicId);
	
	/**
	 * 动态点击
	 * @param qc
	 * @return
	 */
	public List<TopicHot> queryForTopics(QueryCondition qc);
	
	public int countForTopics(QueryCondition qc);
	
	public Page pageForTopics(QueryCondition qc, PageParameters param);
	
	/**
	 * 回复点击
	 * @param qc
	 * @return
	 */
	public List<ReplyHits> queryForReplys(QueryCondition qc);
	
	public int countForReplys(QueryCondition qc);
	
	public Page pageForReplys(QueryCondition qc, PageParameters param);
	
}
