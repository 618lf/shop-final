package com.tmt.system.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.common.persistence.Page;
import com.tmt.common.persistence.PageParameters;
import com.tmt.common.persistence.QueryCondition;
import com.tmt.common.persistence.QueryCondition.Criteria;
import com.tmt.common.utils.StringUtil3;
import com.tmt.common.web.BaseController;
import com.tmt.system.entity.Log;
import com.tmt.system.service.LogServiceFacade;

/**
 * 日志记录
 * 
 * @author root
 *
 */
@Controller
@RequestMapping(value = "${spring.application.web.admin}/system/log")
public class LogController extends BaseController {

	@Autowired
	private LogServiceFacade logService;

	/**
	 * 列表页面
	 */
	@RequestMapping("list")
	public String list() {
		return "/system/LogList";
	}

	/**
	 * 列表数据
	 * 
	 * @param log
	 * @param model
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Log log, Model model, Page page) {
		QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		qc.setOrderByClause(" CREATE_DATE DESC ");
		Criteria c = qc.getCriteria();
		if (StringUtil3.isNotBlank(log.getCreateName())) {
			c.andEqualTo("CREATE_ID", log.getCreateName());
		}
		if (log.getType() != null) {
			c.andEqualTo("TYPE", log.getType());
		}
		if (StringUtil3.isNotBlank(log.getRemoteAddr())) {
			c.andEqualTo("REMOTE_ADDR", log.getRemoteAddr());
		}
		if (StringUtil3.isNotBlank(log.getRequestUri())) {
			c.andEqualTo("REQUEST_URI", log.getRequestUri());
		}
		if (StringUtil3.isNotBlank(log.getUserAgent())) {
			c.andEqualTo("USER_AGENT", log.getUserAgent());
		}
		page = this.logService.queryForPage(qc, param);
		return page;
	}

	/**
	 * 表单页面
	 * 
	 * @param log
	 * @param model
	 * @return
	 */
	@RequestMapping("form")
	public String form(Log log, Model model) {
		if (log != null && log.getId() != null) {
			log = this.logService.get(log.getId());
		}
		model.addAttribute("log", log);
		return "/system/LogForm";
	}
}