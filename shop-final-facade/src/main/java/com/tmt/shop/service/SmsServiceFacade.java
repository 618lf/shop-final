package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Sms;

/**
 * 短信配置 管理
 * @author 超级管理员
 * @date 2017-09-12
 */
public interface SmsServiceFacade extends BaseServiceFacade<Sms,Long> {
	
	/**
	 * 保存
	 */
	public void save(Sms sms);
	
	/**
	 * 删除
	 */
	public void delete(List<Sms> smss);
	
	/**
	 * 场景
	 * @param scene
	 * @return
	 */
	public Sms getByScene(Byte scene);
	
}