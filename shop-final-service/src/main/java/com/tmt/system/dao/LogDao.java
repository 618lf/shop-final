package com.tmt.system.dao;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Repository;

import com.tmt.common.persistence.BaseDaoImpl;
import com.tmt.common.persistence.datasource.DataSourceHolder;
import com.tmt.system.entity.Log;

@Repository
public class LogDao extends BaseDaoImpl<Log, Long> {
	
	/**
	 * 高效的插入数据
	 * @param bytes
	 * bytes 构建：
	 * StringBuilder values = new StringBuilder();
	 * values.append("1").append("\t").append("lifeng1").append("\n")
	 *       .append("2").append("\t").append("lifeng2").append("\n");
	 * bytes = values.toString().getBytes();
	 * 
	 * sql 构建：
	 * LOAD DATA LOCAL INFILE 'LOGS.SQL' IGNORE INTO TABLE SYS_LOG (ID, NAME)
	 */
	public int bulkSave(String sql, byte[] bytes) {
		DataSource dataSource = DataSourceHolder.getDataSource();
		Connection conn = DataSourceUtils.getConnection(dataSource);
		PreparedStatement stmt = null;
		InputStream is = null;
    	try {
    		is = new ByteArrayInputStream(bytes);
    		
    		// 编译语句
        	stmt = conn.prepareStatement(sql);
        	
        	// 返回插入的条数
        	int result = 0;
        	if (stmt.isWrapperFor(com.mysql.jdbc.Statement.class)) {
    			com.mysql.jdbc.PreparedStatement mysqlStatement = stmt.unwrap(com.mysql.jdbc.PreparedStatement.class);
    			mysqlStatement.setLocalInfileInputStream(is);
    			result = mysqlStatement.executeUpdate();
    		}
        	return result;
		} catch (Exception e) {
			logger.error("高效插入数据库失败", e);
		} finally {
			IOUtils.closeQuietly(is);
			JdbcUtils.closeStatement(stmt);
			DataSourceUtils.releaseConnection(conn, dataSource);
		}
    	return 0;
	}
}