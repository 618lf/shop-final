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
import com.tmt.common.persistence.Page;
import com.tmt.common.persistence.PageParameters;
import com.tmt.common.persistence.QueryCondition;
import com.tmt.common.persistence.QueryCondition.Criteria;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.Maps;
import com.tmt.common.utils.StringUtil3;
import com.tmt.common.utils.WebUtils;
import com.tmt.common.web.BaseController;
import com.tmt.system.entity.Group;
import com.tmt.system.entity.Role;
import com.tmt.system.service.GroupServiceFacade;
import com.tmt.system.service.RoleServiceFacade;
import com.tmt.system.utils.UserUtils;


/**
 * 用户组管理
 * @author lifeng
 */
@Controller
@RequestMapping(value = "${adminPath}/system/group")
public class GroupController extends BaseController{

	@Autowired
	private GroupServiceFacade groupService;
	@Autowired
	private RoleServiceFacade roleService;
	
	/**
	 * 用户组初始化
	 * @return
	 */
	@RequestMapping("list")
	public String list(){
		return "/system/GroupList";
	}
	
	/**
	 * 用户组数据
	 * @param group
	 * @param model
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Group group, Model model, Page page){
		QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		if(StringUtil3.isNotBlank(group.getName())) {
		   c.andEqualTo("NAME", group.getName());
		}
		page = this.groupService.queryForPage(qc, param);
		return page;
	}
	
	/**
	 * 用户组编辑
	 * @param group
	 * @param model
	 * @return
	 */
	@RequestMapping("form")
	public String form(Group group, Model model) {
		if( group != null && !IdGen.isInvalidId(group.getId())) {
			group = this.groupService.get(group.getId());
			List<Role> roles = roleService.findByGroupId(group.getId());
			if( roles != null && roles.size() != 0) {
				StringBuffer roleIds = new StringBuffer();
				StringBuffer roleNames = new StringBuffer();
				for( Role r:roles ) {
					roleIds.append(r.getId()).append(",");
					roleNames.append(r.getName()).append(",");
				}
				group.setRoleIds(roleIds.toString());
				group.setRoleNames(roleNames.toString());
			}
		} else {
			if( group == null) {
				group = new Group();
			}
			group.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("group", group);
		return "/system/GroupForm";
	}
	
	/**
	 * 保存
	 * @param group
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Group group,Model model, RedirectAttributes redirectAttributes){
		group.userOptions(UserUtils.getUser());
		this.groupService.save(group);
		UserUtils.removeAllCache();
		addMessage(redirectAttributes, StringUtil3.format("%s'%s'%s", "修改用户组", group.getName(), "成功"));
		redirectAttributes.addAttribute("id", group.getId());
		return WebUtils.redirectTo(new StringBuilder(Globals.adminPath).append("/system/group/form").toString());
	}
	
	/**
	 * 删除用户组，删除所有的权限和关联用户
	 * @param idList
	 * @param model
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete")
	public AjaxResult delete(Long[] idList , Model model, HttpServletResponse response){
		List<Group> groups = Lists.newArrayList();
		if(idList != null && idList.length != 0) {
			for(Long id: idList) {
				Group group = new Group();
				group.setId(id);
				groups.add(group);
			}
			this.groupService.delete(groups);
			UserUtils.removeAllCache();
		}
		return AjaxResult.success();
	}
	
	/**
	 * 用户组树形选择
	 * @param extId
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("treeSelect")
	public List<Map<String, Object>> treeSelect(@RequestParam(required=false)String extId, HttpServletResponse response){
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Group> groups = this.groupService.getAll();
		for( int i= 0; i< groups.size(); i++ ) {
			Group group = groups.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", group.getId());
			map.put("pId", "O_-1");
			map.put("name", group.getName());
			mapList.add(map);
		}
		if(mapList.size() != 0) {
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", "O_-1");
			map.put("pId", "O_-2");
			map.put("name", "用户组");
			mapList.add(map);
		}
		return mapList;
	}
}