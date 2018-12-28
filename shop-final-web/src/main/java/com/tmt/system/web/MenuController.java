package com.tmt.system.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.common.config.Globals;
import com.tmt.common.entity.AjaxResult;
import com.tmt.common.persistence.Page;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.utils.JsonMapper;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.Maps;
import com.tmt.common.utils.StringUtil3;
import com.tmt.common.utils.TreeEntityUtils;
import com.tmt.common.utils.WebUtils;
import com.tmt.common.web.BaseController;
import com.tmt.common.web.security.interceptor.Token;
import com.tmt.system.entity.Menu;
import com.tmt.system.service.MenuServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 系统菜单
 * 
 * @author root
 */
@Controller
@RequestMapping(value = "${spring.application.web.admin}/system/menu")
public class MenuController extends BaseController {

	@Autowired
	private MenuServiceFacade menuService;

	/**
	 * 列表
	 * 
	 * @param menu
	 * @param model
	 * @return
	 */
	@RequestMapping("list")
	public String list(Menu menu, Model model) {
		if (menu != null && menu.getId() != null) {
			model.addAttribute("id", menu.getId());
		}
		return "/system/MenuList";
	}

	/**
	 * 列表数据
	 * 
	 * @param menu
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Menu menu, Model model) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (menu != null && !StringUtil3.isBlank(menu.getName())) {
			params.put("MENU_NAME", menu.getName());
		}
		if (menu != null && !StringUtil3.isBlank(menu.getHref())) {
			params.put("MENU_HREF", menu.getHref());
		}
		if (!params.isEmpty()) {
			List<Menu> menus = this.menuService.findByCondition(params);
			if (menus != null && menus.size() != 0) {
				StringBuffer sb = new StringBuffer(100);
				for (Menu menuItem : menus) {
					sb.append(menuItem.getParentIds());
					sb.append(menuItem.getId()).append(",");
				}
				sb.append("-1");
				params.clear();
				params.put("MENU_IDS", sb.toString());
			}
		}
		if (menu != null && menu.getId() != null) {
			menu = this.menuService.get(menu.getId());
		}
		List<Menu> menus = this.menuService.findByCondition(params);
		if (menus != null) {
			for (Menu menuItem : menus) {
				menuItem.setId(menuItem.getId());
				menuItem.setParent(menuItem.getParentId());
				menuItem.setLevel(menuItem.getLevel());
				menuItem.setExpanded(Boolean.FALSE);
				menuItem.setLoaded(Boolean.TRUE);
				menuItem.setIsLeaf(Boolean.TRUE);
				if (menu != null && menu.getId() != null
						&& (("," + menu.getParentIds() + ",").indexOf("," + menuItem.getId() + ",") != -1)) {
					menuItem.setExpanded(Boolean.TRUE);
				}
			}
		}
		List<Menu> copyMenus = TreeEntityUtils.sort(menus);
		if (copyMenus != null && copyMenus.size() != 0 && !(menu != null && menu.getId() != null)) {
			copyMenus.get(0).setExpanded(Boolean.TRUE);
		}
		Page pageList = new Page();
		pageList.setData(copyMenus);
		return pageList;
	}

	/**
	 * 表单页面
	 * 
	 * @param menu
	 * @param model
	 * @return
	 */
	@Token(save = true)
	@RequestMapping("form")
	public String form(Menu menu, Model model) {
		if (menu != null && menu.getId() != null) {
			menu = this.menuService.get(menu.getId());
		} else {
			menu.setId(IdGen.INVALID_ID);
			if (menu.getParentId() == null) {
				menu.setParentId(IdGen.ROOT_ID);
			}
		}
		Menu parent = this.menuService.get(menu.getParentId());
		menu.setParentMenu(parent);
		model.addAttribute("menu", menu);
		return "/system/MenuForm";
	}

	/**
	 * 选择页面
	 * 
	 * @param extId
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("treeSelect")
	public List<Map<String, Object>> treeSelect(@RequestParam(required = false) String extId) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Menu> list = this.menuService.findAllWithRoot();
		for (int i = 0; i < list.size(); i++) {
			Menu e = list.get(i);
			if (extId == null || (extId != null && !extId.equals(e.getId().toString())
					&& e.getParentIds().indexOf("," + extId + ",") == -1) && e.getType() != 3) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				if (e.getType() == 1) {// 目录判断是否可以选择
					map.put("chkDisabled", Boolean.TRUE);
					int iCount = this.menuService.treeMenuCheck(e.getId());
					if (iCount > 0) {
						map.put("chkDisabled", Boolean.FALSE);
					}
				}
				mapList.add(map);
			}
		}
		return mapList;
	}

	/**
	 * 菜单 细分权限的选择
	 * 
	 * @param extId
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("treeSelect/option")
	public List<Map<String, Object>> treeSelect4Option(@RequestParam(required = false) String extId) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Menu> list = this.menuService.findAllWithRoot();
		for (int i = 0; i < list.size(); i++) {
			Menu e = list.get(i);
			if (extId == null || (extId != null && !extId.equals(e.getId().toString())
					&& e.getParentIds().indexOf("," + extId + ",") == -1)) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				if (e.getType() == 1 || e.getType() == 2) {// 目录判断是否可以选择
					map.put("chkDisabled", Boolean.TRUE);
					int iCount = this.menuService.treeMenuCheck(e.getId());
					if (iCount > 0) {
						map.put("chkDisabled", Boolean.FALSE);
					}
				}
				mapList.add(map);
			}
		}
		return mapList;
	}

	/**
	 * 保存
	 * 
	 * @param menu
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@Token
	@RequestMapping("save")
	public String save(Menu menu, Model model, RedirectAttributes redirectAttributes) {
		menu.userOptions(UserUtils.getUser());
		Long id = this.menuService.save(menu);
		UserUtils.removeAllCache();
		addMessage(redirectAttributes, "保存菜单'" + menu.getName() + "'成功");
		redirectAttributes.addAttribute("id", id);
		return WebUtils.redirectTo(new StringBuilder(Globals.adminPath).append("/system/menu/form").toString());
	}

	/**
	 * 删除
	 * 
	 * @param idList
	 * @param model
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete")
	public AjaxResult delete(Long[] idList, Model model, HttpServletResponse response) {
		List<Menu> menus = Lists.newArrayList();
		for (Long id : idList) {
			Menu menu = new Menu();
			menu.setId(id);
			menus.add(menu);
		}
		Boolean bFalg = this.menuService.delete(menus);
		if (bFalg) {
			UserUtils.removeAllCache();
			return AjaxResult.success();
		}
		return AjaxResult.error("要删除的菜单中存在子菜单，或分配了用户,不能删除");
	}

	/**
	 * 排序
	 * 
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping("sort")
	public AjaxResult sort(Model model, HttpServletRequest request, HttpServletResponse response) {
		String postData = request.getParameter("postData");
		List<Map<String, String>> maps = JsonMapper.fromJson(postData, ArrayList.class);
		if (maps != null && maps.size() != 0) {
			List<Menu> menus = new ArrayList<Menu>();
			for (Map<String, String> map : maps) {
				Menu menu = new Menu();
				menu.setId(Long.parseLong(map.get("id")));
				menu.setSort(Integer.parseInt(map.get("sort")));
				menus.add(menu);
			}
			this.menuService.updateSort(menus);
			UserUtils.removeAllCache();
			return AjaxResult.success();
		}
		return AjaxResult.error("没有需要保存的数据");
	}
}