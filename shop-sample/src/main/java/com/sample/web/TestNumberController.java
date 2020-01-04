package com.sample.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.entity.Result;
import com.tmt.core.utils.FreemarkerUtils;
import com.tmt.core.utils.Maps;
import com.tmt.system.service.NumberGeneratorFacade;

/**
 * 测试
 * 
 * @author lifeng
 */
@Controller
@RequestMapping("/api/test")
public class TestNumberController {

	@Autowired
	private NumberGeneratorFacade numberGenerator;

	/**
	 * 获取增加的列
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("get")
	public Result num() {
		Long num = numberGenerator.generateNumber("LIFENG-TEST");
		return Result.success(num);
	}

	/**
	 * freemarker 测试
	 * 
	 * @return
	 */
	@RequestMapping("400")
	public String _400() {
		return "/front/400";
	}

	/**
	 * freemarker 测试
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("html")
	public String html() {
		Map<String, Object> model = Maps.newHashMap();
		model.put("name", "123");
		String html = null;
		try {
			html = FreemarkerUtils.processUseTemplate("/front/400.html", model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return html;
	}
}
