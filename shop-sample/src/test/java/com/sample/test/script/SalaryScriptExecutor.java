package com.sample.test.script;

import java.util.Map;

import com.tmt.core.groovy.ScriptExecutor;

/**
 * 
 * 表达式执行器
 * 
 * @author lifeng
 */
public class SalaryScriptExecutor extends ScriptExecutor {

	@Override
	protected String getBaseScriptClass() {
		return SalaryScript.class.getName();
	}

	// 执行器
	private static SalaryScriptExecutor executor = new SalaryScriptExecutor();

	/**
	 * 执行表达式 use: Implements ScriptEx
	 * 
	 * @param expression
	 * @param params
	 * @return
	 */
	@Deprecated
	public static Object exec(String expression, Map<String, Object> params) {
		return executor.execute(expression, params);
	}
}