package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Parameter;
import com.tmt.shop.entity.ParameterOption;

/**
 * 商品参数 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
public interface ParameterServiceFacade extends BaseServiceFacade<Parameter,Long> {
	
	/**
	 * 保存
	 */
	public void save(Parameter parameter);
	
	/**
	 * 分为添加和修改删除
	 * @param parameter
	 */
	public void saveOptions(Parameter parameter);
	
	/**
	 * 排序
	 * @param parameters
	 */
	public void updateSort(List<Parameter> parameters);
    
	/**
	 * 删除
	 */
	public void delete(List<Parameter> parameters);
	
	/**
	 * 得到选项
	 * @param id
	 * @return
	 */
	public Parameter getWithOptions(Long id);
	
	/**
	 * 属性选择项
	 * @param id
	 * @return
	 */
	public List<ParameterOption> queryByParameterId(Long id);
	
	/**
	 * 根据分类查询参数列表
	 * @param categoryId
	 * @return
	 */
	public List<Parameter> queryByCategoryId(Long categoryId);
}