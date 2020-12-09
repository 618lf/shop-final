package com.tmt.cms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.cms.dao.NavigationDao;
import com.tmt.cms.entity.Navigation;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;

/**
 * 导航管理 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
@Service("cmsNavigationService")
public class NavigationService extends BaseService<Navigation,Long> implements NavigationServiceFacade{
	
	@Autowired
	private NavigationDao navigationDao;
	
	@Override
	protected BaseDao<Navigation, Long> getBaseDao() {
		return navigationDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Navigation navigation) {
		if(IdGen.isInvalidId(navigation.getId())) {
			this.insert(navigation);
		} else {
			this.update(navigation);
		}
	}
	
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Navigation> navigations) {
		this.batchDelete(navigations);
	}
	
}
