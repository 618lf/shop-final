package com.tmt.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.common.entity.Result;
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
}
