/**
 * Copyright (c) 2005-2012 springside.org.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.tmt.core.codec;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;

/**
 * 封装各种格式的编码解码工具类. 1.Commons-Codec的 hex/base64 编码 2.自制的base62 编码
 * 3.Commons-Lang的xml/html escape 4.JDK提供的URLEncoder
 * 
 * @author calvin
 * @version 2013-01-15
 */
public class Encodes {

	private static final Logger logger = LoggerFactory.getLogger(Encodes.class);

	// Hex
	// ------------------------------------------
	public static String encodeHex(byte[] input) {
		return Hex.encodeHexString(input);
	}

	public static byte[] decodeHex(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			throw new RuntimeException(e);
		}
	}

	// Base64
	// ----------------------------------------------
	public static String encodeBase64(byte[] input) {
		return Base64.encodeBase64String(input);
	}

	public static String encodeBase64URLSafeString(byte[] input) {
		return Base64.encodeBase64URLSafeString(input);
	}

	public static byte[] decodeBase64(String input) {
		return Base64.decodeBase64(input);
	}

	public static String decodeBase64(String input, String charsetName) {
		try {
			return new String(Base64.decodeBase64(input), charsetName);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	// URL
	// ---------------------------------------
	public static String urlEncode(String part) {
		try {
			return URLEncoder.encode(part, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String urlDecode(String part) {

		try {
			return URLDecoder.decode(part, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	public static String toUnicode(String input) {
		StringBuilder builder = new StringBuilder();
		char[] chars = input.toCharArray();
		for (char ch : chars) {
			if (ch < 256) {
				builder.append(ch);
			} else {
				builder.append("\\u" + Integer.toHexString(ch & 0xffff));
			}
		}
		return builder.toString();
	}

	// 预编译XSS过滤正则表达式
	// ---------------------------------------
	private static List<Pattern> xssPatterns = Lists.newArrayList(Pattern.compile(
			"(<\\s*(script|link|style|iframe)([\\s\\S]*?)(>|<\\/\\s*\\1\\s*>))|(</\\s*(script|link|style|iframe)\\s*>)",
			Pattern.CASE_INSENSITIVE),
			Pattern.compile(
					"\\s*(href|src)\\s*=\\s*(\"\\s*(javascript|vbscript):[^\"]+\"|'\\s*(javascript|vbscript):[^']+'|(javascript|vbscript):[^\\s]+)\\s*(?=>)",
					Pattern.CASE_INSENSITIVE),
			Pattern.compile("\\s*on[a-z]+\\s*=\\s*(\"[^\"]+\"|'[^']+'|[^\\s]+)\\s*(?=>)", Pattern.CASE_INSENSITIVE),
			Pattern.compile("(eval\\((.*?)\\)|xpression\\((.*?)\\))", Pattern.CASE_INSENSITIVE),
			Pattern.compile("^(javascript:|vbscript:)", Pattern.CASE_INSENSITIVE));

	/**
	 * XSS 非法字符过滤，内容以<!--HTML-->开头的用以下规则（保留标签）
	 * 
	 * @author ThinkGem
	 */
	public static String xssFilter(String text) {
		String oriValue = StringUtils.trim(text);
		if (text != null) {
			String value = oriValue;
			for (Pattern pattern : xssPatterns) {
				Matcher matcher = pattern.matcher(value);
				if (matcher.find()) {
					value = matcher.replaceAll(StringUtils.EMPTY);
				}
			}
			// 如果开始不是HTML，XML，JOSN格式，则再进行HTML的 "、<、> 转码。
			if (!StringUtils.startsWithIgnoreCase(value, "<!--HTML-->") // HTML
					&& !StringUtils.startsWithIgnoreCase(value, "<?xml ") // XML
					&& !StringUtils.contains(value, "id=\"FormHtml\"") // JFlow
					&& !(StringUtils.startsWith(value, "{") && StringUtils.endsWith(value, "}")) // JSON Object
					&& !(StringUtils.startsWith(value, "[") && StringUtils.endsWith(value, "]")) // JSON Array
			) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < value.length(); i++) {
					char c = value.charAt(i);
					switch (c) {
					case '>':
						sb.append("＞");
						break;
					case '<':
						sb.append("＜");
						break;
					case '\'':
						sb.append("＇");
						break;
					case '\"':
						sb.append("＂");
						break;
//					case '&':
//						sb.append("＆");
//						break;
//					case '#':
//						sb.append("＃");
//						break;
					default:
						sb.append(c);
						break;
					}
				}
				value = sb.toString();
			}
			if (logger.isInfoEnabled() && !value.equals(oriValue)) {
				logger.info("xssFilter: {}   <=<=<=   {}", value, text);
			}
			return value;
		}
		return null;
	}

	// Sql
	// ---------------------------------------
	private static Pattern sqlPattern = Pattern.compile(
			"(?:')|(?:--)|(/\\*(?:.|[\\n\\r])*?\\*/)|(\\b(select|update|and|or|delete|insert|trancate|char|into|substr|ascii|declare|exec|count|master|into|drop|execute|case when)\\b)",
			Pattern.CASE_INSENSITIVE);

	/**
	 * SQL过滤，防止注入，传入参数输入有select相关代码，替换空。
	 * 
	 * @author ThinkGem
	 */
	public static String sqlFilter(String text) {
		if (text != null) {
			String value = text;
			Matcher matcher = sqlPattern.matcher(value);
			if (matcher.find()) {
				value = matcher.replaceAll(StringUtils.EMPTY);
			}
			if (logger.isWarnEnabled() && !value.equals(text)) {
				logger.info("sqlFilter: {}   <=<=<=   {}", value, text);
				return StringUtils.EMPTY;
			}
			return value;
		}
		return null;
	}

//	public static void main(String[] args) {
//		int i = 0;
//		xssFilter((++i) + "你好，<script>alert(document.cookie)</script>我还在。");
//		xssFilter((++i) + "你好，<strong>加粗文字</strong>我还在。");
//		xssFilter("<!--HTML-->" + (++i) + "你好，\"><strong>加粗文字</strong>我还在。");
//		xssFilter("<!--HTML-->" + (++i) + "你好，<iframe src=\"abcdef\"></iframe><strong>加粗文字</strong>我还在。");
//		xssFilter("<!--HTML-->" + (++i) + "你好，<iframe src=\"abcdef\"/><strong>加粗文字</strong>我还在。");
//		xssFilter("<!--HTML-->" + (++i) + "你好，<iframe src=\"abcdef\"><strong>加粗文字</strong>我还在。");
//		xssFilter("<!--HTML-->" + (++i) + "你好，<script type=\"text/javascript\">alert(document.cookie)</script>我还在。");
//		xssFilter("<!--HTML-->" + (++i)
//				+ "你好，<script\n type=\"text/javascript\">\nalert(document.cookie)\n</script>我还在。");
//		xssFilter("<!--HTML-->" + (++i) + "你好，<script src='' onerror='alert(document.cookie)'></script>我还在。");
//		xssFilter("<!--HTML-->" + (++i) + "你好，<script type=text/javascript>alert()我还在。");
//		xssFilter("<!--HTML-->" + (++i) + "你好，<script>alert(document.cookie)</script>我还在。");
//		xssFilter("<!--HTML-->" + (++i) + "你好，<script>window.location='url'我还在。");
//		xssFilter("<!--HTML-->" + (++i) + "你好，</script></iframe>我还在。");
//		xssFilter("<!--HTML-->" + (++i) + "你好，eval(abc)我还在。");
//		xssFilter("<!--HTML-->" + (++i) + "你好，xpression(abc)我还在。");
//		xssFilter("<!--HTML-->" + (++i)
//				+ "你好，<img src='abc.jpg' onerror='location='';alert(document.cookie);'></img>我还在。");
//		xssFilter("<!--HTML-->" + (++i) + "你好，<img src='abc.jpg' onerror='alert(document.cookie);'/>我还在。");
//		xssFilter("<!--HTML-->" + (++i) + "你好，<img src='abc.jpg' onerror='alert(document.cookie);'>我还在。");
//		xssFilter("<!--HTML-->" + (++i) + "你好，<a onload='alert(\"abc\")'>hello</a>我还在。");
//		xssFilter("<!--HTML-->" + (++i) + "你好，<a href=\"/abc\">hello</a>我还在。");
//		xssFilter("<!--HTML-->" + (++i) + "你好，<a href='/abc'>hello</a>我还在。");
//		xssFilter("<!--HTML-->" + (++i) + "你好，<a href='vbscript:alert(\"abc\");'>hello</a>我还在。");
//		xssFilter("<!--HTML-->" + (++i) + "你好，<a href='javascript:alert(\"abc\");'>hello</a>我还在。");
//		xssFilter("<!--HTML-->" + (++i) + "你好，?abc=def&hello=123&world={\"a\":1}我还在。");
//		xssFilter("<!--HTML-->" + (++i) + "你好，?abc=def&hello=123&world={'a':1}我还在。");
//		sqlFilter((++i) + "你好，select * from xxx where abc=def and 1=1我还在。");
//		sqlFilter((++i) + "你好，insert into xxx values(1,2,3,4,5)我还在。");
//		sqlFilter((++i) + "你好，delete from xxx我还在。");
//		sqlFilter((++i)
//				+ "a.audit_result asc,case when 1 like case when length(database())=6 then 1 else exp(11111111111111111) end then 1 else 1/0 end");
//		sqlFilter((++i) + "你好，'我还在。");
//	}
}
