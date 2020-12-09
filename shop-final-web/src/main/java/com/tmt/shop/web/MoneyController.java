package com.tmt.shop.web;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.entity.AjaxResult;
import com.tmt.core.utils.BigDecimalUtil;

/**
 * 提供金额的计算
 * @author 超级管理员
 * @date 2016-05-26
 */
@Controller("shopMoneyController")
@RequestMapping(value = "${adminPath}/shop/money")
public class MoneyController {

	/**
	 * 计算和
	 */
	@ResponseBody
	@RequestMapping("add")
	public AjaxResult add(Double[] moneys) {
		BigDecimal total = BigDecimal.ZERO;
		for(Double d: moneys) {
			total = BigDecimalUtil.add(total, BigDecimal.valueOf(d));
		}
		return AjaxResult.success(total);
	}
}