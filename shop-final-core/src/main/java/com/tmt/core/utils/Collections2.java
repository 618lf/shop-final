package com.tmt.core.utils;

import java.util.Collection;

/**
 * 集合操作类,补充
 * 
 * @author lifeng
 * 
 */
public final class Collections2 {

	private Collections2() {}

	/**
	 * 转换
	 * @param iterable
	 * @return
	 */
	public static <T> Collection<T> cast(Iterable<T> iterable) {
		return (Collection<T>) iterable;
	}
}
