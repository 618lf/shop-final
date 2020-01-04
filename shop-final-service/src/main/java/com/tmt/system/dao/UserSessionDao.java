package com.tmt.system.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.system.entity.UserSession;

/**
 * 用户会话 管理
 * @author 超级管理员
 * @date 2017-05-27
 */
@Repository("systemUserSessionDao")
public class UserSessionDao extends BaseDaoImpl<UserSession,Long> {}
