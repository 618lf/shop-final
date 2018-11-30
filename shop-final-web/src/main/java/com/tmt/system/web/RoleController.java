package com.tmt.system.web;

import java.util.List;
import java.util.Map;

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
import com.tmt.common.entity.LabelVO;
import com.tmt.common.persistence.Page;
import com.tmt.common.persistence.PageParameters;
import com.tmt.common.persistence.QueryCondition;
import com.tmt.common.persistence.QueryCondition.Criteria;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.StringUtil3;
import com.tmt.common.utils.WebUtils;
import com.tmt.common.web.BaseController;
import com.tmt.system.entity.Office;
import com.tmt.system.entity.Role;
import com.tmt.system.entity.Role.DataScope;
import com.tmt.system.service.OfficeServiceFacade;
import com.tmt.system.service.RoleServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 角色管理
 * @author root
 *
 */
@Controller
@RequestMapping(value = "${spring.application.web.admin}/system/role")
public class RoleController extends BaseController {

	@Autowired
	private RoleServiceFacade roleService;
	@Autowired
	private OfficeServiceFacade officeService;
	
	/**
	 * 列表
	 * @param role
	 * @param model
	 * @return
	 */
	@RequestMapping("list")
	public String list(Role role,Model model){
		if(role != null && role.getOfficeId() != null) {
		   model.addAttribute("officeId", role.getOfficeId());
		}
		return "/system/RoleList";
	}
	
	/**
	 * 列表数据
	 * @param role
	 * @param model
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Role role,Model model,Page page) {
		QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		if(role != null && role.getOfficeId() != null) {
		   c.andEqualTo("R.OFFICE_ID", role.getOfficeId());
		}
		if(role != null && role.getName() != null && !StringUtil3.isBlank(role.getName()) ) {
		   c.andLike("R.NAME", role.getName());
		}
		qc.setOrderByClause("R.CODE");
		page = this.roleService.queryForPage(qc, param);
		return page;
	}
	
	/**
	 * 表单页面
	 * @param role
	 * @param model
	 * @return
	 */
	@RequestMapping("form")
	public String form(Role role,Model model) {
		if (role != null && role.getId() != null) {
			role = this.roleService.get(role.getId());
			//组织结构
			Office office = officeService.get(role.getOfficeId());
			if (office != null) {
				role.setOfficeId(office.getId());
				role.setOfficeName(office.getName());
			}
		}else {
			if(role == null) {
				role = new Role();
			}
			if(role.getOfficeId() != null && StringUtil3.isNotBlank(role.getOfficeId().toString())) {
				Office office = officeService.get(role.getOfficeId());
				role.setOfficeId(office.getId());
				role.setOfficeName(office.getName());
			} else {
				Office office = new Office();
				role.setOfficeId(office.getId());
				role.setOfficeName(office.getName());
			}
			role.setId(IdGen.INVALID_ID);
		}
		// 数据范围
		List<LabelVO> labels = Lists.newArrayList();
		for(DataScope scope: DataScope.values()) {
			labels.add(LabelVO.newLabel(scope.getName(),scope.name()));
		}
		model.addAttribute("scopes", labels);
		model.addAttribute("role", role);
		model.addAttribute("officeId", role.getOfficeId());
		return "/system/RoleForm";
	}
	
	/**
	 * 保存
	 * @param role
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Role role, Model model, RedirectAttributes redirectAttributes ){
		role.userOptions(UserUtils.getUser());
		roleService.save(role);
		UserUtils.removeAllCache();
		addMessage(redirectAttributes, "保存角色'" + role.getName() + "'成功");
		redirectAttributes.addAttribute("id", role.getId());
		return WebUtils.redirectTo(new StringBuilder(Globals.adminPath).append("/system/role/form").toString());
	}
	
	/**
	 * 删除
	 * @param idList
	 * @param model
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete")
	public AjaxResult delete(Long[] idList , Model model,HttpServletResponse response) {
		List<Role> roles = Lists.newArrayList();
		for(Long id: idList) {
			Role role = new Role();
			role.setId(id);
			roles.add(role);
		}
		this.roleService.delete(roles);
		UserUtils.removeAllCache();
		return AjaxResult.success();
	}
	
	/**
	 * 角色树形选择
	 * @param extId
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("treeSelect")
	public List<Map<String, Object>> treeSelect(@RequestParam(required=false)String extId, HttpServletResponse response) {
		return this.roleService.roleTreeSelect();
	}
}