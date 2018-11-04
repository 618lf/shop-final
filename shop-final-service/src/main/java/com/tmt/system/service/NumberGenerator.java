package com.tmt.system.service;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.LinkedList;

import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Service;

import com.tmt.common.exception.DataAccessException;
import com.tmt.common.persistence.datasource.DataSourceHolder;

/**
 * 自增长服务
 * @author root
 */
@Service
public class NumberGenerator implements NumberGeneratorFacade{

	private HashMap<String,LinkedList<Long>> KEY_CACHE = new HashMap<String,LinkedList<Long>>();
	private final String VALUE_SQL = "{? = call F_NEXT_VALUE(?) }";
	private final String VALUE_INIT_SQL = "INSERT INTO SYS_ID_STORE(TABLE_NAME,MIN_VALUE,CURRENT_VALUE,MAX_VALUE,STEP,REMARK) VALUES(?,?,?,?,?,?)";
	private final String STEP_SEQ_SEPARATOR = "-";
	
	@Override
	public Long generateNumber(String key) {
		return getNextKey(key);
	}
	
	private synchronized Long getNextKey(String tableName) {
		String mapKey = tableName.toUpperCase();
		if (!KEY_CACHE.containsKey(mapKey) || KEY_CACHE.get(mapKey).isEmpty()) {
			LinkedList<Long> keyList = KEY_CACHE.get(mapKey);
			if(keyList== null || keyList.size() == 0) {
				loadFromDB(mapKey);
			}
		}
		return KEY_CACHE.get(mapKey).remove(0);
	}
	
	private void loadFromDB(String tableName){
		Connection con = DataSourceHolder.getConnection();
		CallableStatement stmt = null;
		try {
			Boolean autoCommit = con.getAutoCommit();
			con.setAutoCommit(Boolean.FALSE);
			stmt = con.prepareCall(VALUE_SQL);
			stmt.registerOutParameter(1, Types.VARCHAR);
			stmt.setString(2, tableName);
			stmt.execute();
			String keyAndStep = stmt.getString(1);
			if( keyAndStep != null ) {
				initKeyCache(tableName,keyAndStep);
			} else {
				initTableKey(con, tableName);
				stmt.execute();
				keyAndStep = stmt.getString(1);
				initKeyCache(tableName,keyAndStep);
			}
			con.setAutoCommit(autoCommit);
		}catch (SQLException ex) {
			throw new DataAccessException("Could not call function F_NEXT_VALUE", ex);
		}finally {
			JdbcUtils.closeStatement(stmt);
			DataSourceHolder.releaseConnection(con);
		}
	}
	
	private void initTableKey(Connection con, String tableName){
		PreparedStatement  stmt = null;
		try {
			stmt = con.prepareStatement(VALUE_INIT_SQL);
			stmt.setString(1, tableName);
			stmt.setLong(2, 1L);
			stmt.setLong(3, 0L);
			stmt.setLong(4, 99999999999L);
			stmt.setInt(5, 20);
			stmt.setString(6, "TABLE " + tableName + " ID");
			stmt.executeUpdate();
		} catch (SQLException ex) {
			throw new DataAccessException("Could not insert EM_ID_STORE ,tableName:" + tableName, ex);
		} finally {
			JdbcUtils.closeStatement(stmt);
		}
	}
	
	private void initKeyCache( String keyName, String keyAndStep) {
		String[] seqAndStep = keyAndStep.split(STEP_SEQ_SEPARATOR);
		if (seqAndStep != null && seqAndStep.length == 2) {
			long seq = Long.parseLong(seqAndStep[0]);
			int step = Integer.parseInt(seqAndStep[1]);
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
}