package com.tmt.wechat.utils;

import java.util.List;

import com.tmt.core.utils.CacheUtils;
import com.tmt.core.utils.SpringContextHolder;
import com.tmt.wechat.bean.base.Article;
import com.tmt.wechat.bean.base.WechatConstants;
import com.tmt.wechat.entity.App;
import com.tmt.wechat.entity.Qrcode;
import com.tmt.wechat.service.AppServiceFacade;
import com.tmt.wechat.service.MenuServiceFacade;
import com.tmt.wechat.service.MetaSettingServiceFacade;

/**
 * 微信工具类
 * @author root
 */
public class WechatUtils {
	
	/**
	 * 得到缓存的APP 服务
	 * 通过域名获取
	 * @return
	 */
	public static App enhanceApp(App app) {
		MetaSettingServiceFacade settingService = SpringContextHolder.getBean(MetaSettingServiceFacade.class);
		MenuServiceFacade menuService = SpringContextHolder.getBean(MenuServiceFacade.class);
		
		// 加载默认的设置
		app.setSetting(settingService.getByAppId(app.getId()));
		
		// 加载菜单项
		app.setMenus(menuService.queryMenusByAppId(app.getId()));
		return app;
	}
	
	/**
	 * ID 获取APP
	 * @param appId
	 * @return
	 */
	public static App get(String appId) {
		String key = new StringBuilder(WechatConstants.APP_CACHE).append(appId).toString();
		App app = CacheUtils.get(key);
		if (app == null) {
			AppServiceFacade appService = SpringContextHolder.getBean(AppServiceFacade.class);
			app = enhanceApp(appService.get(appId));
			// 存储
			CacheUtils.put(key, app);
		}
		return app;
	}
	
	/**
	 * 根据domain 获取App
	 * @param domain
	 * @return
	 */
	public static App getDomainApp(String domain) {
		String key = new StringBuilder(WechatConstants.APP_CACHE).append(domain).toString();
		App app = CacheUtils.get(key);
		if (app == null) {
			AppServiceFacade appService = SpringContextHolder.getBean(AppServiceFacade.class);
			app = enhanceApp(appService.getDomain(domain));
			
			// 存储
			CacheUtils.put(key, app);
		}
		return app;
	}
	
	/**
	 * 响应事件中的APP
	 * 响应事件中会把APP 的微信号传递过来
	 * @param wxnum
	 * @return
	 */
	public static App getEventApp(String wxnum) {
        String key = new StringBuilder(WechatConstants.APP_CACHE).append(wxnum).toString();
		App app = CacheUtils.get(key);
		if (app == null) {
			AppServiceFacade appService = SpringContextHolder.getBean(AppServiceFacade.class);
			app = enhanceApp(appService.getEvent(wxnum));
			
			// 存储
			CacheUtils.put(key, app);
		}
		return app;
	}
	
	/**
	 * 清除缓存
	 */
	public static void clearCache() {
		String key = new StringBuilder(WechatConstants.APP_CACHE).append("*").toString();
		CacheUtils.deletePattern(key);
	}
	
	// ------------ 图文消息(最好有失效期限) -----------------
	/**
	 * 缓存的图文消息
	 * @param richId
	 * @return
	 */
	public static List<Article> getCacheMetaRich(Long richId) {
		String key = new StringBuilder(WechatConstants.META_CACHE).append(richId).toString();
		return CacheUtils.getSessCache().get(key);
	}
	
	/**
	 * 缓存的图文消息
	 * @param richId
	 * @return
	 */
	public static void cachedMetaRich(Long richId, List<Article> articles) {
		String key = new StringBuilder(WechatConstants.META_CACHE).append(richId).toString();
		CacheUtils.getSessCache().put(key, articles);
	}
	
	/**
	 * 删除缓存的图文消息
	 * @param richId
	 */
	public static void clearMetaRich(Long richId) {
		String key = new StringBuilder(WechatConstants.META_CACHE).append(richId).toString();
		CacheUtils.getSessCache().delete(key);
	}
	
	// ------------ 二维码缓存(最好有失效期限) -----------------
	public static Qrcode getCacheQrcode(String qrscene) {
		String key = new StringBuilder(WechatConstants.QRCODE_CACHE).append(qrscene).toString();
		return CacheUtils.getSessCache().get(key);
	}
	public static void cachedQrcode(String qrscene, Qrcode qrcode) {
		String key = new StringBuilder(WechatConstants.QRCODE_CACHE).append(qrscene).toString();
		CacheUtils.getSessCache().put(key, qrcode);
	}
	public static void clearQrcode(String qrscene) {
		String key = new StringBuilder(WechatConstants.QRCODE_CACHE).append(qrscene).toString();
		CacheUtils.getSessCache().delete(key);
	}
}