package com.tmt.bbs.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.bbs.dao.TopicReplyDao;
import com.tmt.bbs.entity.Hit;
import com.tmt.bbs.entity.Topic;
import com.tmt.bbs.entity.TopicReply;
import com.tmt.bbs.update.BbsModule;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.Sets;
import com.tmt.system.entity.UpdateData;
import com.tmt.system.entity.User;
import com.tmt.update.UpdateServiceFacade;

/**
 * 主题回复 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
@Service("bbsTopicReplyService")
public class ReplyService extends BaseService<TopicReply,Long> implements ReplyServiceFacade{
	
	@Autowired
	private TopicReplyDao topicReplyDao;
	@Autowired
	private ReplyHitService hitService;
	@Autowired
	private TopicHotService replysService;
	
	// 数据更新
	@Autowired
	private UpdateServiceFacade updateDataService;
	
	@Override
	protected BaseDao<TopicReply, Long> getBaseDao() {
		return topicReplyDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(TopicReply topicReply) {
		if(IdGen.isInvalidId(topicReply.getId())) {
			this.insert(topicReply);
		} else {
			this.update(topicReply);
			this._update(topicReply, (byte)0);
		}
	}
	
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<TopicReply> topicReplys) {
		this.batchDelete(topicReplys);
	}

	/**
	 * 回复
	 */
	@Override
	@Transactional
	public void reply(TopicReply reply) {
		this.insert(reply);
		this.replysService.reply(reply.getTopicId());
		
		// 回复
		this._update(reply, (byte)0);
		
		// 统计
		this._replyUpdate(reply, (byte)0);
	}
	
	/**
	 * 点赞
	 */
	@Override
	@Transactional
	public void hit(TopicReply reply) {
		Boolean hit = hitService.hit(reply);
		if (hit) {
			this._hitUpdate(reply, (byte)0);
		}
	}
	
	// 数据修改
	private void _update(TopicReply topic, byte opt) {
		UpdateData updateData = new UpdateData();
		updateData.setId(topic.getId());
		updateData.setModule(BbsModule.REPLY);
		updateData.setOpt(opt);
		updateDataService.save(updateData);
	}
	
	// 数据修改
	private void _hitUpdate(TopicReply topic, byte opt) {
		String id = new StringBuilder(topic.getId().toString()).append("0").toString();
		UpdateData updateData = new UpdateData();
		updateData.setId(Long.parseLong(id));
		updateData.setModule(BbsModule.REPLY_HOTSPOT);
		updateData.setOpt(opt);
		updateDataService.save(updateData);
	}
	
	// 数据修改
	private void _replyUpdate(TopicReply topic, byte opt) {
		String id = new StringBuilder(topic.getTopicId().toString()).append("0").toString();
		UpdateData updateData = new UpdateData();
		updateData.setId(Long.parseLong(id));
		updateData.setModule(BbsModule.TOPIC_HOTSPOT);
		updateData.setOpt(opt);
		updateDataService.save(updateData);
	}

	/**
	 * 用户是否回复过这个动态
	 */
	@Override
	public List<Hit> user_replys(User user, List<Long> topics) {
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria();
		c.andIn("TOPIC_ID", topics);
		c.andEqualTo("CREATE_ID", user.getId());
		List<Long> hits = this.queryForGenericsList("queryUserReplys", qc);
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
