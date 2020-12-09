package com.tmt.resolver;

import java.util.Locale;

import org.springframework.web.servlet.view.AbstractTemplateViewResolver;

public class StoreFreeMarkerViewResolver extends AbstractTemplateViewResolver{

	private String prefix = "/front/";
	private String redirect_prefix = "redirect:";
	
	public StoreFreeMarkerViewResolver() {
		setViewClass(StoreFreemarkerView.class);
	}
	
	/**
	 * 只需要处理 front 开头的
	 */
	@Override
	protected boolean canHandle(String viewName, Locale locale) {
		if (viewName != null && ((viewName.regionMatches(false, 0, prefix, 0, 7))
				|| (viewName.regionMatches(false, 0, redirect_prefix, 0, 9)))) {
			return true;
		}
		return false;
	}
}