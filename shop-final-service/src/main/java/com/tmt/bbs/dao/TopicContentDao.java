package com.tmt.bbs.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.bbs.entity.TopicContent;
import com.tmt.core.persistence.BaseDaoImpl;

/**
 * 主题内容 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
@Repository("bbsTopicContentDao")
public class TopicContentDao extends BaseDaoImpl<TopicContent,Long> {}
