package com.tmt.core.utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.converters.ArrayConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.tmt.Constants;
import com.tmt.core.converter.EnumConverter;
import com.tmt.core.exception.BaseRuntimeException;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.freemarker.FreeMarkerConfig;
import com.tmt.core.utils.time.DateUtils;

import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapperBuilder;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;
import freemarker.template.utility.DeepUnwrap;

/**
 * 统一的使用
 * 
 * @ClassName: FreemarkerUtils
 * @author 李锋
 * @date Jul 1, 2016 11:25:46 AM
 */
public class FreemarkerUtils {

	public static final ConvertUtilsBean convertBean = new FreemarkerUtils.Convert();
	public static Configuration localConfiguration = null;
	public static BeansWrapperBuilder builder = new BeansWrapperBuilder(Configuration.VERSION_2_3_23);

	static {
		DateConverter localDateConverter = new DateConverter();
		localDateConverter.setPatterns(Constants.DATE_PATTERNS);
		convertBean.register(localDateConverter, Date.class);
	}

	/**
	 * 基于模板来生成数据
	 * 
	 * @param templateName
	 * @param model
	 * @return
	 */
	public static String processUseTemplate(String templateName, Map<String, ?> model) {
		try {
			Configuration configuration = getLocalConfiguration();
			return FreeMarkerTemplateUtils.processTemplateIntoString(
					configuration.getTemplate(templateName, Constants.DEFAULT_ENCODING.toString()), model);
		} catch (Exception localIOException) {
			throw new BaseRuntimeException("生成模版错误", localIOException);
		}
	}

	/**
	 * 生成html页面，模版名称和 model数据
	 * 
	 * @param template
	 *            模版内容
	 * @param model
	 * @return
	 */
	public static String processNoTemplate(String templateContent, Map<String, ?> model) {
		StringWriter out = new StringWriter();
		try {
			Configuration configuration = getLocalConfiguration();
			new Template("template", new StringReader(templateContent), configuration).process(model, out);
		} catch (Exception localIOException) {
			throw new BaseRuntimeException("生成数据错误", localIOException);
		}
		return out.toString();
	}

	/**
	 * 1.优先尝试加载 XFreemarkerUtils 的配置，如果没有在加载自己的配置
	 * 
	 * @return
	 * @throws TemplateException
	 * @throws IOException
	 */
	public static Configuration getLocalConfiguration() throws IOException, TemplateException {
		if (localConfiguration == null) {
			FreeMarkerConfig localFreeMarkerConfigurer = (FreeMarkerConfig) SpringContextHolder
					.getBean("freeMarkerConfigurer", FreeMarkerConfig.class);
			if (localFreeMarkerConfigurer != null) {
				localConfiguration = localFreeMarkerConfigurer.getConfiguration();
			}
		}
		return localConfiguration;
	}

	// ----------其他的操作----------------------------
	@SuppressWarnings("unchecked")
	public static <T> T getParameter(String name, Class<T> type, Map<String, TemplateModel> params)
			throws TemplateModelException {
		TemplateModel localTemplateModel = (TemplateModel) params.get(name);
		if (localTemplateModel == null)
			return null;
		Object localObject = DeepUnwrap.unwrap(localTemplateModel);
		return (T) convertBean.convert(localObject, type);
	}

	public static TemplateModel getVariable(String name, Environment env) throws TemplateModelException {
		return env.getVariable(name);
	}

	public static void setVariable(String name, Object value, Environment env) throws TemplateModelException {
		if ((value instanceof TemplateModel))
			env.setVariable(name, (TemplateModel) value);
		else
			env.setVariable(name, builder.build().wrap(value));
	}

	@SuppressWarnings("rawtypes")
	public static void setVariables(Map<String, Object> variables, Environment env) throws TemplateModelException {
		Iterator localIterator = variables.entrySet().iterator();
		while (localIterator.hasNext()) {
			Map.Entry localEntry = (Map.Entry) localIterator.next();
			String str = (String) localEntry.getKey();
			Object localObject = localEntry.getValue();
			if ((localObject instanceof TemplateModel))
				env.setVariable(str, (TemplateModel) localObject);
			else
				env.setVariable(str, builder.build().wrap(localObject));
		}
	}

	public static class Convert extends ConvertUtilsBean {

		public String convert(Object value) {
			if (value != null) {
				Class<?> localClass = value.getClass();
				if ((localClass.isEnum()) && (super.lookup(localClass) == null)) {
					super.register(new EnumConverter(localClass), localClass);
				} else if ((localClass.isArray()) && (localClass.getComponentType().isEnum())) {
					if (super.lookup(localClass) == null) {
						Object localObject = new ArrayConverter(localClass,
								new EnumConverter(localClass.getComponentType()), 0);
						((ArrayConverter) localObject).setOnlyFirstToString(false);
						super.register((Converter) localObject, localClass);
					}
					Object localObject = super.lookup(localClass);
					return (String) ((Converter) localObject).convert(String.class, value);
				}
			}
			return super.convert(value);
		}

		@SuppressWarnings("rawtypes")
		public Object convert(String value, Class clazz) {
			if ((clazz.isEnum()) && (super.lookup(clazz) == null))
				super.register(new EnumConverter(clazz), clazz);
			return super.convert(value, clazz);
		}

		@SuppressWarnings("rawtypes")
		public Object convert(String[] values, Class clazz) {
			if ((clazz.isArray()) && (clazz.getComponentType().isEnum())
					&& (super.lookup(clazz.getComponentType()) == null))
				super.register(new EnumConverter(clazz.getComponentType()), clazz.getComponentType());
			return super.convert(values, clazz);
		}

		@SuppressWarnings("rawtypes")
		public Object convert(Object value, Class targetType) {
			if (super.lookup(targetType) == null)
				if (targetType.isEnum()) {
					super.register(new EnumConverter(targetType), targetType);
				} else if ((targetType.isArray()) && (targetType.getComponentType().isEnum())) {
					ArrayConverter localArrayConverter = new ArrayConverter(targetType,
							new EnumConverter(targetType.getComponentType()), 0);
					localArrayConverter.setOnlyFirstToString(false);
					super.register(localArrayConverter, targetType);
				}
			return super.convert(value, targetType);
		}
	}

	/**
	 * 生成随机路径
	 * 
	 * @param format
	 *            类似 /qrcode/tmp/${data}/${datatime}${rand}.jpg
	 * @return
	 */
	public static String getFormatePath(String format) {
		return FreemarkerUtils.processNoTemplate(format, getRootMap());
	}

	/**
	 * 用户创建随机文件名的ROOT
	 * 
	 * @return
	 */
	private static Map<String, Object> getRootMap() {
		Map<String, Object> root = Maps.newHashMap();
		root.put("data", DateUtils.getTodayStr("yyyy-MM-dd"));
		root.put("datatime", DateUtils.getTodayStr("yyyyMMddHHmmss"));
		root.put("rand", IdGen.stringKey());
		return root;
	}

	/**
	 * 有些属性，是freemarker 不支持的，需要转义
	 */
	public static Map<String, Object> escape(Map<String, Object> context) {

		// 基本的校验
		if (context == null || context.isEmpty()) {
			return context;
		}

		// size
		if (context.containsKey("size")) {
			Object v = context.remove("size");
			context.put("_size", v);
		}

		return context;
	}
}