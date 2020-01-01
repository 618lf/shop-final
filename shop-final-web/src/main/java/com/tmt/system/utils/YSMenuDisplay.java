package com.tmt.system.utils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.tmt.common.config.Globals;
import com.tmt.common.utils.ContextHolderUtils;
import com.tmt.common.utils.StringUtils;
import com.tmt.common.utils.TreeEntityUtils;
import com.tmt.common.utils.TreeEntityUtils.Filter;
import com.tmt.system.entity.Menu;

/**
 * 默认的菜单显示
 * 
 * @author root
 */
public class YSMenuDisplay {

	/**
	 * 展示菜单
	 */
	public String getUIMenu(List<Menu> menuList) {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("<ul class='nav nav-list' id='userMenuNav'>");
			sb.append(getBSUIMenu(menuList));
			sb.append("</ul>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	// menuList是按级别和排序字段排好序 只支持两级菜单 bootStrap
	public static String getBSUIMenu(List<Menu> menuList) throws IOException {
		// 菜单过滤
		Filter<Menu> filter = new Filter<Menu>() {
			@Override
			public Boolean filter(Menu menu) {
				return menu != null && "1".equals(menu.getIsShow()) ? Boolean.TRUE : Boolean.FALSE;
			}
		};
		Map<Integer, List<Menu>> menuMap = TreeEntityUtils.classifyByLevel(menuList, filter);
		StringBuffer menuString = new StringBuffer();
		int firstLevel = 1;
		List<Menu> firstMenu = menuMap.get(firstLevel); // level从1开始
		if (firstMenu == null) {
			return "";
		}
		for (Menu menu : firstMenu) {
			int deep = 1;// 深度
			menuString.append(getContentTags(menu, firstLevel, menuMap, deep));
		}
		return menuString.toString();
	}

	/**
	 * 得到菜单内容
	 * 
	 * @param menu
	 * @param level
	 * @param menuMap
	 * @return
	 * @throws IOException
	 */
	private static String getContentTags(Menu menu, int level, Map<Integer, List<Menu>> menuMap, int deep)
			throws IOException {
		StringBuffer menuString = new StringBuffer();
		menuString.append("<li>");
		menuString.append(getBHrefTag(menu, deep));
		menuString.append(getIconTag(menu, deep));
		menuString.append(getSpanTag(menu, deep));
		menuString.append(getEHrefTag(menu));
		if (menu.getType() == 1) {// 目录
			deep++;
			menuString.append(getBSUIChildMenu(menu, level + 1, menuMap, deep));
		}
		menuString.append("</li>");
		return menuString.toString();
	}

	// 竖 展示子菜单
	private static String getBSUIChildMenu(Menu parent, int level, Map<Integer, List<Menu>> menuMap, int deep)
			throws IOException {
		List<Menu> menuList = menuMap.get(level);
		if (menuList == null) {
			return "";
		}
		StringBuffer menuString = null;
		Boolean hasChild = Boolean.FALSE;
		Boolean hasGroup = Boolean.FALSE;
		for (Menu menu : menuList) {
			if (menu.getParentId().compareTo(parent.getId()) == 0) {
				if (menu.getType() == 1) {
					hasGroup = Boolean.TRUE;
				} else if (menu.getType() == 2) {
					hasChild = Boolean.TRUE;
				}
			}
		}
		if (hasGroup) {
			for (Menu menu : menuList) {
				if (menu.getParentId().compareTo(parent.getId()) == 0 && menu.getType() == 1) {
					if (menuString == null) {
						menuString = new StringBuffer();
						menuString.append("<div class='submenu-wrap group-submenu'>");
					}
					menuString.append("<div class='group-item'>");
					menuString.append("<h3>" + menu.getName() + "</h3>");
					menuString.append(getBSUIChildMenu(menu, level + 1, menuMap, deep + 1));
					menuString.append("</div>");
				}
			}
			if (menuString != null) {
				menuString.append("</div>");
			}
		} else if (hasChild) {
			for (Menu menu : menuList) {
				if (menu.getParentId().compareTo(parent.getId()) == 0 && menu.getType() == 2) {
					if (menuString == null) {
						menuString = new StringBuffer();
						menuString.append("<div class='submenu-wrap'><ul class='submenu'>");
					}
					menuString.append(getContentTags(menu, level, menuMap, deep));
				}
			}
			if (menuString != null) {
				menuString.append("</ul></div>");
			}
		}
		if (menuString != null) {
			return menuString.toString();
		}
		return "";
	}

	/**
	 * 得到菜单的连接地址 如果没有配置 前缀 则自动加上
	 * 
	 * @param href
	 * @return
	 */
	private static String getBHrefTag(Menu menu, int deep) {
		String href = "javascript:void(0)";
		if (menu.getType() == 2) {
			href = menu.getHref();
			if (!StringUtils.startsWithAny(href, new String[] { "http://", "https://" })) {
				if (!StringUtils.startsWithAny(href, new String[] { "http://", "https://" })
						&& !StringUtils.startsWith(href, Globals.adminPath)) {
					href = Globals.adminPath + href;
					menu.setHref(href);
				}
				href = ContextHolderUtils.getWebRoot() + href;
			}
		}
		StringBuffer menuString = new StringBuffer();
		menuString.append("<a href='").append(href).append("' data-id='").append(menu.getId());
		if (menu.getType() == 1 && deep == 1) {// 目录
			menuString.append("' class='top-catalog");
		}
		if (menu.getType() == 1) {
			menuString.append("' >");
		} else {
			menuString.append("' rel='pageTab'>");
		}
		return menuString.toString();
	}

	/**
	 * 得到菜单的连接地址 如果没有配置 前缀 则自动加上
	 * 
	 * @param href
	 * @return
	 */
	private static String getEHrefTag(Menu menu) {
		StringBuilder menuString = new StringBuilder();
		if (menu.getType() == 1) {// 目录
			menuString.append("<i class='arrow iconfont icon-chevron-right'></i>");
		}
		menuString.append("</a>");
		return menuString.toString();
	}

	/**
	 * 得到图标,第一层才有图标，其他层没有图标
	 * 
	 * @param menu
	 * @param defaulIcon
	 * @return
	 * @throws IOException
	 */
	private static String getIconTag(Menu menu, int deep) throws IOException {
		String icon = menu.getIconClass() == null ? "iconfont icon-menu" : menu.getIconClass();
		StringBuilder icons = new StringBuilder("");
		if (deep == 1) {
			return StringUtils.appendTo(icons, "<i class='menu ", icon, "'></i>&nbsp;").toString();
		}
		return StringUtils.appendTo(icons).toString();
	}

	/**
	 * 菜单文本
	 * 
	 * @param menu
	 * @param deep
	 * @return
	 * @throws IOException
	 */
	private static String getSpanTag(Menu menu, int deep) throws IOException {
		return StringUtils.appendTo(new StringBuilder(), "<span class='menu-text ",
				StringUtils.trimToEmpty(menu.getDegree()), "'>", menu.getName(), "</span>", menu.getSubMenu())
				.toString();
	}
}
