package com.tmt.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.api.dao.GroupDao;
import com.tmt.api.entity.Group;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;

/**
 * 接口分组 管理
 * @author 超级管理员
 * @date 2017-06-15
 */
@Service("apiGroupService")
public class GroupService extends BaseService<Group,Long> {
	
	@Autowired
	private GroupDao groupDao;
	
	@Override
	protected BaseDao<Group, Long> getBaseDao() {
		return groupDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Group group) {
		if(IdGen.isInvalidId(group.getId())) {
			this.insert(group);
		} else {
			this.update(group);
		}
	}
	
    @Transactional
	public void updateSort(List<Group> groups ) {
		this.batchUpdate("updateSort", groups);
	}
    
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Group> groups) {
		this.batchDelete(groups);
	}
	
	/**
	 * 查询项目下面的分组
	 * @param projectId
	 * @return
	 */
	public List<Group> queryByProject(Long projectId) {
		return this.queryForList("queryByProject", projectId);
	}
}
