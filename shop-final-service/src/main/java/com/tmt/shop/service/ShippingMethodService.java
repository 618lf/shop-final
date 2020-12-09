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
import com.tmt.core.utils.Lists;
import com.tmt.shop.dao.PaymentShiopingMethodDao;
import com.tmt.shop.dao.ShippingMethodAreaDao;
import com.tmt.shop.dao.ShippingMethodDao;
import com.tmt.shop.entity.PaymentMethod;
import com.tmt.shop.entity.PaymentShiopingMethod;
import com.tmt.shop.entity.ShippingMethod;
import com.tmt.shop.entity.ShippingMethodArea;
import com.tmt.shop.entity.ShopConstant;

/**
 * 配送方式 管理
 * @author 超级管理员
 * @date 2016-01-20
 */
@Service("shopShippingMethodService")
public class ShippingMethodService extends BaseService<ShippingMethod,Long> implements ShippingMethodServiceFacade{
	
	@Autowired
	private ShippingMethodDao shippingMethodDao;
	@Autowired
	private PaymentShiopingMethodDao paymentShiopingMethodDao;
	@Autowired
	private ShippingMethodAreaDao shippingMethodAreaDao;
	
	@Override
	protected BaseDaoImpl<ShippingMethod, Long> getBaseDao() {
		return shippingMethodDao;
	}
	
	public ShippingMethod getWithAreas(Long id) {
		ShippingMethod shippingMethod = this.get(id);
		if (shippingMethod != null) {
			List<ShippingMethodArea> areas = this.shippingMethodAreaDao.queryForList("queryByShippingMethod", id);
			shippingMethod.setAreas(areas);
		}
		return shippingMethod;
	}
	
	public ShippingMethod getWithPaymentMethods(Long id) {
		ShippingMethod shippingMethod = this.get(id);
		if( shippingMethod != null) {
			List<PaymentShiopingMethod> paymentMethods = this.queryByShiopingMethodId(id);
			shippingMethod.setPaymentMethods(paymentMethods);
		}
		return shippingMethod;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(ShippingMethod shippingMethod) {
		if (IdGen.isInvalidId(shippingMethod.getId())) {
			this.insert(shippingMethod);
		} else {
			this.update(shippingMethod);
		}
		this.savePaymentMethods(shippingMethod);
		this.saveAreas(shippingMethod);
		clearCache();
	}
	
	// 保存区域配置
	private void saveAreas(ShippingMethod shippingMethod) {
		List<ShippingMethodArea> olds = this.shippingMethodAreaDao.queryForList("queryByShippingMethod", shippingMethod.getId());
		List<ShippingMethodArea> areas = shippingMethod.getAreas();
		for(int i= 0; i < areas.size(); i++) {
			ShippingMethodArea area = areas.get(i);
			area.setSort((byte)i);
			area.setShippingMethodId(shippingMethod.getId());
		}
		this.shippingMethodAreaDao.batchInsert(areas);
		this.shippingMethodAreaDao.batchDelete(olds);
	}
	
	
    @Transactional
	public void updateSort(List<ShippingMethod> shippingMethods ) {
		this.batchUpdate("updateSort", shippingMethods);
	}
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<ShippingMethod> shippingMethods) {
		this.batchDelete(shippingMethods);
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void savePaymentMethods(ShippingMethod shippingMethod) {
		List<PaymentShiopingMethod> olds = this.queryByShiopingMethodId(shippingMethod.getId());
		List<PaymentShiopingMethod> paymentMethods = shippingMethod.getPaymentMethods();
		for(PaymentShiopingMethod paymentMethod: paymentMethods) {
			paymentMethod.setShippingMethods(shippingMethod.getId());
		}
		this.paymentShiopingMethodDao.batchDelete(olds);
		this.paymentShiopingMethodDao.batchInsert(paymentMethods);
	}
	
	/**
	 * 根据配送方式查询关联的支付方式
	 * @return
	 */
	public List<PaymentShiopingMethod> queryByShiopingMethodId(Long shiopingMethodId) {
		return this.paymentShiopingMethodDao.queryForList("queryByShiopingMethodId", shiopingMethodId);
	}
	
	/**
	 * 得到指定的支付方式
	 * @param id
	 * @return
	 */
	public ShippingMethod getShippingMethod(Long id) {
		String key = new StringBuilder(ShopConstant.SHOP_SHIPPING_METHODS).append(id).toString();
		ShippingMethod shippingMethod = CacheUtils.getSysCache().get(key);
		if( shippingMethod == null) {
			shippingMethod = this.get(id);
			if( shippingMethod != null) {
				CacheUtils.getSysCache().put(key, shippingMethod);
			}
		}
		return shippingMethod;
	}
	
	/**
	 * 得到所有的配送方式(并查询出相应的支付方式)
	 * @return
	 */
	public List<ShippingMethod> queryShippingMethods() {
		String key = new StringBuilder(ShopConstant.SHOP_SHIPPING_METHODS).append("ALL").toString();
		List<ShippingMethod> methods = CacheUtils.getSysCache().get(key);
		if (methods == null) {
			QueryCondition qc = new QueryCondition();
			qc.setOrderByClause("SORT");
			List<ShippingMethod> shippingMethods = this.queryByCondition(qc);
			List<ShippingMethod> copys = Lists.newArrayList();
			for(ShippingMethod method: shippingMethods) {
				method = this.getWithPaymentMethods(method.getId());
				copys.add(method);
			}
			methods = copys;
			if(methods != null) {
			   CacheUtils.getSysCache().put(key, methods);
			}
		}
		return methods;
	}
	
	/**
	 * 获得默认的货运方式
	 * @param paymentMethod
	 * @return
	 */
	public ShippingMethod getDefaultShippingMethod(PaymentMethod paymentMethod) {
		List<ShippingMethod> methods = queryShippingMethods();
		for(ShippingMethod method: methods) {
			if(method.isSupport(paymentMethod)) {
			   return method;
			}
		}
		return null;
	}
	
	public static void clearCache() {
		String key = new StringBuilder(ShopConstant.SHOP_SHIPPING_METHODS).append("*").toString();
		CacheUtils.getSysCache().delete(key);
	}
}