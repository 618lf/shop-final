package com.tmt.wechat.service;

import java.util.List;

import com.tmt.common.service.BaseServiceFacade;
import com.tmt.wechat.entity.MetaText;

/**
 * 文本回复 管理
 * @author 超级管理员
 * @date 2016-09-30
 */
public interface MetaTextServiceFacade extends BaseServiceFacade<MetaText,Long> {
	
	/**
	 * 保存
	 */
	public void save(MetaText metaText);
	
	/**
	 * 删除
	 */
	public void delete(List<MetaText> metaTexts);
	
}