package com.tmt.system.service;


/**
 * 自增长数字服务
 * @author root
 */
public interface NumberGeneratorFacade {

	/**
	 * key -- 表名或其他唯一KEY
	 * @return
	 */
	public Long generateNumber(String key);
}