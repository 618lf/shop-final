package com.tmt.system.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.system.entity.ExcelTemplate;

public interface ExcelTemplateServiceFacade extends BaseServiceFacade<ExcelTemplate,Long>{

	/**
	 * 添加
	 * @param template
	 * @return
	 */
	public Long save(ExcelTemplate template);
	
	/**
	 * 删除
	 * @param templates
	 */
	public void delete(List<ExcelTemplate> templates);
	
	/**
	 * 查询模版和详情
	 * @param templateId
	 * @return
	 */
	public ExcelTemplate getWithItems(Long templateId);
	
	/**
	 * 通过目标类,查询模版列表
	 * @param className
	 * @return
	 */
	public List<ExcelTemplate> queryByTargetClass(String className);
	
	/**
	 * 通过类型，查询模板列表
	 * @param className
	 * @return
	 */
	public List<ExcelTemplate> queryByType(String type);
}
