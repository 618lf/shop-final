package com.tmt.system.service;

import com.tmt.common.service.BaseServiceFacade;
import com.tmt.system.entity.Log;

public interface LogServiceFacade extends BaseServiceFacade<Log, Long>{
	
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