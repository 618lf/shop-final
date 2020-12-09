package com.tmt.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.api.dao.MockDao;
import com.tmt.api.entity.Mock;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;

/**
 * Mock 管理
 * @author 超级管理员
 * @date 2017-06-15
 */
@Service("apiMockService")
public class MockService extends BaseService<Mock,Long> {
	
	@Autowired
	private MockDao mockDao;
	
	@Override
	protected BaseDao<Mock, Long> getBaseDao() {
		return mockDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Mock mock) {
		if(IdGen.isInvalidId(mock.getId())) {
			this.insert(mock);
		} else {
			this.update(mock);
		}
	}
	
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Mock> mocks) {
		this.batchDelete(mocks);
	}
	
}
