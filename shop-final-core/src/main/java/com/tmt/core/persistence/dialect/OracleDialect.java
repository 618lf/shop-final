package com.tmt.core.persistence.dialect;

import java.util.Map;

import com.google.common.collect.Maps;
import com.tmt.core.persistence.Database;

/**
 * ORACLE 的 分页设计
 * 
 * @author lifeng
 */
public class OracleDialect implements Dialect {

	protected final static String PAGE_SQL_PREFIX = "SELECT * FROM (SELECT M.*,ROWNUM NUM FROM (";
	protected final static String PAGE_SQL_END = ") M WHERE ROWNUM <= ";
	protected static final String SQL_END_DELIMITER = ";";

	@Override
	public String getLimitString(String sql, int offset, int limit) {
		sql = trim(sql);
		StringBuffer sb = new StringBuffer(sql.length() + 100);
		sb.append(PAGE_SQL_PREFIX).append(sql).append(PAGE_SQL_END).append(limit + offset).append(") WHERE NUM >")
				.append(offset);
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
		return Database.oracle;
	}
	
	/**
	 * 变量
	 */
	@Override
	public Map<String, String> variables() {
		Map<String, String> variables = Maps.newHashMap();
		variables.put("X_LEN", "LENGTH");
		return variables;
	}
}
