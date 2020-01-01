package com.tmt.system.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.common.entity.AjaxResult;
import com.tmt.common.persistence.Page;
import com.tmt.common.persistence.PageParameters;
import com.tmt.common.persistence.QueryCondition;
import com.tmt.common.persistence.QueryCondition.Criteria;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.utils.StringUtils;
import com.tmt.common.web.BaseController;
import com.tmt.system.entity.User;
import com.tmt.system.service.AreaServiceFacade;
import com.tmt.system.service.UserServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 系统控件
 * 
 * @author root
 */
@Controller
@RequestMapping(value = "${spring.application.web.admin}/system/tag")
public class TagController extends BaseController {

	// -------------基础功能-------------------
	/**
	 * 标签选择之树形选择
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "treeselect")
	public String treeselect(HttpServletRequest request, Model model) {
		String extId = request.getParameter("extId");
		extId = StringUtils.isBlank(extId) ? String.valueOf(IdGen.INVALID_ID) : extId;
		model.addAttribute("id", request.getParameter("id")); // 编号
		model.addAttribute("url", request.getParameter("url")); // 树结构数据URL
		model.addAttribute("extId", extId); // 排除的编号ID
		model.addAttribute("checked", request.getParameter("checked")); // 是否可复选
		model.addAttribute("notAllowSelectRoot", request.getParameter("notAllowSelectRoot")); // 是否允许选择根节点
		model.addAttribute("notAllowSelectParent", request.getParameter("notAllowSelectParent")); // 允许选择父节点
		model.addAttribute("allowClear", request.getParameter("allowClear")); // 是否允许清除
		model.addAttribute("selectIds", request.getParameter("selectIds")); // 指定默认选中的ID
		return "/system/TreeSelect";
	}

	@RequestMapping(value = "iconselect")
	public String iconselect(HttpServletRequest request, Model model) {
		model.addAttribute("value", request.getParameter("value"));
		return "/system/Iconselect";
	}

	/**
	 * 二维码操作
	 * 
	 * @return
	 */
	@RequestMapping(value = "qrcode")
	public String qrcode(String title, String url, Model model) {
		String _url = StringUtils.startsWith(url, "http://")
				? StringUtils.format("http://%s", StringUtils.replace(StringUtils.remove(url, "http://"), "//", "/"))
						.toString()
				: StringUtils.replace(url, "//", "/");
		model.addAttribute("title", title);
		model.addAttribute("url", _url);
		return "/system/QrcodeOperator";
	}

	// -------------系统业务功能(可以提供的业务功能)-------------------
	@Autowired
	private AreaServiceFacade areaService;
	@Autowired
	private UserServiceFacade userService;

	/**
	 * 国家, 省, 市, 县, 街道 1 2 3 4 5
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/area/{level}")
	public AjaxResult areas(@PathVariable Integer level, Long parentId, String name) {
		return AjaxResult.success(areaService.queryAreasByLevel(level, parentId, name));
	}

	/**
	 * 显示level以上的部分 例如，level = 3 则显示 省, 市
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/area_b/{level}")
	public AjaxResult area_b(@PathVariable Integer level, Long parentId) {
		return AjaxResult.success(areaService.queryAreasByBeforeLevel(level, parentId));
	}

	// 用户模块提供的组件
	/**
	 * 表格选择
	 * 
	 * @return
	 */
	@RequestMapping("/user/tableSelect")
	public String userTableSelect() {
		return "/system/UserTableSelect";
	}

	/**
	 * 列表数据
	 * 
	 * @param user
	 * @param model
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/user/page")
	public Page page(User user, Model model, Page page) {
		QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		if (user != null && StringUtils.isNotEmpty(user.getName())) {
			c.andLike("U.NAME", user.getName());
		}
		if (user != null && StringUtils.isNotEmpty(user.getNo())) {
			String sql = StringUtils.format("((',%s,' LIKE CONCAT('%,', U.NO, ',%') ) OR (U.NO LIKE '%%s%'))",
					StringUtils.escapeDb(user.getNo()), StringUtils.escapeDb(user.getNo()));
			c.andConditionSql(sql);
		}
		qc.setOrderByClause("U.NO");
		page = this.userService.queryForPage(qc, param);
		// 加载用户组
		if (page.getData() != null && page.getData().size() != 0) {
			List<User> users = page.getData();
			for (User u : users) {
				u.setName(StringUtils.isBlank(u.getName()) ? ("匿名 - " + u.getNo()) : u.getName() + " - " + u.getNo());
			}
		}
		return page;
	}

	/**
	 * 树形选择
	 * 
	 * @param extId
	 * @param officeId
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/user/treeSelect")
	public List<Map<String, Object>> userTreeSelect(@RequestParam(required = false) String extId, Long officeId,
			HttpServletResponse response) {
		return this.userService.userTreeSelect(officeId);
	}

	/**
	 * 限制查询的部门是自己所在的部门
	 * 
	 * @param extId
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/user/office_treeSelect")
	public List<Map<String, Object>> userTreeSelect2(@RequestParam(required = false) String extId,
			HttpServletResponse response) {
		Long officeId = UserUtils.getUser().getOfficeId();
		return this.userTreeSelect(extId, officeId, response);
	}
}