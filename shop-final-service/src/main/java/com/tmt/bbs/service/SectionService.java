package com.tmt.bbs.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.bbs.dao.SectionDao;
import com.tmt.bbs.entity.Section;
import com.tmt.bbs.utils.BbsUtils;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;

/**
 * 主题分类 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
@Service("bbsSectionService")
public class SectionService extends BaseService<Section,Long> implements SectionServiceFacade{
	
	@Autowired
	private SectionDao sectionDao;
	
	@Override
	protected BaseDao<Section, Long> getBaseDao() {
		return sectionDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Section section) {
		if (IdGen.isInvalidId(section.getId())) {
			if (section.getPid() != null) {
				section.setId(section.getPid());
			}
			this.insert(section);
		} else {
			this.update(section);
		}
		
		// 清楚缓存
		BbsUtils.clearSectionStats(section.getId());
	}
	
	/**
	 * 保存排序
	 * @param sections
	 */
    @Transactional
	public void updateSort(List<Section> sections ) {
		this.batchUpdate("updateSort", sections);
	}
    
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Section> sections) {
		this.batchDelete(sections);
		
		// 清楚缓存
		BbsUtils.clearSectionStats(null);
	}
}