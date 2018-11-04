package com.tmt.common.web.tld.functions;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.tmt.common.config.Globals;
import com.tmt.common.utils.ContextHolderUtils;
import com.tmt.common.utils.DateUtil3;
import com.tmt.common.utils.StringUtil3;

/**
 * 系统提供的一些常用的方法
 * @author lifeng
 */
public class Functions {

	/**
	 * 系统默认的路径
	 * @return
	 */
	public static String getBasePath() {
		HttpServletRequest request = ContextHolderUtils.getRequest();
		if (request != null) {
			StringBuilder url = new StringBuilder().append(request.getScheme()).append("://").append(request.getServerName());
			return 80 == request.getServerPort() ? url.toString(): url.append(":").append(request.getServerPort()).toString();
		}
		return "";
	}
	
	/**
	 * 系统服务
	 * @return
	 */
	public static String getWebRoot() {
		return ContextHolderUtils.getWebRoot();
	}
	
	/**
	 * 后台的路径
	 * @return
	 */
	public static String getAdminPath() {
		return Globals.getAdminPath();
	}
	
	/**
	 * 前台的路径
	 * @return
	 */
	public static String getFrontPath() {
		return Globals.getFrontPath();
	}
	
	/**
	 * 版本
	 * @return
	 */
	public static String getVersion() {
		return Globals.getVersion();
	}
	
	/**
	 * 系统配置项
	 * @param key
	 * @return
	 */
	public static String getConfig(String key) {
		return Globals.getConfig(key);
	}
	
	/**
	 * 长度 只支持 list map string
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static int length(Object obj) {
        if (obj == null) return 0;  
        
        if (obj instanceof String) return ((String)obj).length();
        if (obj instanceof Collection) return ((Collection)obj).size();
        if (obj instanceof Map) return ((Map)obj).size();
        return 0;
	}
	
	/**
	 * 字符串是否为空
	 * @param cs
	 * @return
	 */
	public static boolean isNotBlank(String cs) {
		return StringUtil3.isNotBlank(cs);
	}
	
	/**
	 * 开头
	 * @param cs
	 * @param prefix
	 * @return
	 */
	public static boolean startsWith(String cs, String prefix) {
		return StringUtil3.startsWith(cs, prefix);
	}
	
	/**
	 * 结尾
	 * @param cs
	 * @param suffix
	 * @return
	 */
	public static boolean endsWith(String cs, String suffix) {
		return StringUtil3.endsWith(cs, suffix);
	}
	
	/**
	 * 缩写字符串，超过最大宽度用“...”表示
	 * @param cs
	 * @param length
	 * @return
	 */
	public static String abbr(String cs, int length) {
		return StringUtil3.abbr(cs, length);
	}
	
	/**
	 * 位置
	 * @param sc
	 * @param searchSeq
	 * @return
	 */
	public static int indexOf(String sc, String searchSeq) {
		return StringUtil3.indexOf(sc, searchSeq);
	}
	
	/**
	 * 包含
	 * @param sc
	 * @param searchSeq
	 * @return
	 */
	public static boolean contains(String sc, String searchSeq) {
		return StringUtil3.contains(sc, searchSeq);
	}
	
	/**
	 * 格式化
	 * @param format
	 * @param pattern
	 * @return
	 */
	public static String formatDate(Date format, String pattern) {
		if (format == null) {return "";}
		return DateUtil3.getFormatDate(format, pattern);
	}
}