package com.tmt.system.utils;

import com.tmt.common.security.context.ThreadContext;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.system.entity.Site;
import com.tmt.system.entity.SystemConstant;
import com.tmt.system.service.SiteServiceFacade;

/**
 * 默认的站点
 * @author root
 */
public class SiteUtils {
	
	private static SiteServiceFacade siteService = SpringContextHolder.getBean(SiteServiceFacade.class);
	
	public static Site getSite() {
		Site site = ThreadContext.get(SystemConstant.SITE_KEY);
		if (site == null) {
			site = siteService.getSite();
			ThreadContext.put(SystemConstant.SITE_KEY, site);
		}
		return site;
	}
}