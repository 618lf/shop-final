package com.tmt.bbs.service;

import java.util.List;

import com.tmt.bbs.entity.Hit;
import com.tmt.bbs.entity.TopicReply;
import com.tmt.core.service.BaseServiceFacade;
import com.tmt.system.entity.User;

/**
 * 回复
 * 
 * @author lifeng
 */
public interface ReplyServiceFacade extends BaseServiceFacade<TopicReply, Long> {

	
	/**
	 * 点赞当前动态
	 * 
	 * @param topic
	 */
	public void hit(TopicReply reply);
	
	/**
	 * 回复
	 * 
	 * @param reply
	 */
	public void reply(TopicReply reply);
	
	/**
	 * 用户回复状态
	 * @param user
	 * @param topics
	 * @return
	 */
	public List<Hit> user_replys(User user, List<Long> topics);
}
