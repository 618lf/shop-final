package com.shop.config.jdbc;

/**
 * 数据库安全相关的设置
 */
public interface DataBaseSecurity {

	/**
	 * 密码解密
	 * 
	 * @param password
	 * @return
	 */
	default String decrypt(String password) {
		return password;
	}
}