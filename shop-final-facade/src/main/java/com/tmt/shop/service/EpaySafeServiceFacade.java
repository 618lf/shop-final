package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.EpaySafe;

/**
 * 企业支付安全设置 管理
 * @author 超级管理员
 * @date 2015-12-21
 */
public interface EpaySafeServiceFacade extends BaseServiceFacade<EpaySafe,Long> {
	
	/**
	 * 保存
	 */
	public void save(EpaySafe epaySafe);
	
	/**
	 * 删除
	 */
	public void delete(List<EpaySafe> epaySafes);
	
}
