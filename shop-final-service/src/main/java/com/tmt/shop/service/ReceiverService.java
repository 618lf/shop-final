package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.shop.dao.ReceiverDao;
import com.tmt.shop.entity.Receiver;
import com.tmt.system.entity.User;

/**
 * 会员地址 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Service("shopReceiverService")
public class ReceiverService extends BaseService<Receiver,Long> implements ReceiverServiceFacade{
	
	@Autowired
	private ReceiverDao receiverDao;
	
	@Override
	protected BaseDaoImpl<Receiver, Long> getBaseDao() {
		return receiverDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Receiver receiver) {
		if (receiver.getIsDefault() == null) {
		    receiver.setIsDefault(Receiver.NO);
		}
		if (IdGen.isInvalidId(receiver.getId())) {
			this.insert(receiver);
		} else {
			this.update(receiver);
		}
		if (Receiver.YES == receiver.getIsDefault()) { 
		    this.update("updateNoDefault", receiver);
		}
	}
	
	/**
	 * 设置默认
	 * @param receiver
	 */
	@Transactional
	public void def(Receiver receiver) {
		receiver.setIsDefault(Receiver.YES);
		//把当前的地址设置为默认
		this.update("updateDefault", receiver);
		//把其他的地址设置为不是默认
		this.update("updateNoDefault", receiver);
	}
	
	/**
	 * 用户地址
	 * @param user
	 * @return
	 */
	public List<Receiver> queryUserReceivers(User user){
		return this.queryForList("queryUserReceivers", user.getId());
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Receiver> receivers) {
		this.batchDelete(receivers);
	}
	
	/**
	 * 用户收货地址
	 * @return
	 */
	public Receiver queryUserDefaultReceiver(User user) {
		return this.queryForObject("queryUserDefaultReceiver", user.getId());
	}
}
