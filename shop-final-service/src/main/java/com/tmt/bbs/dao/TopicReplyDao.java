package com.tmt.bbs.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.bbs.entity.TopicReply;
import com.tmt.core.persistence.BaseDaoImpl;

/**
 * 主题回复 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
@Repository("bbsTopicReplyDao")
public class TopicReplyDao extends BaseDaoImpl<TopicReply,Long> {
	
}
