package com.tmt.bbs.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.bbs.entity.TopicHot;
import com.tmt.core.persistence.BaseDaoImpl;

/**
 * 主题点赞数 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
@Repository("bbsTopicHitsDao")
public class TopicHotDao extends BaseDaoImpl<TopicHot,Long> {
	
	public void hit(TopicHot hot) {
		this.insert("insertHit", hot);
	}
	
	public void reply(TopicHot hot) {
		this.insert("insertReply", hot);
	}
	
	public void touch(TopicHot hot) {
		this.insert("touch", hot);
	}
}
