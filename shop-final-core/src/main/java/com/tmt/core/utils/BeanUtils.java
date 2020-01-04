package com.tmt.core.utils;

import java.util.Map;

import org.apache.poi.ss.formula.functions.T;

/**
 * 属性操作
 * @author lifeng
 */
public class BeanUtils {

	/**
	 * 两个同源对象属性合并，忽略属性为 null 的值
	 * @param target
	 * @param source
	 * @param clazz
	 * @return
	 */
	@SuppressWarnings("hiding")
	public static <T> T merge(Object target, Object source, Class<T> clazz) {
		Map<String, Object> mtarget = Maps.fromBean(target);
		Map<String, Object> msource = Maps.fromBean(source);
		mtarget.putAll(msource);
		String json = JsonMapper.toJson(mtarget);
		return JsonMapper.fromJson(json, clazz);
	}
}
