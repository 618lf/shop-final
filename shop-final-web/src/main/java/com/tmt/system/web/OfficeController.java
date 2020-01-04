package com.tmt.system.web;

import java.util.HashMap;
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

import com.tmt.core.config.Globals;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.entity.TreeVO;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;
import com.tmt.core.web.BaseController;
import com.tmt.system.entity.Office;
import com.tmt.system.service.OfficeServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 组织管理
 * 
 * @author root
 *
 */
@Controller
@RequestMapping(value = "${spring.application.web.admin}/system/office")
public class OfficeController extends BaseController {

	@Autowired
	private OfficeServiceFacade officeService;

	/**
	 * 列表
	 * 
	 * @param office
	 * @param model
	 * @return
	 */
	@RequestMapping("list")
	public String list(Office office, Model model) {
		if (office != null && office.getId() != null) {
			model.addAttribute("id", office.getId());
		}
		return "/system/OfficeList";
	}

	/**
	 * 列表数据
	 * 
	 * @param office
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Office office, Model model) {
		Map<String, Object> params = new HashMap<String, Object>();
		if (office != null && !StringUtils.isBlank(office.getName())) {
			params.put("ORG_NAME", office.getName());
			QueryCondition qc = new QueryCondition();
			Criteria c = qc.getCriteria();
			c.andLike("NAME", office.getName());
			List<Office> offices = this.officeService.queryByCondition(qc);
			if (offices != null && offices.size() != 0) {
				StringBuffer sb = new StringBuffer(",");
				for (Office orgItem : offices) {
					sb.append(orgItem.getParentIds());
					sb.append(orgItem.getId()).append(",");
				}
				sb.append("-1");
				params.clear();
				params.put("ORG_IDS", sb.toString());
				// 选中
				office = new Office();
				office.setId(offices.get(0).getId());
			}
		}
		List<TreeVO> trees = this.officeService.findTreeList(params);
		trees = TreeVO.sort(trees);
		if (trees != null && trees.size() != 0 && office != null && office.getId() != null && office.getId() != null) {
			office = this.officeService.get(office.getId());
			if (office != null) {
				for (TreeVO treeVO : trees) {
					if (("," + office.getParentIds()).indexOf("," + treeVO.getId() + ",") != -1) {
						treeVO.setExpanded(Boolean.TRUE);
					}
				}
			}
		} else if (trees != null && trees.size() != 0) {
			trees.get(0).setExpanded(Boolean.TRUE);
		}
		Page pageList = new Page();
		pageList.setData(trees);
		return pageList;
	}

	/**
	 * 表单页面
	 * 
	 * @param office
	 * @param model
	 * @return
	 */
	@RequestMapping("form")
	public String form(Office office, Model model) {
		Office parent = null;
		if (office != null && office.getId() != null && office.getId() != null) {
			office = this.officeService.get(office.getId());
		} else {
			office.setId(IdGen.INVALID_ID);
			if (office.getParentId() == null) {
				office.setParentId(IdGen.ROOT_ID);
			}
		}
		parent = this.officeService.get(office.getParentId());
		if (parent != null) {
			office.setParentId(parent.getId());
			office.setParentName(parent.getName());
		}
		// 组织类型
		model.addAttribute("office", office);
		return "/system/OfficeForm";
	}

	/**
	 * 选择
	 * 
	 * @param extId
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("treeSelect")
	public List<Map<String, Object>> treeSelect(@RequestParam(required = false) String extId,
			HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<TreeVO> trees = this.officeService.findTreeList(new HashMap<String, Object>());
		for (int i = 0; i < trees.size(); i++) {
			TreeVO e = trees.get(i);
			if (extId == null || (extId != null && !extId.equals(e.getId().toString())
					&& e.getParentIds().indexOf("," + extId + ",") == -1)) {
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParent());
				map.put("name", e.getTreeName());
				mapList.add(map);
			}
		}
		return mapList;
	}

	/**
	 * 保存
	 * 
	 * @param office
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Office office, Model model, RedirectAttributes redirectAttributes) {
		office.userOptions(UserUtils.getUser());
		Long Id = this.officeService.save(office);
		addMessage(redirectAttributes, "保存菜单'" + office.getName() + "'成功");
		redirectAttributes.addAttribute("Id", Id);
		return WebUtils.redirectTo(new StringBuilder(Globals.adminPath).append("/system/office/form").toString());
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
		List<Office> offices = Lists.newArrayList();
		Office oneParent = null;
		for (Long id : idList) {
			Office org = new Office();
			org.setId(id);
			offices.add(org);
			if (oneParent == null) {
				oneParent = new Office();
				oneParent.setId(org.getParentId());
			}
		}
		Boolean bFalg = this.officeService.delete(offices);
		if (!bFalg) {// 删除失败
			return AjaxResult.error("要删除的组织结构存在子组织结构或存在用户!");
		}
		return AjaxResult.success(oneParent);
	}
}