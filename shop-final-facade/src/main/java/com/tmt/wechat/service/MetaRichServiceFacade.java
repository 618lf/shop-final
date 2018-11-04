package com.tmt.wechat.service;

import java.util.List;

import com.tmt.common.service.BaseServiceFacade;
import com.tmt.wechat.entity.MetaRich;

/**
 * 图文回复 管理
 * @author 超级管理员
 * @date 2016-09-30
 */
public interface MetaRichServiceFacade extends BaseServiceFacade<MetaRich,Long> {
	
	/**
	 * 返回相关
	 * @param id
	 * @return
	 */
	public MetaRich getWithRelas(Long id);
	
	/**
	 * 保存
	 */
	public void save(MetaRich metaRich);
	
	/**
	 * 删除
	 */
	public void delete(List<MetaRich> metaRichs);
}