package com.tmt.bbs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tmt.bbs.entity.ReplyHits;
import com.tmt.bbs.entity.TopicHot;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;

/**
 * 热点
 * @author lifeng
 */
@Service
public class HotspotService implements HotspotServiceFacade{

	@Autowired
	private TopicHotService topicHotService;
	@Autowired
	private ReplyHitsService replyHitsService;
	
	
	/**
	 * 动态的热度
	 * @param topicId
	 * @return
	 */
	public TopicHot getTopicHot(Long topicId) {
		return topicHotService.get(topicId);
	}
	
	@Override
	public List<TopicHot> queryForTopics(QueryCondition qc) {
		return topicHotService.queryByCondition(qc);
	}
	
	@Override
	public int countForTopics(QueryCondition qc) {
		return topicHotService.countByCondition(qc);
	}
	
	@Override
	public Page pageForTopics(QueryCondition qc, PageParameters param) {
		return topicHotService.queryForPage(qc, param);
	}

	@Override
	public List<ReplyHits> queryForReplys(QueryCondition qc) {
		return replyHitsService.queryByCondition(qc);
	}
	
	@Override
	public int countForReplys(QueryCondition qc) {
		return replyHitsService.countByCondition(qc);
	}
	
	@Override
	public Page pageForReplys(QueryCondition qc, PageParameters param) {
		return replyHitsService.queryForPage(qc, param);
	}
}
