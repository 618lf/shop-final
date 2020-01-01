package com.tmt.common.config;

import java.io.File;

import com.tmt.common.codec.Digests;
import com.tmt.common.codec.Encodes;
import com.tmt.common.utils.StringUtils;

/**
 * 全局配置类
 * 
 * @author lifeng
 */
public class Globals {

	/**
	 * 系统的配置
	 */
	public static String adminPath;
	public static String frontPath;
	public static String index;
	public static String version;
	public static String domain;
	public static File temps;

	/** 用户密码加密 **/
	public static final String HASH_ALGORITHM = "SHA-1";

	/** 用户密码加密 **/
	public static final int HASH_INTERATIONS = 1024;

	/** 用户密码加密 **/
	public static final int SALT_SIZE = 8;
	
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
	 * 
	 * @param plainPassword
	 *            明文密码
	 * @param password
	 *            密文密码
	 * @return 验证成功返回true
	 */
	public static boolean validatePassword(String plainPassword, String password) {
		if (StringUtils.isBlank(plainPassword) || StringUtils.isBlank(password)) {
			return Boolean.FALSE;
		}
		byte[] salt = Encodes.decodeHex(password.substring(0, 16));
		byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), salt, HASH_INTERATIONS);
		return password
				.equals(new StringBuilder(Encodes.encodeHex(salt)).append(Encodes.encodeHex(hashPassword)).toString());
	}
}