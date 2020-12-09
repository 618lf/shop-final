package com.tmt.bbs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.bbs.dao.TopicContentDao;
import com.tmt.bbs.dao.TopicDao;
import com.tmt.bbs.entity.Topic;
import com.tmt.bbs.entity.TopicContent;
import com.tmt.bbs.update.BbsModule;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;
import com.tmt.shop.entity.ProductAppraise;
import com.tmt.system.entity.UpdateData;
import com.tmt.update.UpdateServiceFacade;

/**
 * 主题 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
@Service("bbsTopicService")
public class TopicService extends BaseService<Topic,Long> implements TopicServiceFacade{
	
	@Autowired
	private TopicDao topicDao;
	@Autowired
	private TopicContentDao contentDao;
	@Autowired
	private TopicHitService hitService;
	@Autowired
	private TopicHotService hitsService;
	
	// 数据更新
	@Autowired
	private UpdateServiceFacade updateDataService;
	
	@Override
	protected BaseDao<Topic, Long> getBaseDao() {
		return topicDao;
	}
	
	/**
	 * 填充内容
	 */
	@Override
	public Topic fillContent(Topic topic) {
		TopicContent content = this.contentDao.get(topic.getId());
		topic.setTcontent(content);
		this._fillContent(topic);
		return topic;
	}


	/**
	 * 带出内容
	 * @param id
	 * @return
	 */
	public Topic getWithContent(Long id) {
		Topic topic = this.get(id); 
		if (topic != null) {
			TopicContent content = this.contentDao.get(id);
			topic.setTcontent(content);
			this._fillContent(topic);
		}
		return topic;
	}
	
	private void _fillContent(Topic topic) {
		TopicContent content = topic.getTcontent();
		List<String> images = Lists.newArrayList();
		if(StringUtils.isNotBlank(content.getImage())) {
			images.add(content.getImage());
		}
		if(StringUtils.isNotBlank(content.getImage2())) {
			images.add(content.getImage2());
		}
		if(StringUtils.isNotBlank(content.getImage3())) {
			images.add(content.getImage3());
		}
		if(StringUtils.isNotBlank(content.getImage4())) {
			images.add(content.getImage4());
		}
		topic.setImages(StringUtils.join(images, ","));
		topic.setContent(content.getContent());
		topic.setAddContent(content.getAddContent());
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Topic topic) {
		if (IdGen.isInvalidId(topic.getId())) {
			this.add(topic);
		} else {
			TopicContent content = new TopicContent();
			content.setContent(topic.getContent());
			content.setAddContent(topic.getAddContent());
			String images = topic.getImages();
			if (StringUtils.isNotBlank(images)) {
				String[] _images = images.split(",");
				content.setImage(getImage(_images, 0));
				content.setImage2(getImage(_images, 1));
				content.setImage3(getImage(_images, 2));
				content.setImage4(getImage(_images, 3));
			}
			topic.setRemarks(StringUtils.abbrHtml(topic.getContent(), 100));
			this.update(topic); 
			content.setId(topic.getId());
			this.contentDao.update(content);
			this._update(topic, (byte)0);
		}
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Topic> topics) {
		List<TopicContent> contents = Lists.newArrayList();
		// 记录数据更新
		for(Topic topic: topics) {
			TopicContent content = new TopicContent();
			content.setId(topic.getId());
			contents.add(content);
			_update(topic, (byte)1);
		}
		this.batchDelete(topics);
		this.contentDao.batchDelete(contents);
	}

	/**
	 * 前台发布 (初始化点击数据)
	 */
	@Override
	@Transactional
	public void add(Topic topic) {
		TopicContent content = new TopicContent();
		content.setContent(topic.getContent());
		String images = topic.getImages();
		if (StringUtils.isNotBlank(images)) {
			String[] _images = images.split(",");
			content.setImage(getImage(_images, 0));
			content.setImage2(getImage(_images, 1));
			content.setImage3(getImage(_images, 2));
			content.setImage4(getImage(_images, 3));
		}
		topic.setRemarks(StringUtils.abbrHtml(topic.getContent(), 100));
		this.insert(topic);
		content.setId(topic.getId());
		this.contentDao.insert(content);
		this.hitsService.touch(topic);
		
		// 索引
		this._update(topic, (byte)0);
		
		// 加入热点
		this._hitUpdate(topic, (byte)0);
	}
	
	// 得到图片最多四张
	private String getImage(String[] _images, int index) {
		if (_images.length <= index) {
			return null;
		}
		return _images[index];
	}
	
	
	/**
	 * 点赞
	 */
	@Override
	@Transactional
	public void hit(Topic topic) {
		Boolean hit = hitService.hit(topic);
		if (hit) {
			this._hitUpdate(topic, (byte)0);
		}
	}

	// 数据修改
	private void _update(Topic topic, byte opt) {
		UpdateData updateData = new UpdateData();
		updateData.setId(topic.getId());
		updateData.setModule(BbsModule.TOPIC);
		updateData.setOpt(opt);
		updateDataService.save(updateData);
	}
	
	// 数据修改(点击的id 需要转化，不然与_update重复)
	private void _hitUpdate(Topic topic, byte opt) {
		String id = new StringBuilder(topic.getId().toString()).append("0").toString();
		UpdateData updateData = new UpdateData();
		updateData.setId(Long.parseLong(id));
		updateData.setModule(BbsModule.TOPIC_HOTSPOT);
		updateData.setOpt(opt);
		updateDataService.save(updateData);
	}
	
	 /**
     * 修改
     */
	@Override
	public void save(ProductAppraise appraise) {
		// 动态和评价的id统一
		Topic topic = appraise.getTopic();
		
		// 先添加商品的图片
		TopicContent content = new TopicContent();
		content.setContent(appraise.getContent());
		content.setAddContent(appraise.getAddContent());
		
		// 图片存储
		if (!StringUtils.isBlank(topic.getImages())) {
			String images = topic.getImages();
			String[] _images = images.split(",");
			content.setImage(getImage(_images, 0));
			content.setImage2(getImage(_images, 1));
			content.setImage3(getImage(_images, 2));
			content.setImage4(getImage(_images, 3));
		}
		topic.setRemarks(StringUtils.abbrHtml(content.getContent(), 100));
		this.update(topic);
		content.setId(topic.getId());
		this.contentDao.update(content);
		this._update(topic, (byte)0);
	}
	
	/**
	 * 前台发布 (初始化点击数据)
	 */
	@Override
	@Transactional
	public void add(ProductAppraise appraise) {
		
		// 动态和评价的id统一
		Topic topic = appraise.getTopic();
		
		// 先添加商品的图片
		TopicContent content = new TopicContent();
		content.setContent(appraise.getContent());
		
		// 图片存储
		if (StringUtils.isBlank(appraise.getImages())) {
			content.setImage(appraise.getProductImage());
		} else {
			String images = topic.getImages();
			if (StringUtils.isNotBlank(images)) {
				String[] _images = images.split(",");
				content.setImage(getImage(_images, 0));
				content.setImage2(getImage(_images, 1));
				content.setImage3(getImage(_images, 2));
				content.setImage4(getImage(_images, 3));
			}
		}
		topic.setRemarks(StringUtils.abbrHtml(content.getContent(), 100));
		this.insert(topic);
		content.setId(topic.getId());
		this.contentDao.insert(content);
		this.hitsService.touch(topic);
		
		// 索引
		this._update(topic, (byte)0);
		
		// 加入热点
		this._hitUpdate(topic, (byte)0);
	}
	
	/**
	 * 前台发布 (初始化点击数据)
	 */
	@Override
	@Transactional
	public void rappraise(ProductAppraise appraise) {
		
		// 先添加商品的图片
		TopicContent content = new TopicContent();
		content.setAddContent(appraise.getContent());
		content.setId(appraise.getId());
		this.contentDao.update("updateRappraise", content);
		
		// 添加追评的标签
		Topic topic = new Topic();
		topic.setId(appraise.getId());
		topic.setTags(appraise.getTags());
		this.update("updateRappraise", topic);
		
		// 索引
		this._update(topic, (byte)0);
	}

	/**
	 * 是否显示
	 */
	@Override
	@Transactional
	public void updateIsShow(List<Topic> topics) {
		for(Topic topic : topics) {
			this.update("updateShow", topic);
			// 索引
			this._update(topic, (byte) 0);
			
			// 加入热点(需要同步是否显示)
			this._hitUpdate(topic, (byte)0);
		}
	}

	/**
	 * 是否显示
	 */
	@Override
	public Byte isShow(Long id) {
		return this.queryForAttr("isShow", id);
	}
}