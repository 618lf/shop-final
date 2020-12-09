package com.tmt.bbs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.bbs.dao.TopicHotDao;
import com.tmt.bbs.entity.Topic;
import com.tmt.bbs.entity.TopicHot;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.service.BaseService;

/**
 * 主题点赞数 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
@Service("bbsTopicHotService")
public class TopicHotService extends BaseService<TopicHot,Long> {
	
	@Autowired
	private TopicHotDao topicHotDao;
	
	@Override
	protected BaseDao<TopicHot, Long> getBaseDao() {
		return topicHotDao;
	}
	
	/**
	 * 点赞
	 * @param topic
	 */
	@Transactional
	public void hit(Topic topic) {
		TopicHot hot = new TopicHot();
		hot.setId(topic.getId()); hot.setHits(1);
		this.topicHotDao.hit(hot);
	}
	
	/**
	 * 点赞
	 * @param topic
	 */
	@Transactional
	public void reply(Long topicId) {
		TopicHot hot = new TopicHot();
		hot.setId(topicId); hot.setReplys(1);
		this.topicHotDao.reply(hot);
	}
	
	/**
	 * 生成0数据
	 * @param topic
	 */
	@Transactional
	public void touch(Topic topic){
		TopicHot hot = new TopicHot();
		hot.setId(topic.getId());
		this.topicHotDao.touch(hot);
	}
}
