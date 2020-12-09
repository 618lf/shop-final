package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Lists;
import com.tmt.shop.dao.AttributeDao;
import com.tmt.shop.dao.AttributeOptionDao;
import com.tmt.shop.entity.Attribute;
import com.tmt.shop.entity.AttributeOption;

/**
 * 商品属性 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Service("shopAttributeService")
public class AttributeService extends BaseService<Attribute, Long> implements AttributeServiceFacade {
	
	@Autowired
	private AttributeDao attributeDao;
	@Autowired
	private AttributeOptionDao optionDao;
	
	@Override
	protected BaseDao<Attribute, Long> getBaseDao() {
		return attributeDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Attribute attribute) {
		if( IdGen.isInvalidId(attribute.getId()) ) {
			this.insert(attribute);
		} else {
			this.update(attribute);
		}
		this.saveOptions(attribute); 
	}
	
	/**
	 * 分为添加和修改删除
	 * @param attribute
	 */
	@Transactional
	public void saveOptions(Attribute attribute) {
		List<AttributeOption> options = attribute.getOptions();
		List<AttributeOption> olds = this.queryByAttributeId(attribute.getId());
		List<AttributeOption> inserts = Lists.newArrayList();
		List<AttributeOption> updates = Lists.newArrayList();
		List<AttributeOption> deletes = Lists.newArrayList();
		//设置序号
		int i = 1;
		for(AttributeOption option: options) {
			option.setAttributeId(attribute.getId());
			option.setSort(i++);
		}
		//修改的和删除的
		for(AttributeOption old: olds) {
			Boolean found = Boolean.FALSE;
			for(AttributeOption option: options) {
				if(old.getId().equals(option.getId())) {
				   updates.add(option);  found = Boolean.TRUE;
				   break;
				}
			}
			if(!found) {
			   deletes.add(old);
			}
		}
		//新增的
		for(AttributeOption option: options) {
			Boolean found = Boolean.FALSE;
			for(AttributeOption old: olds) {
				if(old.getId().equals(option.getId())) {
				   found = Boolean.TRUE; break;
				}
			}
			if(!found) {
			   inserts.add(option);
			}
		}
		this.optionDao.batchDelete(deletes);
		this.optionDao.batchInsert(inserts);
		this.optionDao.batchUpdate(updates);
	}
	
    @Transactional
	public void updateSort(List<Attribute> attributes ) {
		this.batchUpdate("updateSort", attributes);
	}
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Attribute> attributes) {
		this.batchDelete(attributes);
	}
	
	/**
	 * 得到选项
	 * @param id
	 * @return
	 */
	public Attribute getWithOptions(Long id) {
		Attribute attribute = this.get(id);
		if( attribute != null) {
			List<AttributeOption> options = this.queryByAttributeId(id);
			attribute.setOptions(options);
		}
		return attribute;
	}
	
	public List<AttributeOption> queryByAttributeId(Long id) {
		return this.optionDao.queryForList("queryByAttributeId", id);
	}
	
	/**
	 * 查询分类的属性
	 * @param categoryId
	 * @return
	 */
	public List<Attribute> queryByCategoryId(Long categoryId) {
		return this.queryForList("queryByCategoryId", categoryId);
	}
}