package com.tmt.shop.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.service.BaseService;
import com.tmt.shop.dao.EpaymentDao;
import com.tmt.shop.entity.Epayment;
import com.tmt.shop.exception.EpaymentErrorException;
import com.tmt.shop.utils.EpayUtils;
import com.tmt.system.entity.User;

/**
 * 企业支付 管理
 * @author 超级管理员
 * @date 2015-12-21
 */
@Service("shopEpaymentService")
public class EpaymentService extends BaseService<Epayment,Long> implements EpaymentServiceFacade{
	
	@Autowired
	private EpaymentDao epaymentDao;
	
	@Override
	protected BaseDaoImpl<Epayment, Long> getBaseDao() {
		return epaymentDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Epayment epayment) {
		this.insert(epayment);
	}
	
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Epayment> epayments) {
		this.batchDelete(epayments);
	}
	
	/**
	 * sn
	 * @param sn
	 * @return
	 */
	public Epayment findBySn(String sn) {
		return this.queryForObject("findBySn", sn);
	}
	
	/**
	 * 发起支付
	 * @param sn
	 * @param remarks
	 * @param amount
	 * @param user
	 * @param openId
	 */
	@Transactional
	public void pay(String sn, String remarks, BigDecimal amount, User user, Long epayId, String openId) {
		Epayment epayment = this.findBySn(sn);
		if (epayment != null) {
			throw new EpaymentErrorException("已经支付过,不能再发起支付，如有问题请联系系统维护人员！");
		}
		
		// 构建支付
		epayment = Epayment.cashingEpayment(sn, remarks, amount, user);
		epayment.setEpayId(epayId);
		epayment.setOpenid(openId);
		this.save(epayment);
		
		// 发起支付
		EpayUtils.pay(epayment);
		
		// 支付成功(即使这个地方错误了，也已经支付成功了，这样不行) --- 不能和前面的在一个事务里面
		// 开启新的事务
		this.updatePayResult(epayment);
	}
	
	/**
	 * 修改支付成功结果
	 * 开启新的事务
	 * @param epayment
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW) 
	public void updatePayResult(Epayment epayment) {
		try {
			this.update("updatePayResult", epayment);
		}catch(Exception e){
			logger.error("数据更新异常，但事务没有回滚！");
		}
	}
}
