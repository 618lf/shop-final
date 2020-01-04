package com.tmt.core.groovy;


/**
 * 验证的表达式
 * @author root
 *
 */
public class ValidateScriptExecutor extends ScriptExecutor{

	@Override
	protected String getBaseScriptClass() {
		return ValidateScript.class.getName();
	}
}