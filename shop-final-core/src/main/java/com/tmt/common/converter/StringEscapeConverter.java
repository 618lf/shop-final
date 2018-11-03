package com.tmt.common.converter;

import org.springframework.core.convert.converter.Converter;

import com.tmt.common.utils.StringUtil3;

/**
 * String 格式化
 * @author root
 */
public class StringEscapeConverter implements Converter<String, String>{
	/**
	 * 格式化(不需要这么严格，直接去掉脚本就行了)
	 */
	@Override
	public String convert(String source) {
		return source == null ? null : StringUtil3.removeScript(source.trim());
	}
}