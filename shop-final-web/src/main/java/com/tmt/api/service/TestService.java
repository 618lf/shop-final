package com.tmt.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.api.dao.TestDao;
import com.tmt.api.entity.Test;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;

/**
 * 测试 管理
 * @author 超级管理员
 * @date 2017-06-15
 */
@Service("apiTestService")
public class TestService extends BaseService<Test,Long> {
	
	@Autowired
	private TestDao testDao;
	
	@Override
	protected BaseDao<Test, Long> getBaseDao() {
		return testDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Test test) {
		if(IdGen.isInvalidId(test.getId())) {
			this.insert(test);
		} else {
			this.update(test);
		}
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Test> tests) {
		this.batchDelete(tests);
	}
	
	/**
	 * 根据接口查询
	 * @param documentId
	 * @return
	 */
	public List<Test> queryByDocumnet(Long documentId) {
		return this.queryForList("queryByDocumnet", documentId);
	}
}
