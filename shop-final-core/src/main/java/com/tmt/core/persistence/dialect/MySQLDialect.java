package com.tmt.core.persistence.dialect;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;

import com.google.common.collect.Maps;
import com.tmt.core.persistence.Database;
import com.tmt.core.persistence.datasource.DataSourceHolder;

/**
 * mysql 的分页
 * 
 * @author lifeng
 */
public class MySQLDialect implements Dialect {

	protected static final String SQL_END_DELIMITER = ";";

	public String getLimitString(String sql, int offset, int limit) {
		sql = trim(sql);
		StringBuffer sb = new StringBuffer(sql.length() + 20);
		sb.append(sql);
		if (offset > 0) {
			sb.append(" LIMIT ").append(offset).append(",").append(limit).append(SQL_END_DELIMITER);
		} else {
			sb.append(" LIMIT ").append(limit).append(SQL_END_DELIMITER);
		}
		return sb.toString();
	}

	private String trim(String sql) {
		sql = sql.trim();
		if (sql.endsWith(SQL_END_DELIMITER)) {
			sql = sql.substring(0, sql.length() - 1 - SQL_END_DELIMITER.length());
		}
		return sql;
	}

	/**
	 * 高效的插入数据
	 * 
	 * @param bytes bytes 构建： StringBuilder values = new StringBuilder();
	 *              values.append("1").append("\t").append("lifeng1").append("\n")
	 *              .append("2").append("\t").append("lifeng2").append("\n"); bytes
	 *              = values.toString().getBytes();
	 * 
	 *              sql 构建： LOAD DATA LOCAL INFILE 'LOGS.SQL' IGNORE INTO TABLE
	 *              SYS_LOG (ID, NAME)
	 */
	@Override
	public int bulkSave(List<String> sqls, byte[] bytes) {
		DataSource dataSource = DataSourceHolder.getDataSource();
		Connection conn = DataSourceUtils.getConnection(dataSource);
		PreparedStatement stmt = null;
		InputStream is = null;
		try {
			is = new ByteArrayInputStream(bytes);
			stmt = conn.prepareStatement(sqls.get(0));
			int result = 0;
			if (stmt.isWrapperFor(com.mysql.jdbc.Statement.class)) {
				com.mysql.jdbc.PreparedStatement mysqlStatement = stmt.unwrap(com.mysql.jdbc.PreparedStatement.class);
				mysqlStatement.setLocalInfileInputStream(is);
				result = mysqlStatement.executeUpdate();
			}
			return result;
		} catch (Exception e) {
		} finally {
			IOUtils.closeQuietly(is);
			JdbcUtils.closeStatement(stmt);
			DataSourceUtils.releaseConnection(conn, dataSource);
		}
		return 0;
	}

	@Override
	public Database getDatabase() {
		return Database.mysql;
	}

	/**
	 * 变量
	 */
	@Override
	public Map<String, String> variables() {
		Map<String, String> variables = Maps.newHashMap();
		variables.put("X_LEN", "LENGHT");
		return variables;
	}
}