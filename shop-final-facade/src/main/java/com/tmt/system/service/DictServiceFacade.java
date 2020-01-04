package com.tmt.system.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.system.entity.Dict;

public interface DictServiceFacade extends BaseServiceFacade<Dict, Long>{
	
	/**
	 * 保存
	 * @param dict
	 * @return
	 */
	public Long save(Dict dict);
	
	/**
	 * 根据code查询
	 * @param dictCode
	 * @return
	 */
	public Dict getDictByCode(String dictCode);
	
	/**
	 * 校验
	 * @param dict
	 * @return
	 */
	public int checkDictCode(Dict dict);
	
	/**
	 * 删除
	 * @param dicts
	 */
	public void delete(List<Dict> dicts);
}