package com.tmt.cms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.cms.dao.AdDao;
import com.tmt.cms.dao.AdPositionDao;
import com.tmt.cms.entity.AdPosition;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;

/**
 * 广告管理 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
@Service("cmsAdPositionService")
public class AdPositionService extends BaseService<AdPosition,Long> implements AdPositionServiceFacade{
	
	@Autowired
	private AdPositionDao adPositionDao;
	@Autowired
	private AdDao adDao;
	
	@Override
	protected BaseDao<AdPosition, Long> getBaseDao() {
		return adPositionDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(AdPosition adPosition) {
		if(IdGen.isInvalidId(adPosition.getId())) {
			this.insert(adPosition);
		} else {
			this.update(adPosition);
		}
	}
	
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<AdPosition> adPositions) {
		this.batchDelete(adPositions);
	}
	
	/**
	 * 
	 * @return
	 */
	public AdPosition getWithAds(Long id) {
		AdPosition position = this.get(id);
		if(position != null) {
		   position.setAds(this.adDao.queryForList("queryByPositionId", id));
		}
		return position;
	}
}