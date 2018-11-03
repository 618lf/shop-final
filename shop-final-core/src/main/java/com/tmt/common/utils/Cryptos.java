/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.tmt.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

import com.tmt.common.utils.ExceptionUtil;

/**
 * 支持HMAC-SHA1消息签名 及 DES/AES对称加密的工具类.
 * 
 * 支持Hex与Base64两种编码方式.
 * 
 * @author calvin
 */
public class Cryptos {

	private static final String AES = "AES";

	//-- AES funciton --//
	/**
	 * 使用AES加密原始字符串.
	 * 
	 * @param input 原始输入字符数组
	 * @param key 符合AES要求的密钥
	 */
	private static byte[] aesEncrypt(byte[] input, byte[] key) {
		return aes(input, key, Cipher.ENCRYPT_MODE);
	}

	/**
	 * 使用AES解密字符串, 返回原始字符串.
	 * 
	 * @param input Hex编码的加密字符串
	 * @param key 符合AES要求的密钥
	 */
	private static String aesDecrypt(byte[] input, byte[] key) {
		byte[] decryptResult = aes(input, key, Cipher.DECRYPT_MODE);
		return new String(decryptResult);
	}

	/**
	 * 使用AES加密或解密无编码的原始字节数组, 返回无编码的字节数组结果.
	 * 
	 * @param input 原始字节数组
	 * @param key 符合AES要求的密钥
	 * @param mode Cipher.ENCRYPT_MODE 或 Cipher.DECRYPT_MODE
	 */
	private static byte[] aes(byte[] input, byte[] key, int mode) {
		try {
			SecretKey secretKey = new SecretKeySpec(key, AES);
			Cipher cipher = Cipher.getInstance(AES);
			cipher.init(mode, secretKey);
			return cipher.doFinal(input);
		} catch (GeneralSecurityException e) {
			throw ExceptionUtil.unchecked(e);
		}
	}
	
	/**
	 * 加密
	 * @param content   需要加密的内容
	 * @param password  密码
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encrypt(String content, String password){
		try{
			byte[] dataBytes = content.getBytes("UTF-8");
			byte[] keyBytes = Base64.decodeBase64(password);
			return Base64.encodeBase64String(aesEncrypt(dataBytes, keyBytes));
		}catch(Exception e){
			throw ExceptionUtil.unchecked(e);
		}
	}
	
	/**
	 * 解密
	 * @param content   需要解密的内容
	 * @param password  密码
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String decrypt(String content, String password) {
		try{
			return new String(aesDecrypt(Base64.decodeBase64(content), Base64.decodeBase64(password)));
		}catch(Exception e){
			throw ExceptionUtil.unchecked(e);
		}
	}
	
	public static void main(String[] args){
		String ss = encrypt("1234567812345678","VxDksHQiTvQt9MMPtMVXdA==");
		System.out.println(ss);
		String sss = decrypt(ss,"VxDksHQiTvQt9MMPtMVXdA==");
		System.out.println(sss);
	}
}