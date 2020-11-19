package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Tag;

/**
 * 商品标签 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
public interface TagServiceFacade extends BaseServiceFacade<Tag,Long> {
	
	/**
	 * 保存
	 */
	public void save(Tag tag);
	
	/**
	 * 排序
	 * @param tags
	 */
	public void updateSort(List<Tag> tags);
	
	/**
	 * 删除
	 */
	public void delete(List<Tag> tags);
	
	/**
	 * 查询可用的标签
	 * @return
	 */
	public List<Tag> queryUseAbleTags();
}