package com.tmt.bbs.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.bbs.entity.Topic;
import com.tmt.core.persistence.BaseDaoImpl;

/**
 * 主题 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
@Repository("bbsTopicDao")
public class TopicDao extends BaseDaoImpl<Topic,Long> {}
