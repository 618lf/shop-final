package com.tmt.system.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.common.config.Globals;
import com.tmt.common.persistence.Page;
import com.tmt.common.persistence.PageParameters;
import com.tmt.common.persistence.QueryCondition;
import com.tmt.common.persistence.QueryCondition.Criteria;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.StringUtils;
import com.tmt.common.utils.WebUtils;
import com.tmt.common.web.BaseController;
import com.tmt.system.entity.Dict;
import com.tmt.system.service.DictServiceFacade;
import com.tmt.system.utils.UserUtils;

/**
 * 字典项设置
 * 
 * @author root
 */
@Controller
@RequestMapping(value = "${spring.application.web.admin}/system/dict")
public class DictController extends BaseController {

	@Autowired
	private DictServiceFacade dictService;

	/**
	 * 表单
	 * 
	 * @param dict
	 * @param model
	 * @return
	 */
	@RequestMapping("form")
	public String form(Dict dict, Model model) {
		if (dict != null && dict.getId() != null) {
			dict = this.dictService.get(dict.getId());
		} else {
			if (dict == null) {
				dict = new Dict();
			}
			dict.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("dict", dict);
		return "/system/DictForm";
	}

	/**
	 * 保存
	 * 
	 * @param dict
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Dict dict, Model model, RedirectAttributes redirectAttributes) {
		dict.userOptions(UserUtils.getUser());
		this.dictService.save(dict);
		addMessage(redirectAttributes, "保存字典项'" + dict.getName() + "'成功");
		redirectAttributes.addAttribute("id", dict.getId());
		return WebUtils.redirectTo(new StringBuilder(Globals.adminPath).append("/system/dict/form").toString());
	}

	/**
	 * 列表
	 * 
	 * @param dict
	 * @param model
	 * @return
	 */
	@RequestMapping("list")
	public String list() {
		return "/system/DictList";
	}

	/**
	 * 列表数据
	 * 
	 * @param dict
	 * @param model
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Dict dict, Page page) {
		QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		this.initQc(dict, c);
		return this.dictService.queryForPage(qc, param);
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
	public Boolean delete(Long[] idList, Model model, HttpServletResponse response) {
		List<Dict> dicts = Lists.newArrayList();
		Boolean bFalg = Boolean.TRUE;
		for (Long id : idList) {
			Dict dict = new Dict();
			dict.setId(id);
			dicts.add(dict);
		}
		this.dictService.delete(dicts);
		return bFalg;
	}

	/**
	 * 校验code
	 * 
	 * @param dict
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping("checkDictCode")
	public Boolean checkDictCode(Dict dict, Model model) {
		Boolean bFalg = Boolean.TRUE;
		int iCount = this.dictService.checkDictCode(dict);
		if (iCount > 0) {
			bFalg = Boolean.FALSE;
		}
		return bFalg;
	}

	// 查询条件
	private void initQc(Dict app, Criteria c) {
		if (StringUtils.isNotBlank(app.getName())) {
			c.andEqualTo("NAME", app.getName());
		}
	}
}