package com.tmt.core.security.utils;

import com.tmt.core.codec.Digests;
import com.tmt.core.codec.Encodes;
import com.tmt.core.utils.StringUtils;

public class PasswordUtils {

	/** 用户密码加密 **/
	private static final int HASH_INTERATIONS = 1024;

	/** 用户密码加密 **/
	private static final int SALT_SIZE = 8;
	
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
