package com.tmt.system.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Service;

import com.tmt.core.exception.DataAccessException;
import com.tmt.core.persistence.datasource.DataSourceHolder;

/**
 * 自增长服务
 * 
 * @author root
 */
@Service
public class NumberGenerator implements NumberGeneratorFacade {

	private HashMap<String, LinkedList<Long>> KEY_CACHE = new HashMap<String, LinkedList<Long>>();
	private final String UPDATE_SQL = "UPDATE SYS_ID_STORE SET CURRENT_VALUE = (CASE WHEN CURRENT_VALUE >= MIN_VALUE THEN CURRENT_VALUE ELSE MIN_VALUE END) + STEP WHERE TABLE_NAME = ?";
	private final String VALUE_SQL = "SELECT CURRENT_VALUE + 1, STEP FROM SYS_ID_STORE WHERE TABLE_NAME = ?";
	private final String VALUE_INIT_SQL = "INSERT INTO SYS_ID_STORE(TABLE_NAME, MIN_VALUE, CURRENT_VALUE, MAX_VALUE, STEP, REMARK) VALUES(?,?,?,?,?,?)";

	@Override
	public Long generateNumber(String key) {
		return getNextKey(key);
	}

	private synchronized Long getNextKey(String tableName) {
		String mapKey = tableName.toUpperCase();
		if (!KEY_CACHE.containsKey(mapKey) || KEY_CACHE.get(mapKey).isEmpty()) {
			LinkedList<Long> keyList = KEY_CACHE.get(mapKey);
			if (keyList == null || keyList.size() == 0) {
				loadFromDB(mapKey);
			}
		}
		return KEY_CACHE.get(mapKey).remove(0);
	}

	private void loadFromDB(String tableName) {
		Connection con = DataSourceHolder.getConnection();
		try {
			Boolean autoCommit = con.getAutoCommit();
			con.setAutoCommit(Boolean.FALSE);
			long[] keyAndStep = this.fetchKeyAndStep(con, tableName);
			if (keyAndStep != null) {
				initKeyCache(tableName, keyAndStep);
			}
			con.setAutoCommit(autoCommit);
		} catch (SQLException ex) {
			throw new DataAccessException("Could not call function SYS_ID_STORE", ex);
		} finally {
			DataSourceHolder.releaseConnection(con);
		}
	}

	// 获取数据
	private long[] fetchKeyAndStep(Connection con, String tableName) {
		PreparedStatement stmt = null;
		ResultSet result = null;
		try {
			stmt = con.prepareStatement(UPDATE_SQL);
			stmt.setString(1, tableName);
			int row = stmt.executeUpdate();
			if (row == 0) {
				initTableKey(con, tableName);
			}
			JdbcUtils.closeStatement(stmt);
			stmt = con.prepareStatement(VALUE_SQL);
			stmt.setString(1, tableName);
			result = stmt.executeQuery();
			result.next();
			long value = result.getLong(1);
			long step = result.getInt(2);
			return new long[] { value, step };
		} catch (SQLException ex) {
			throw new DataAccessException("Could Not Query SYS_ID_STORE ,tableName:" + tableName, ex);
		} finally {
			JdbcUtils.closeResultSet(result);
			JdbcUtils.closeStatement(stmt);
		}
	}

	// 初始化表数据
	private void initTableKey(Connection con, String tableName) {
		PreparedStatement stmt = null;
		try {
			stmt = con.prepareStatement(VALUE_INIT_SQL);
			stmt.setString(1, tableName);
			stmt.setLong(2, 1L);
			stmt.setLong(3, 1L + 20); // min + step
			stmt.setLong(4, 99999999999L);
			stmt.setInt(5, 20);
			stmt.setString(6, "TABLE " + tableName + " ID");
			stmt.executeUpdate();
		} catch (SQLException ex) {
			throw new DataAccessException("Could not Insert SYS_ID_STORE ,tableName:" + tableName, ex);
		} finally {
			JdbcUtils.closeStatement(stmt);
		}
	}

	// 初始化缓存
	private void initKeyCache(String keyName, long[] keyAndStep) {
		long seq = keyAndStep[0];
		long step = keyAndStep[1];
		LinkedList<Long> keyList = (LinkedList<Long>) KEY_CACHE.get(keyName);
		if (keyList == null) {
			keyList = new LinkedList<Long>();
		}
		for (int i = 0; i < step; i++) {
			keyList.add(seq + i);
		}
		KEY_CACHE.put(keyName, keyList);
	}
}