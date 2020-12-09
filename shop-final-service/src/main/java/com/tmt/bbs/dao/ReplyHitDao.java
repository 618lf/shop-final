package com.tmt.bbs.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.bbs.entity.ReplyHit;
import com.tmt.core.persistence.BaseDaoImpl;

/**
 * 回复点赞 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
@Repository("bbsReplyHitDao")
public class ReplyHitDao extends BaseDaoImpl<ReplyHit,Long> {}
