package com.tmt.bbs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.bbs.dao.ReplyHitsDao;
import com.tmt.bbs.entity.ReplyHits;
import com.tmt.bbs.entity.TopicReply;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.service.BaseService;

/**
 * 回复点赞数 管理
 * 
 * @author 超级管理员
 * @date 2017-04-12
 */
@Service("bbsReplyHitsService")
public class ReplyHitsService extends BaseService<ReplyHits, Long> {

	@Autowired
	private ReplyHitsDao replyHitsDao;

	@Override
	protected BaseDao<ReplyHits, Long> getBaseDao() {
		return replyHitsDao;
	}

	/**
	 * 点赞
	 * 
	 * @param topic
	 */
	@Transactional
	public void hit(TopicReply topic) {
		ReplyHits hits = new ReplyHits();
		hits.setId(topic.getId()); hits.setHits(1); 
		this.insert(hits);
	}
}
