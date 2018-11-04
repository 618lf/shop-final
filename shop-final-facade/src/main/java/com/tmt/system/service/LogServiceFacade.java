package com.tmt.system.service;

import com.tmt.common.service.BaseServiceFacade;
import com.tmt.system.entity.Log;

public interface LogServiceFacade extends BaseServiceFacade<Log, Long>{
	
	/**
	 * 通过流高速写入数据
	 * @param sql -- 需要执行的sql脚本
	 * @param bytes -- 格式化数据的bytes
	 * @return -- 执行的数据数量
	 */
	public int bulkSave(String sql, byte[] bytes);
	
	/**
	 * 指定多少秒钟之前的数据
	 * @param second
	 * @return
	 */
	public int countLogsAfterDate(int second, int step);
	
	/**
	 * 指定多少秒钟之前的数据
	 * @param second
	 * @return
	 */
	public int countUserAfterDate(int second, int step);
}