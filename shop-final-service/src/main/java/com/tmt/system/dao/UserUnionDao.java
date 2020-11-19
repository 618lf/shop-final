package com.tmt.system.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.system.entity.UserUnion;

/**
 * 统一用户 管理
 * @author 超级管理员
 * @date 2016-09-01
 */
@Repository("systemUserUnionDao")
public class UserUnionDao extends BaseDaoImpl<UserUnion,String> {}
