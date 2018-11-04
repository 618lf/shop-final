package com.tmt.common.staticize;

/**
 * 所有需要提供前端访问的
 * 需要实现这个，来处理域名
 * @author lifeng
 */
public interface DomainSer {

	/**
	 * 返回支持的域名
	 * @return
	 */
	String getDomain();
	
	/**
	 * 返回更新时间
	 * @return
	 */
	Long getUpdateTime();
}