package com.tmt.core.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.AbstractFlashMapManager;

import com.tmt.core.web.security.session.SessionProvider;

/**
 * 不建议使用springmvc 的重定向，前台提价数据全部使用ajax
 * 后端可以使用form提交
 * springmvc 的 SessionFlashMapManager
 * 
 * @author lifeng
 */
public class SessionFlashMapManager extends AbstractFlashMapManager {

	private String FLASH_MAPS_SESSION_ATTRIBUTE = SessionFlashMapManager.class.getName() + ".FLASH_MAPS";

	/**
	 * 获取数据
	 */
	@Override
	protected List<FlashMap> retrieveFlashMaps(HttpServletRequest request) {
		return SessionProvider.getAttribute(FLASH_MAPS_SESSION_ATTRIBUTE);
	}

	/**
	 * 存储数据
	 */
	@Override
	protected void updateFlashMaps(List<FlashMap> flashMaps,
			HttpServletRequest request, HttpServletResponse response) {
		if (flashMaps == null || flashMaps.isEmpty()) {
			flashMaps = null;
		}
		SessionProvider.setAttribute(FLASH_MAPS_SESSION_ATTRIBUTE, flashMaps);
	}

	/**
	 * 不需要
	 */
	@Override
	protected Object getFlashMapsMutex(HttpServletRequest request) {
		return null;
	}
}