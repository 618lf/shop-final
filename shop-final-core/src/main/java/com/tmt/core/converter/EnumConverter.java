package com.tmt.core.converter;

import org.apache.commons.beanutils.converters.AbstractConverter;

public class EnumConverter extends AbstractConverter {

	private final Class<?> clazz;

	public EnumConverter(Class<?> enumClass) {
		this(enumClass, null);
	}

	public EnumConverter(Class<?> enumClass, Object defaultValue) {
		super(defaultValue);
		this.clazz = enumClass;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected Object convertToType(Class type, Object value) {
		String str = value.toString().trim();
		return Enum.valueOf(type, str);
	}

	@Override
	protected Class<?> getDefaultType() {
		return clazz;
	}

	protected String convertToString(Object value) {
		return value.toString();
	}

}
