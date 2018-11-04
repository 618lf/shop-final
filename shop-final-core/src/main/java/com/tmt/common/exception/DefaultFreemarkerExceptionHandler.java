package com.tmt.common.exception;

import java.io.Writer;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

/**
 * Freemarker 页面中如果有异常，在springmvc 中无法捕获
 * 如果设置为 template_exception_handler = ignore ，又不会获得这个异常
 * @author lifeng
 */
public class DefaultFreemarkerExceptionHandler implements TemplateExceptionHandler{

	@Override
	public void handleTemplateException(TemplateException te, Environment env,
			Writer out) throws TemplateException {
		
		// 抛出一个普通的异常
		throw new BaseRuntimeException(te);
	}
}
