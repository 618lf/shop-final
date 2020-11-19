package com.tmt.cms.service;

import java.util.List;

import com.tmt.cms.entity.MpageField;
import com.tmt.cms.entity.Mtemplate;
import com.tmt.core.service.BaseServiceFacade;

/**
 * 页面模板 管理
 * @author 超级管理员
 * @date 2016-11-11
 */
public interface MtemplateServiceFacade extends BaseServiceFacade<Mtemplate,Long> {
	
	/**
	 * 带出组件
	 * @param id
	 * @return
	 */
	public Mtemplate getWithFields(Long id);
	
	/**
	 * 保存
	 */
	public void save(Mtemplate mtemplate);
	
	/**
	 * 删除
	 */
	public void delete(List<Mtemplate> mtemplates);
	
	/**
	 * 查询配置项
	 * @param pageId
	 * @return
	 */
	public List<MpageField> queryFieldsByTemplateId(Long templateId);
}