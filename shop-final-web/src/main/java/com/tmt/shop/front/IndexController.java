package com.tmt.shop.front;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tmt.core.staticize.StaticUtils;
import com.tmt.core.utils.WebUtils;
import com.tmt.shop.entity.Store;
import com.tmt.shop.utils.StoreUtils;

/**
 * 系统首页配置
 * 方便后台页面中重定向到前台的地址上
 * 可以支持不再同一台服务器上的情况
 * @author root
 */
@Controller
@RequestMapping(value = "/")
public class IndexController {
  
	/**
	 * 对应首页的地址
	 */
	@Value("${web.view.index}")
	private String siteIndex;
	
	/**
	 * 如果前面有nginx 服务器则不需要设置这个
	 * @return
	 */
	@RequestMapping("")
	public String index() {
		Store store = StoreUtils.getDefaultStore();
		return WebUtils.redirectTo(StaticUtils.staticDomain(store).append(siteIndex).append("?_t=").append(store.getUpdateTime()).toString());
	}
}