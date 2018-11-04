package com.tmt.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;

import com.tmt.common.exception.BaseRuntimeException;

/**
 * 取代默认的FlashMap -- 不需要支持
 * @author lifeng
 */
public class NoneFlashMapManager implements FlashMapManager {

	@Override
	public FlashMap retrieveAndUpdate(HttpServletRequest request,
			HttpServletResponse response) {
		return null;
	}

	@Override
	public void saveOutputFlashMap(FlashMap flashMap,
			HttpServletRequest request, HttpServletResponse response) {
		throw new BaseRuntimeException("not use FlashMap");
	}
}