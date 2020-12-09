package com.tmt.bbs.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.bbs.dao.TopicHitDao;
import com.tmt.bbs.entity.Hit;
import com.tmt.bbs.entity.Topic;
import com.tmt.bbs.entity.TopicHit;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.Sets;
import com.tmt.system.entity.User;

/**
 * 主题点赞 管理
 * 
 * @author 超级管理员
 * @date 2017-04-12
 */
@Service("bbsTopicHitService")
public class TopicHitService extends BaseService<TopicHit, Long> implements TopicHitServiceFacade{

	@Autowired
	private TopicHitDao topicHitDao;
	@Autowired
	private TopicHotService hitsService;

	@Override
	protected BaseDao<TopicHit, Long> getBaseDao() {
		return topicHitDao;
	}

	/**
	 * 保存
	 */
	@Transactional
	public void save(TopicHit topicHit) {
		if (IdGen.isInvalidId(topicHit.getId())) {
			this.insert(topicHit);
		} else {
			this.update(topicHit);
		}
	}

	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<TopicHit> topicHits) {
		this.batchDelete(topicHits);
	}

	/**
	 * 点赞此动态
	 * 
	 * @param topic
	 * @return
	 */
	@Transactional
	public Boolean hit(Topic topic) {
		TopicHit hit = new TopicHit();
		hit.setTopicId(topic.getId());
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
	public Boolean hited(TopicHit hit) {
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
		c.andIn("TOPIC_ID", topics);
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
}
