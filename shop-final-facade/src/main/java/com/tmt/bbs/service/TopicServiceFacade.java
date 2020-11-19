package com.tmt.bbs.service;

import java.util.List;

import com.tmt.bbs.entity.Topic;
import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.ProductAppraise;

/**
 * 主题 管理
 * 
 * @author 超级管理员
 * @date 2017-04-12
 */
public interface TopicServiceFacade extends BaseServiceFacade<Topic, Long> {

	
	/**
	 * 填充内容
	 * @param topic
	 * @return
	 */
	public Topic fillContent(Topic topic);
	
	/**
	 * 带出内容
	 * 
	 * @param id
	 * @return
	 */
	public Topic getWithContent(Long id);

	/**
	 * 保存
	 */
	public void save(Topic topic);

	/**
	 * 删除
	 */
	public void delete(List<Topic> topics);
	
	/**
	 * 添加评价动态
	 * @param topic
	 */
	public void rappraise(ProductAppraise appraise);
	
	/**
	 * 修改
	 */
	public void save(ProductAppraise appraise);
	
	/**
	 * 添加评价动态
	 * @param topic
	 */
	public void add(ProductAppraise appraise);
	
	/**
	 * 添加动态
	 * 
	 * @param topic
	 */
	public void add(Topic topic);

	/**
	 * 点赞当前动态
	 * 
	 * @param topic
	 */
	public void hit(Topic topic);
	

	/**
	 * 更新主题是否显示
	 * 
	 * @param topics
	 */
	public void updateIsShow(List<Topic> topics);
	
	/**
	 * 是否显示
	 * @param id
	 * @return
	 */
	public Byte isShow(Long id);
}
