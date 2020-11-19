package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Attribute;
import com.tmt.shop.entity.AttributeOption;

/**
 * 商品属性 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
public interface AttributeServiceFacade extends BaseServiceFacade<Attribute,Long> {
	
	
	/**
	 * 保存
	 * @param attribute
	 */
	public void save(Attribute attribute);
	
	/**
	 * 保存选项
	 * @param attribute
	 */
	public void saveOptions(Attribute attribute);
	
	/**
	 * 修改排序
	 * @param attributes
	 */
	public void updateSort(List<Attribute> attributes);
    
	/**
	 * 删除
	 */
	public void delete(List<Attribute> attributes);
	
	/**
	 * 得到选项
	 * @param id
	 * @return
	 */
	public Attribute getWithOptions(Long id);
	
	/**
	 * 得到所有选项
	 * @param id
	 * @return
	 */
	public List<AttributeOption> queryByAttributeId(Long id);
	
	/**
	 * 查询分类的属性
	 * @param categoryId
	 * @return
	 */
	public List<Attribute> queryByCategoryId(Long categoryId);
}