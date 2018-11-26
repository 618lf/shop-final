package com.tmt.system.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tmt.common.config.Globals;
import com.tmt.common.utils.WebUtils;

/**
 * 访问首页
 * 
 * @author lifeng
 */
@Controller
@RequestMapping(value = "")
public class IndexController {

	/**
	 * 首页
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("")
	public String index(HttpServletRequest request, HttpServletResponse response) {
		return WebUtils.redirectTo(Globals.adminPath);
	}
}