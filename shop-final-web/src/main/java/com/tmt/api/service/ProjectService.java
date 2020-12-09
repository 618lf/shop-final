package com.tmt.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.api.dao.ProjectDao;
import com.tmt.api.entity.Project;
import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;

/**
 * 项目 管理
 * @author 超级管理员
 * @date 2017-06-15
 */
@Service("apiProjectService")
public class ProjectService extends BaseService<Project,Long> {
	
	@Autowired
	private ProjectDao projectDao;
	
	@Override
	protected BaseDao<Project, Long> getBaseDao() {
		return projectDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Project project) {
		if(IdGen.isInvalidId(project.getId())) {
			this.insert(project);
		} else {
			this.update(project);
		}
	}
	
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Project> projects) {
		this.batchDelete(projects);
	}
	
}
