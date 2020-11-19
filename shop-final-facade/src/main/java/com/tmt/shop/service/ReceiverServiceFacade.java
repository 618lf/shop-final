package com.tmt.shop.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.shop.entity.Receiver;
import com.tmt.system.entity.User;

/**
 * 会员地址 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
public interface ReceiverServiceFacade extends BaseServiceFacade<Receiver,Long> {
	
	/**
	 * 保存
	 */
	public void save(Receiver receiver);
	
	/**
	 * 默认
	 */
	public void def(Receiver receiver);
	
	/**
	 * 删除
	 */
	public void delete(List<Receiver> receivers);
	
	/**
	 * 收货地址
	 * @param user
	 * @return
	 */
	public List<Receiver> queryUserReceivers(User user);
	
	/**
	 * 默认地址
	 * @return
	 */
	public Receiver queryUserDefaultReceiver(User user);
}
