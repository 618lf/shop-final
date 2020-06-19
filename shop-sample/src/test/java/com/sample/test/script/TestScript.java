package com.sample.test.script;

import java.util.Map;

import com.google.common.collect.Maps;

public class TestScript {

	public static void main(String[] args) {

		// 表达式执行器
		SalaryScriptExecutor executor = new SalaryScriptExecutor();

		// DSL
		Map<String, Object> context = Maps.newHashMap();
		context.put("x", 18);
		Object object = executor.execute("(1+2+3+1111222.0) * add(1,2) * 123.01 * x", context);
		System.out.println(object.toString());
	}
}
