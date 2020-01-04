package com.tmt.system.utils;

import com.tmt.core.utils.SpringContextHolder;
import com.tmt.system.entity.Dict;
import com.tmt.system.service.DictServiceFacade;

/**
 * 字典工具类
 * @author lifeng
 * @version 2013-5-29
 */
public class DictUtils {
	
	private static DictServiceFacade dictService = SpringContextHolder.getBean(DictServiceFacade.class);
	
	/**
	 * 获得字典项的值
	 * @param code
	 * @return
	 */
	public static Dict getDict(String code) {
		return dictService.getDictByCode(code);
	}
}