package com.tmt.core.converter;

import org.springframework.core.convert.converter.Converter;

import com.tmt.core.codec.Encodes;

/**
 * String 格式化
 * 
 * @author root
 */
public class StringEscapeConverter implements Converter<String, String> {
	@Override
	public String convert(String source) {
		return source == null ? null : Encodes.xssFilter(source);
	}
}