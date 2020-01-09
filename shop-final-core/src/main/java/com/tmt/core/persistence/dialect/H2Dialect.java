package com.tmt.core.persistence.dialect;

import com.tmt.core.persistence.Database;

/**
 * h2 数据库操作
 * 
 * @author lifeng
 */
public class H2Dialect extends OracleDialect {

	@Override
	public Database getDatabase() {
		return Database.h2;
	}
}