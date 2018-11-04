package com.tmt.common.web;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.tmt.common.utils.JsonMapper;

/**
 * 处理 Long 转 String 的问题
 * copy from  FastJsonHttpMessageConverter
 * 删除对jackson 的支持
 * 
 * @author root
 *
 */
public class JsonHttpMessageConverter extends AbstractHttpMessageConverter<Object> implements
		GenericHttpMessageConverter<Object> {

	private FastJsonConfig fastJsonConfig = new FastJsonConfig();
	 
	@Override
	public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
		return super.canRead(contextClass, mediaType);
	}

	/**
	 * 输入
	 */
	@Override
	public Object read(Type type, Class<?> contextClass,
			HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {
		InputStream in = inputMessage.getBody();
		return JSON.parseObject(in, fastJsonConfig.getCharset(), type,
				fastJsonConfig.getFeatures());
	}

	@Override
	public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
		return super.canWrite(clazz, mediaType);
	}

	/**
	 *  输出
	 */
	@Override
	public void write(Object t, Type type, MediaType contentType,
			HttpOutputMessage outputMessage) throws IOException,
			HttpMessageNotWritableException {
		HttpHeaders headers = outputMessage.getHeaders();
		if (headers.getContentType() == null) {
			if (contentType == null || contentType.isWildcardType()
					|| contentType.isWildcardSubtype()) {
				contentType = getDefaultContentType(t);
			}
			if (contentType != null) {
				headers.setContentType(contentType);
			}
		}
		if (headers.getContentLength() == -1) {
			Long contentLength = getContentLength(t, headers.getContentType());
			if (contentLength != null) {
				headers.setContentLength(contentLength);
			}
		}
		writeInternal(t, outputMessage);
		outputMessage.getBody().flush();
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	protected Object readInternal(Class<? extends Object> clazz,
			HttpInputMessage inputMessage) throws IOException,
			HttpMessageNotReadableException {
		InputStream in = inputMessage.getBody();
		return JSON.parseObject(in, fastJsonConfig.getCharset(), clazz, fastJsonConfig.getFeatures());
	}

	@Override
	protected void writeInternal(Object obj, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException {
		HttpHeaders headers = outputMessage.getHeaders();
		ByteArrayOutputStream outnew = new ByteArrayOutputStream();

		int len = JsonMapper.writeJSONString(outnew, //
				fastJsonConfig.getCharset(), //
				obj, //
				fastJsonConfig.getSerializeConfig(), //
				fastJsonConfig.getSerializeFilters(), //
				fastJsonConfig.getDateFormat(), //
				JSON.DEFAULT_GENERATE_FEATURE, //
				fastJsonConfig.getSerializerFeatures());
		if (fastJsonConfig.isWriteContentLength()) {
			headers.setContentLength(len);
		}

		OutputStream out = outputMessage.getBody();
		outnew.writeTo(out);
		outnew.close();
	}
}