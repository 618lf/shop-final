package com.tmt.wechat.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.config.Globals;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.JsonMapper;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;
import com.tmt.core.web.BaseController;
import com.tmt.system.utils.UserUtils;
import com.tmt.wechat.bean.base.WechatConstants;
import com.tmt.wechat.bean.menu.MenuButtons;
import com.tmt.wechat.bean.menu.MenuButtons.Button;
import com.tmt.wechat.entity.App;
import com.tmt.wechat.entity.Menu;
import com.tmt.wechat.service.AppServiceFacade;
import com.tmt.wechat.service.MenuServiceFacade;
import com.tmt.wechat.service.WechatOptionService;

/**
 * 自定义菜单 管理
 * 
 * @author 超级管理员
 * @date 2016-09-13
 */
@Controller("wechatMenuController")
@RequestMapping(value = "${adminPath}/wechat/menu")
public class MenuController extends BaseController {

	@Autowired
	private MenuServiceFacade menuService;
	@Autowired
	private AppServiceFacade appService;
	@Autowired
	private WechatOptionService wechatService;

	/**
	 * 进入初始化页面
	 * 
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Model model) {
		List<App> apps = appService.getAll();
		model.addAttribute("apps", apps);
		return "/wechat/MenuList";
	}

	/**
	 * 初始化页面的数据
	 * 
	 * @param menu
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public AjaxResult page(Menu menu, Page page) {
		List<Menu> menus = this.menuService.queryMenusByAppId(menu.getAppId());
		if (menus.size() == 0) {
			Menu item = new Menu();
			item.setId(IdGen.INVALID_ID);
			item.setParentId(IdGen.ROOT_ID);
			menus.add(item);
		}
		return AjaxResult.success(menus);
	}

	/**
	 * 保存
	 * 
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("save")
	public AjaxResult save(Menu template, HttpServletRequest request) {
		template.userOptions(UserUtils.getUser());
		String postData = request.getParameter("postData");
		List<Menu> menus = JsonMapper.fromJsonToList(postData, Menu.class);
		if (menus != null && menus.size() != 0) {
			this.menuService.save(template, menus);

			// 是否发布
			if (template.getPublish() != null && Menu.YES == template.getPublish()) {
				return this.send(template, menus);
			}
			return AjaxResult.success();
		}
		return AjaxResult.error("没有需要保存的数据");
	}

	// 发布微信自定义菜单
	private AjaxResult send(Menu template, List<Menu> menus) {

		// 对应公众号
		App account = appService.get(template.getAppId());

		// 拼接消息(menus是两层结构)
		MenuButtons menuItem = new MenuButtons();
		List<Button> firstMenus = Lists.newArrayList();
		for (Menu menu : menus) {
			Button first = new Button();
			first.setName(menu.getName());
			first.setKey(menu.getId().toString());
			first.setType(menuType(menu));
			first.setUrl(menuUrl(account, menu));
			firstMenus.add(first);

			// 第二层
			List<Menu> children = menu.getMenus();
			List<Button> secondMenus = Lists.newArrayList();
			for (Menu child : children) {
				Button second = new Button();
				second.setName(child.getName());
				second.setKey(child.getId().toString());
				second.setType(menuType(child));
				second.setUrl(menuUrl(account, child));
				secondMenus.add(second);
			}
			if (secondMenus.size() != 0) {
				first.setType(null);
				first.setUrl(null);
				first.setSub_button(secondMenus);
			}
		}
		menuItem.setButton(firstMenus.toArray(new Button[] {}));

		// 发布
		boolean result = wechatService.bind(account).menuCreate(menuItem);
		if (!result) {
			return AjaxResult.error("创建菜单错误");
		}
		return AjaxResult.success();
	}

	// 菜单类型，跳转或拉取数据
	private String menuType(Menu menu) {
		if (menu.getType() == WechatConstants.HANDLER_view || menu.getType() == WechatConstants.HANDLER_site_view) {
			return "view";
		}
		return "click";
	}

	// 封装菜单URL
	private String menuUrl(App account, Menu menu) {
		String domain = account.getDomain();
		if (StringUtils.isBlank(domain)) {
			domain = Globals.domain;
		} else {
			domain = new StringBuilder("http://").append(domain).toString();
		}
		if (menu.getType() == 5 || menu.getType() == 6) {
			return WebUtils.preAppendScheme(domain, menu.getConfig());
			// return new StringBuilder(domain).append(menu.getConfig()).toString();
		}
		// 默认首页
		return null;
	}
}