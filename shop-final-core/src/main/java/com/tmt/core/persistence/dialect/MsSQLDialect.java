package com.tmt.core.persistence.dialect;

import com.tmt.core.persistence.Database;

/**
 * mysql 的分页
 * 
 * @author lifeng
 */
public class MsSQLDialect implements Dialect {

	protected static final String SQL_END_DELIMITER = ";";

	public String getLimitString(String sql, int offset, int limit) {
		sql = trim(sql);
		StringBuffer sb = new StringBuffer(sql.length() + 64);
		sb.append(sql).append(" OFFSET ").append(offset).append(" ROWS FETCH NEXT ").append(limit).append(" ROWS ONLY")
				.append(SQL_END_DELIMITER);
		return sb.toString();
	}

	private String trim(String sql) {
		sql = sql.trim();
		if (sql.endsWith(SQL_END_DELIMITER)) {
			sql = sql.substring(0, sql.length() - 1 - SQL_END_DELIMITER.length());
		}
		return sql;
	}

	@Override
	public Database getDatabase() {
		return Database.mssql;
	}
}