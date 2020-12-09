package com.tmt.api.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.api.entity.Group;
import com.tmt.core.persistence.BaseDaoImpl;

/**
 * 接口分组 管理
 * @author 超级管理员
 * @date 2017-06-15
 */
@Repository("apiGroupDao")
public class GroupDao extends BaseDaoImpl<Group,Long> {}
