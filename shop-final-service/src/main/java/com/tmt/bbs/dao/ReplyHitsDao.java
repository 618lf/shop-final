package com.tmt.bbs.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.bbs.entity.ReplyHits;
import com.tmt.core.persistence.BaseDaoImpl;

/**
 * 回复点赞数 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
@Repository("bbsReplyHitsDao")
public class ReplyHitsDao extends BaseDaoImpl<ReplyHits,Long> {}
