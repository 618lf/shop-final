package com.tmt.common.config;

import com.tmt.common.utils.Digests;
import com.tmt.common.utils.Encodes;
import com.tmt.common.utils.StringUtil3;

/**
 * 全局配置类
 * @author lifeng
 */
public class Globals {
	
	/** 用户密码加密 **/
	public static final String HASH_ALGORITHM = "SHA-1";
	
	/** 用户密码加密 **/
	public static final int HASH_INTERATIONS = 1024;
	
	/** 用户密码加密 **/
	public static final int SALT_SIZE = 8;
	
	/** 系统错误码 **/
	public static final String REQUEST_ERROR_CODE_PARAM = "REQUEST_ERROR_CODE_PARAM";
	
	/** 超级管理员 **/
	public static final Long ROOT = 0L;
	
	/** 默认的编码格式 **/
	public static final String DEFAULT_ENCODING = "UTF-8";
	
	/** 新用户 **/
	public static final String NEW_USER = "_nu";
	
	/**
	 * 日期
	 */
	public static final String[] DATE_PATTERNS = {"yyyy-MM-dd",  "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH", "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy", "yyyyMM", "yyyy/MM", "yyyyMMddHHmmss", "yyyyMMdd"};
	
	/**
	 * 系统的配置
	 */
	public static String adminPath;
	public static String frontPath;
	public static String index;
	public static String version;
	public static String domain;
	public static String temps;
	
	/**
	 * 生成安全的密码，生成随机的16位salt并经过1024次 sha-1 hash
	 */
	public static String entryptPassword(String plainPassword) {
		byte[] salt = Digests.generateSalt(SALT_SIZE);
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return new StringBuilder(Encodes.encodeHex(salt)).append(Encodes.encodeHex(hashPassword)).toString();
	}
	
	/**
	 * 验证密码
	 * @param plainPassword 明文密码
	 * @param password 密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		if(StringUtil3.isBlank(plainPassword) || StringUtil3.isBlank(password)) {return Boolean.FALSE;}
		byte[] salt = Encodes.decodeHex(password.substring(0,16));
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return password.equals(new StringBuilder(Encodes.encodeHex(salt)).append(Encodes.encodeHex(hashPassword)).toString());
	}
}