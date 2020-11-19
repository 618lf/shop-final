package com.tmt.bbs.service;

import java.util.List;

import com.tmt.bbs.entity.Section;
import com.tmt.core.service.BaseServiceFacade;

/**
 * 分类
 * @author lifeng
 */
public interface SectionServiceFacade extends BaseServiceFacade<Section,Long>{

	/**
	 * 保存分类
	 * @param section
	 */
	public void save(Section section);
	
	/**
	 * 删除分类
	 * @param sections
	 */
	public void delete(List<Section> sections);
}