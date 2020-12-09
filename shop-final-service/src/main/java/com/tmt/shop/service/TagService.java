package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.shop.dao.TagDao;
import com.tmt.shop.entity.Tag;

/**
 * 商品标签 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Service("shopTagService")
public class TagService extends BaseService<Tag,Long> implements TagServiceFacade{
	
	@Autowired
	private TagDao tagDao;
	
	@Override
	protected BaseDao<Tag, Long> getBaseDao() {
		return tagDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Tag tag) {
		if( IdGen.isInvalidId(tag.getId())) {
			this.insert(tag);
		} else {
			this.update(tag);
		}
	}
	
    @Transactional
	public void updateSort(List<Tag> tags) {
		this.batchUpdate("updateSort", tags);
	}
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Tag> tags) {
		this.batchDelete(tags);
	}
	
	//前端显示需要的
	public List<Tag> queryUseAbleTags() {
		return this.getAll();
	}
}