package com.tmt.cms.service;

import java.util.List;

import com.tmt.cms.entity.Mpage;
import com.tmt.cms.entity.MpageField;
import com.tmt.core.service.BaseServiceFacade;

/**
 * 页面 管理
 * @author 超级管理员
 * @date 2016-11-04
 */
public interface MpageServiceFacade extends BaseServiceFacade<Mpage,Long> {
	
	/**
	 * 带出组件
	 * @param id
	 * @return
	 */
	public Mpage getWithFields(Long id);
	
	/**
	 * 复制的
	 */
	public void copy(Mpage mpage);
	
	/**
	 * 保存
	 */
	public void save(Mpage mpage);
	
	/**
	 * 修改排序
	 * @param mpages
	 */
	public void updateSort(List<Mpage> mpages);
    
	/**
	 * 删除
	 */
	public void delete(List<Mpage> mpages);
	
	/**
	 * 查询配置项
	 * @param pageId
	 * @return
	 */
	public List<MpageField> queryFieldsByPageId(Long pageId);
}