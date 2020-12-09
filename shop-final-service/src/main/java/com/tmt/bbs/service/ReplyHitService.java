package com.tmt.bbs.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.bbs.dao.ReplyHitDao;
import com.tmt.bbs.entity.Hit;
import com.tmt.bbs.entity.ReplyHit;
import com.tmt.bbs.entity.Topic;
import com.tmt.bbs.entity.TopicReply;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.Sets;
import com.tmt.system.entity.User;

/**
 * 回复点赞 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
@Service("bbsReplyHitService")
public class ReplyHitService extends BaseService<ReplyHit,Long> implements ReplyHitServiceFacade{
	
	@Autowired
	private ReplyHitDao replyHitDao;
	@Autowired
	private ReplyHitsService hitsService;
	
	@Override
	protected BaseDao<ReplyHit, Long> getBaseDao() {
		return replyHitDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(ReplyHit replyHit) {
		if(IdGen.isInvalidId(replyHit.getId())) {
			this.insert(replyHit);
		} else {
			this.update(replyHit);
		}
	}
	
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<ReplyHit> replyHits) {
		this.batchDelete(replyHits);
	}
	
	/**
	 * 点赞此动态
	 * 
	 * @param topic
	 * @return
	 */
	@Transactional
	public Boolean hit(TopicReply topic) {
		ReplyHit hit = new ReplyHit();
		hit.setReplyId(topic.getId());
		hit.setCreateId(topic.getCreateId());
		if (!this.hited(hit)) {
			hit.setCreateName(topic.getCreateName());
			this.insert(hit);
			hitsService.hit(topic);
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	/**
	 * 是否点赞
	 * 
	 * @param hit
	 * @return
	 */
	public Boolean hited(ReplyHit hit) {
		int count = this.countByCondition("hited", hit);
		return count >= 1 ? Boolean.TRUE : Boolean.FALSE;
	}

	/**
	 * 用户点击
	 */
	@Override
	public List<Hit> user_hits(User user, List<Long> topics) {
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria();
		c.andIn("REPLY_ID", topics);
		c.andEqualTo("CREATE_ID", user.getId());
		List<Long> hits = this.queryForGenericsList("queryUserHits", qc);
		Set<Long> shits = Sets.newHashSet(hits);
		List<Hit> _hits = Lists.newArrayList();
		for(Long topic: topics) {
			Hit _hit = new Hit();
			_hit.setId(topic);
			if (shits.contains(topic)) {
				_hit.setHited(Topic.YES);
			}
			_hits.add(_hit);
		}
		return _hits;
	}
	
	
	@Override
	public Integer count_replyId(Long replyId){
		return this.countByCondition("count_replyId", replyId);
	}
}