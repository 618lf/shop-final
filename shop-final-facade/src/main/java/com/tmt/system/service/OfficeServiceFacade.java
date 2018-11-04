package com.tmt.system.service;

import java.util.List;
import java.util.Map;

import com.tmt.common.entity.TreeVO;
import com.tmt.common.service.BaseServiceFacade;
import com.tmt.system.entity.Office;

public interface OfficeServiceFacade extends BaseServiceFacade<Office, Long>{

	/**
	 * 保存
	 * @param office
	 * @return
	 */
	public Long save(Office office);
	
	/**
	 * 查询子节点
	 * @param parent
	 * @return
	 */
	public List<Office> findByParent(Office parent);
	
	/**
	 * 查询子节点
	 * @param parent
	 * @return
	 */
	public List<Office> findByParent(Long parent);
	
	/**
	 * 查询子节点
	 * @param parent
	 * @return
	 */
	public List<Office> findAllByParent(Long parent);
	
	/**
	 * 根据条件查询
	 * @param params
	 * @return
	 */
	public List<TreeVO> findTreeList(Map<String,Object> params);
	
	/**
	 * 获取office 通过code
	 * @param code
	 * @return
	 */
	public Office getByCode(String code);
	
	/**
	 * 删除
	 * @param offices
	 */
	public Boolean delete(List<Office> offices);
}