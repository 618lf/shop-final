package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Epay;

/**
 * 支付账户 管理
 * @author 超级管理员
 * @date 2015-09-23
 */
public interface EpayServiceFacade extends BaseServiceFacade<Epay, Long> {
	
	/**
	 * 保存
	 */
	public void save(Epay epay);
	
	/**
	 * 删除
	 */
	public void delete(List<Epay> epays);
	
	/**
	 * 查询前端可用的在线支付方式
	 * @return
	 */
	public List<Epay> queryUseableEpays();
}
