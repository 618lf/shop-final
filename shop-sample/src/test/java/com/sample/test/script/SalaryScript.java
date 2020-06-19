package com.sample.test.script;

import java.math.BigDecimal;

import com.tmt.core.utils.BigDecimalUtil;

import groovy.lang.Script;

/**
 * 
 * 薪酬计算的表达式
 * 
 * @author lifeng
 */
public class SalaryScript extends Script {

	@Override
	public Object run() {
		return null;
	}

	/**
	 * 定义的添加函数
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public BigDecimal add(BigDecimal x, BigDecimal y) {
		return BigDecimalUtil.add(x, y);
	}
}