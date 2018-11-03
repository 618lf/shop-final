package com.tmt.common.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import com.tmt.common.utils.CacheUtils;
import com.tmt.common.utils.Digests;
import com.tmt.common.utils.Encodes;
import com.tmt.common.utils.Maps;
import com.tmt.common.utils.PropertiesLoader;
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
	 * 保存全局属性值
	 */
	private static Map<String, String> map = Maps.newHashMap();
	/**
	 * 属性文件加载对象
	 */
	private static PropertiesLoader propertiesLoader = new PropertiesLoader("application.properties");
	
	//---------需要系统配置的数据---------------------------------
	
	//获得后台管理路劲
	public static String getAdminPath() {
		return getConfig("adminPath");
	}
	//获得前台管理路劲
	public static String getFrontPath() {
		return getConfig("frontPath");
	}
	// 静态地址 -- 如果没有静态地址则和上面一个一致
	public static String getStaticFront() {
		return getConfig("web.view.index");
	}
	//cookie的路径
	public static String getCookiePath(){
		return getConfig("cookiePath");
	}
	//cookie的域
	public static String getCookieDomain(){
		return getConfig("cookieDomain");
	}
	//读取配置的缓存使用的序列化的工具
	public static String getCacheSerialization(){
		return getConfig("cache.serialization");
	}
	//网站域名
	public static String getDomain(){
		return getConfig("domain");
	}
	//服务名
	public static String getServerSn() {
		return getConfig("server.sn");
	}
	//版本号
	public static String getVersion() {
		return getConfig("server.version");
	}
	/**
	 * 获取配置
	 */
	public static String getConfig(String key) {
		String value = map.get(key);
		if (value == null){
			value = propertiesLoader.getProperty(key);
			map.put(key, value);
		}
		return value;
	}
	
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
	
	// 服务器的相关信息
	/**
	 * 得到服务器的IP地址
	 * @return
	 */
	public static String getSystemIP() {
		String IP_CACHE_NAME = StringUtil3.appendTo(Globals.getServerSn(), "#", "LIP").toString();
		String ip = CacheUtils.getDictCache().get(IP_CACHE_NAME);
		if (ip == null) {
			try {
				ip = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {}
			if(ip != null) {
				CacheUtils.getDictCache().put(IP_CACHE_NAME, ip);
			}
		}
		return ip;
	}
}