package com.tmt.bbs.service;

import java.util.List;

import com.tmt.bbs.entity.Hit;
import com.tmt.bbs.entity.TopicHit;
import com.tmt.core.service.BaseServiceFacade;
import com.tmt.system.entity.User;

/**
 * 点赞
 * @author lifeng
 */
public interface TopicHitServiceFacade extends BaseServiceFacade<TopicHit, Long>{

	/**
	 * 用户点击状态
	 * @param user
	 * @param topics
	 * @return
	 */
	public List<Hit> user_hits(User user, List<Long> topics);
}