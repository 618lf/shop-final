package com.tmt.shop.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.CacheUtils;
import com.tmt.shop.dao.PaymentMethodDao;
import com.tmt.shop.entity.PaymentMethod;
import com.tmt.shop.entity.ShopConstant;

/**
 * 支付方式 管理
 * @author 超级管理员
 * @date 2015-11-04
 */
@Service("shopPaymentMethodService")
public class PaymentMethodService extends BaseService<PaymentMethod,Long> implements PaymentMethodServiceFacade{
	
	@Autowired
	private PaymentMethodDao paymentMethodDao;
	
	@Override
	protected BaseDaoImpl<PaymentMethod, Long> getBaseDao() {
		return paymentMethodDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(PaymentMethod paymentMethod) {
		if(IdGen.isInvalidId(paymentMethod.getId())) {
		   this.insert(paymentMethod);
		} else {
		   this.update(paymentMethod);
		}
		clearCache();
	}
	
    @Transactional
	public void updateSort(List<PaymentMethod> paymentMethods ) {
		this.batchUpdate("updateSort", paymentMethods);
	}
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<PaymentMethod> paymentMethods) {
		this.batchDelete(paymentMethods);
	}
	
	/**
	 * 得到指定的支付方式
	 * @param id
	 * @return
	 */
	public PaymentMethod getPaymentMethod(Long id) {
		String key = new StringBuilder(ShopConstant.SHOP_PAYMENT_METHODS).append(id).toString();
		PaymentMethod paymentMethod = CacheUtils.getSysCache().get(key);
		if(paymentMethod == null) {
		   paymentMethod = this.get(id);
		   if(paymentMethod != null) {
		      CacheUtils.getSysCache().put(key, paymentMethod);
		   }
		}
		return paymentMethod;
	}
	
	/**
	 * 获得默认的支付方式
	 * @return
	 */
	public PaymentMethod getDefaultPaymentMethod() {
		String key = new StringBuilder(ShopConstant.SHOP_PAYMENT_METHODS).append("DEF").toString();
		PaymentMethod paymentMethod = CacheUtils.getSysCache().get(key);
		if(paymentMethod == null) {
			List<PaymentMethod> methods = queryPaymentMethods();
			for(PaymentMethod method: methods) {
				if(method.getMethod() == PaymentMethod.Method.ON_LINE && method.getType() == PaymentMethod.Type.BEFORE_PAYED) {
				   paymentMethod = method; break;
				}
			}
			if(paymentMethod != null) {
				CacheUtils.getSysCache().put(key, paymentMethod);
			}
		}
		return paymentMethod;
	}
	
	/**
	 * 获得支付方式
	 * @return
	 */
	public List<PaymentMethod> queryPaymentMethods() {
		String key = new StringBuilder(ShopConstant.SHOP_PAYMENT_METHODS).append("ALL").toString();
		List<PaymentMethod> methods = CacheUtils.getSysCache().get(key);
		if(methods == null) {
		   QueryCondition qc = new QueryCondition();
		   qc.setOrderByClause("SORT");
		   methods = this.queryByCondition(qc);
		   if(methods != null) {
			  CacheUtils.getSysCache().put(key, methods);
		   }
		}
		return methods;
	}
	
	public static void clearCache() {
		String key = new StringBuilder(ShopConstant.SHOP_PAYMENT_METHODS).append("*").toString();
		CacheUtils.getSysCache().delete(key);
	}
}
