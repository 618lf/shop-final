package com.tmt.bbs.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.bbs.entity.TopicHit;
import com.tmt.core.persistence.BaseDaoImpl;

/**
 * 主题点赞 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
@Repository("bbsTopicHitDao")
public class TopicHitDao extends BaseDaoImpl<TopicHit,Long> {}
