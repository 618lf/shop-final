package com.tmt.bbs.service;

import java.util.List;

import com.tmt.bbs.entity.Hit;
import com.tmt.bbs.entity.ReplyHit;
import com.tmt.core.service.BaseServiceFacade;
import com.tmt.system.entity.User;

/**
 * 点赞
 * @author lifeng
 */
public interface ReplyHitServiceFacade extends BaseServiceFacade<ReplyHit,Long>{
	
	/**
	 * 用户点击状态
	 * @param user
	 * @param topics
	 * @return
	 */
	public List<Hit> user_hits(User user, List<Long> topics);
	
	/**
	 * 根据回复ID查看点击数
	 * @param replyId
	 * @return
	 */
	public Integer count_replyId(Long replyId);
}
