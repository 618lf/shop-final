package com.tmt.api.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.api.entity.Project;
import com.tmt.core.persistence.BaseDaoImpl;

/**
 * 项目 管理
 * @author 超级管理员
 * @date 2017-06-15
 */
@Repository("apiProjectDao")
public class ProjectDao extends BaseDaoImpl<Project,Long> {}
