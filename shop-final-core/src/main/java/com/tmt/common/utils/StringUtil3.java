package com.tmt.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.tmt.common.utils.splitter.Splitter;

/**
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 * @author ThinkGem
 * @version 2013-05-22
 */
public abstract class StringUtil3 extends org.apache.commons.lang3.StringUtils {

	/**
	 * 不能创建对象
	 */
	private StringUtil3(){}
	
	/**
	 * 替换所有的标点符号
	 * @return
	 */
	public static String replaceP(String text, String replacement){
		if (StringUtil3.isNotBlank(text) && replacement != null) {
		    return text.replaceAll("\\pP", replacement);
		}
		return text;
	}
	
	/**
	 * 替换所有的标点符号
	 * @return
	 */
	public static String removeP(String text){
		if (StringUtil3.isNotBlank(text)) {
		    return text.replaceAll("\\pP", "");
		}
		return text;
	}
	
	/**
	 * 替换所有的空白
	 * @return
	 */
	public static String removeZ(String text){
		if (StringUtil3.isNotBlank(text)) {
		    return text.replaceAll("\\pZ", "");
		}
		return text;
	}
	
	/**
	 * 替换所有的空白
	 * @return
	 */
	public static String removeSpace(String text){
		if (StringUtil3.isNotBlank(text)) {
		    return text.replaceAll("\\s", "");
		}
		return text;
	}
	
	/**
	 * 去掉BOM字符
	 * @return
	 */
	public static String removeBom(String text){
		if (StringUtil3.isNotBlank(text)) {
		    return StringUtil3.trim(StringUtil3.replaceChars(text, (char)65279, ' '));
		}
		return text;
	}
	
	/**
	 * 替换掉HTML标签方法
	 */
	public static String removeHtml(String html) {
		if (isBlank(html)){
			return "";
		}
		String regEx = "<[^>]+>";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(html);
		String s = m.replaceAll("").replaceAll("&nbsp;", "");
		return s;
	}
	
	/**
	 * 删除 javaScript 脚本
	 * @return
	 */
	public static String removeScript(String input) {
		if(StringUtil3.isBlank(input)) {
		   return "";
		}
		return input.replaceAll("<script.*?</script>", "");
	}

	/**
	 * 缩略字符串（不区分中英文字符）
	 * @param str 目标字符串
	 * @param length 截取长度
	 * @return
	 */
	public static String abbr(String str, int length) {
		if (str == null) { return ""; }
		try {
			StringBuilder sb = new StringBuilder();
			int currentLength = 0;
			for (char c : str.toCharArray()) {
				currentLength += String.valueOf(c).getBytes("GBK").length;
				if (currentLength <= length - 3) {
					sb.append(c);
				} else {
					sb.append("...");
					break;
				}
			}
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/**
	 * 缩略字符串（不区分中英文字符） --- 先去掉html的格式
	 * @param str 目标字符串
	 * @param length 截取长度
	 * @return
	 */
	public static String abbrHtml(String str, int length) {
		String html = str;
	    if (StringUtil3.isNotBlank(html)) {
	    	html = StringUtil3.removeHtml(html);
			html = StringUtil3.removeSpace(html);
	    }
		return abbr(html, length);
	}
	
	/**
	 * StringBuffer .. append
	 */
	public static StringBuilder appendTo(Object... parts){
	    return appendTo(new StringBuilder(), Arrays.asList(parts));
	}
	
	/**
	 * StringBuffer .. append
	 */
	public static StringBuilder appendTo(StringBuilder appendable, Iterable<?> parts){
		return appendTo(appendable, parts.iterator());
	}
	
	/**
	 * StringBuffer .. append
	 */
	public static StringBuilder appendTo(StringBuilder appendable, Object... parts){
	    return appendTo(appendable, Arrays.asList(parts));
	}
	
	
	/**
	 * StringBuffer .. append
	 */
    public static StringBuilder appendTo(StringBuilder appendable, Iterator<?> parts){
		if (parts.hasNext()) {
			appendable.append(toString(parts.next()));
			while (parts.hasNext()) {
				appendable.append(toString(parts.next()));
			}
		}
		return appendable;
	}
    
    public static CharSequence toString(Object part) {
		return (part instanceof CharSequence) ? (CharSequence) part : part.toString();
	}
    
	/**
	 * 信息格式化
	 * @param template
	 * @param args
	 * @return
	 */
	public static String format(String template, Object... args) {
		template = String.valueOf(template); // null -> "null"
		StringBuilder builder = new StringBuilder(template.length() + 16* args.length);
		int templateStart = 0;
		int i = 0;
		while (i < args.length) {
			int placeholderStart = template.indexOf("%s", templateStart);
			if (placeholderStart == -1) {
				break;
			}
			builder.append(template.substring(templateStart, placeholderStart));
			builder.append(args[i++]);
			templateStart = placeholderStart + 2;
		}
		builder.append(template.substring(templateStart));

		if (i < args.length) {
			builder.append(" [");
			builder.append(args[i++]);
			while (i < args.length) {
				builder.append(", ");
				builder.append(args[i++]);
			}
			builder.append(']');
		}
		return builder.toString();
	}
	
	/**
	 * 首字母转小写 
	 * @param s
	 * @return
	 */
	public static String lowerCaseFirstOne(String s) {
		if (Character.isLowerCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
	}

	/**
	 * 首字母转大写 
	 * @param s
	 * @return
	 */
	public static String upperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
	}
	
	/**
	 * 将属性转换为默认字段(约定大于配置)  userName --> USER_NAME
	 * @param property
	 * @return
	 */
	public static String convertProperty2Column(String property){
		StringBuilder column = new StringBuilder();
		for(int i=0;i<property.length();i++){
			char c = property.charAt(i);
			if(Character.isUpperCase(c)){
				column.append("_");
			}
			column.append(Character.toUpperCase(c));
		}
		return column.toString();
	}
	
	/**
	 * 将字段名转为属性名  USER_NAME --- > userName
	 * @param property  USER_NAME --- > userName
	 * @return
	 */
	public static String convertColumn2Property(String column){
		StringBuilder property = new StringBuilder();
		String[] columns = column.split("_");
		for(int i = 0; i<columns.length; i++) {
			String s = StringUtil3.lowerCase(columns[i]);
			if(i != 0) {
				s = StringUtil3.upperCaseFirstOne(s);
			}
			property.append(s);
		}
		return property.toString();
	}
	
	/**
	 * 删除： utf-8 无法显示的字符(有问题)，直接将数据库的相关字段改为
	 * NAME VARCHAR(100) CHARACTER SET UTF8MB4 COLLATE UTF8MB4_GENERAL_CI DEFAULT NULL COMMENT '昵称';
	 * 貌似不能解决所有问题，很多地方需要改，还不如把不能插入的字符过滤掉
	 * @param src
	 * @param replace
	 * @return
	 */
	public static String mb4Replace(String src, String replace) {
		replace = replace==null?"":replace;
		if (StringUtil3.isNotBlank(src)) {
			return src.replaceAll("[\\x{10000}-\\x{10FFFF}]", replace);
		}
		return src;
	}
	
	/**
	 * 转义数据库的特殊字符 --- 数据库的转义
	 * @param value
	 * @return
	 */
	public static Object escapeDb(String value) {
		if (value != null && value instanceof String
				&& StringUtil3.containsAny((String)value, '\\', '\'')) {
			return StringUtil3.replaceEach(value, new String[]{"\\", "\'"}, new String[]{"\\\\\\\\", "\\'"});
		}
		return value;
	}
	
	/**
	 * 不为空
	 * @param str
	 * @param defaultStr
	 * @return
	 */
    public static String defaultString(String str, String defaultStr) {
        return StringUtil3.isBlank(str) ? defaultStr : str;
    }
    
    /**
     * 创建一个分隔器 【简单的】
     * @param separator
     * @return
     */
    public static Splitter splitter(final String separator) {
		return Splitter.on(separator);
	}
    
    /**
     * 创建一个分隔器 【正则】
     * @param separator
     * @return
     */
    public static Splitter splitterByPattern(String separatorPattern) {
    	return Splitter.onPattern(separatorPattern);
    }
}