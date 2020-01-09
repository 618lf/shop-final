package com.tmt.system.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.persistence.Database;
import com.tmt.core.persistence.dialect.Dialect;
import com.tmt.system.entity.UserSession;

/**
 * 用户会话 管理
 * 
 * @author 超级管理员
 * @date 2017-05-27
 */
@Repository("systemUserSessionDao")
public class UserSessionDao extends BaseDaoImpl<UserSession, Long> {

	@Autowired
	private Dialect dialect;

	/**
	 * 兼容性处理
	 */
	@Override
	public Long insert(UserSession entity) {

		// 数据库类型
		Database db = dialect.getDatabase();

		// h2 数据的插入
		if (db == Database.h2) {
			return this.insert("insert_h2", entity);
		}

		// mssql 数据的插入
		if (db == Database.mssql) {
			return this.insert("insert_mssql", entity);
		}

		// 默认mysql
		return super.insert(entity);
	}
}
