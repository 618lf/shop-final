package com.tmt.shop.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.service.BaseService;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.RangeUtils;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.shop.dao.CouponDao;
import com.tmt.shop.dao.CouponProductDao;
import com.tmt.shop.dao.OrderCouponDao;
import com.tmt.shop.entity.Coupon;
import com.tmt.shop.entity.CouponCode;
import com.tmt.shop.entity.CouponFission;
import com.tmt.shop.entity.CouponMini;
import com.tmt.shop.entity.CouponProduct;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderCoupon;
import com.tmt.shop.entity.ShopConstant;
import com.tmt.shop.exception.CouponErrorException;
import com.tmt.shop.utils.Codes;
import com.tmt.shop.utils.CouponUtils;
import com.tmt.system.entity.User;

/**
 * 优惠券 管理
 * @author 超级管理员
 * @date 2016-11-26
 */
@Service("shopCouponService")
public class CouponService extends BaseService<Coupon,Long> implements CouponServiceFacade{
	
	@Autowired
	private CouponDao couponDao;
	@Autowired
	private CouponProductDao productDao;
	@Autowired
	private CouponCodeService codeService;
	@Autowired
	private OrderCouponDao orderCouponDao;
	@Autowired
	private CouponFissionService fissionService;
	
	@Override
	protected BaseDao<Coupon, Long> getBaseDao() {
		return couponDao;
	}
	
	/**
	 * 保存
	 */
	@Transactional
	public void save(Coupon coupon) {
		if(IdGen.isInvalidId(coupon.getId())) {
			coupon.setGeted(0);
			coupon.setUsed(0);
			this.insert(coupon);
			// 分享型不需要
			if (coupon.getIsFission() == Coupon.NO) {
				this.generatorCodes(coupon);
			} else {
				this.generatorFission(coupon);
			}
		} else {
			this.update(coupon);
			CouponUtils.clearCache(coupon.getId());
		}
		this.saveProducts(coupon);
	}
	
	// 修改商品
	private void saveProducts(Coupon coupon) {
		List<CouponProduct> olds = this.queryProductByCouponId(coupon.getId());
	    List<CouponProduct> products = coupon.getProducts();
	    for(CouponProduct product: products) {
	    	product.setCoupons(coupon.getId());
	    }
	    this.productDao.batchDelete(olds);
	    this.productDao.batchInsert(products);
	}
	
	// 生成券码(仅仅保存时)
	private void generatorCodes(Coupon coupon) {
		int total = coupon.getTotal();
		List<CouponCode> codes = Lists.newArrayList();
		for(int i= 1; i <= total; i++) {
			String code = Codes.generatorCode();
			CouponCode _code = CouponCode.newCode(code);
			_code.setCoupon(coupon.getId());
			_code.setBeginDate(coupon.getBeginDate());
			_code.setEndDate(this.getEndDate(coupon, null));
			_code.setVal(coupon.getVal());
			codes.add(_code);
			if (i % 15 == 0) {
				codeService.save(codes);
				codes.clear();
			}
		}
		
		if (codes.size() > 0) {
			codeService.save(codes);
		}
		codes.clear();
		codes = null;
	}
	
	// 生成裂变(仅仅保存时)
	private void generatorFission(Coupon coupon) {
		int total = coupon.getTotal();
		List<CouponFission> codes = Lists.newArrayList();
		for(int i= 1; i <= total; i++) {
			CouponFission fission = new CouponFission();
			fission.setCoupon(coupon.getId());
			fission.setFissionNum(coupon.getFissionNum());
			fission.setVal(coupon.getVal());
			codes.add(fission);
			if (i % 15 == 0) {
				fissionService.save(codes);
				codes.clear();
			}
		}
		
		if (codes.size() > 0) {
			fissionService.save(codes);
		}
		codes.clear();
		codes = null;
	}
	
	// 计算过期时间
	public Date getEndDate(Coupon coupon, Date fetchDate) {
		if (coupon.getExpireType() == 0) {
			return coupon.getEndDate();
		} else if(coupon.getExpireType() == 2) {
			return coupon.getExpireDate();
		} else if(coupon.getExpireType() == 1 && fetchDate != null) {
			return DateUtils.addDays(fetchDate, coupon.getExpireDays());
		}
		return null;
	}
	
	/**
	 * 删除
	 */
	@Transactional
	public void delete(List<Coupon> coupons) {
		this.batchDelete(coupons);
		List<CouponProduct> products = Lists.newArrayList();
		for(Coupon coupon: coupons) {
			products.addAll(this.queryProductByCouponId(coupon.getId()));
			this.codeService.delete(coupon.getId());
		}
		this.productDao.batchDelete(products);
	}
	
	/**
	 * 更新领取数量
	 */
	@Transactional
	public void updateGeted(List<Coupon> coupons) {
		this.batchUpdate("updateGeted", coupons);
	}
	
	
	/**
	 * 查询优惠券关联的商品(简单版)
	 * @param couponId
	 * @return
	 */
	public List<CouponProduct> queryProductByCouponId(Long couponId) {
		return this.productDao.queryForList("queryProductByCouponId", couponId);
	}
	
	/**
	 * 查询优惠券关联的商品(包含商品信息)
	 * @param couponId
	 * @return
	 */
	public List<CouponProduct> queryRichProductByCouponId(Long couponId) {
		return this.productDao.queryForList("queryRichProductByCouponId", couponId);
	}
	
	/**
	 * 获取一个券码(下单人才会走这个服务)
	 * --- 支持分享型的获取
	 * @return
	 */
	@Transactional
	public CouponCode assignOneCode_supportFission(Long couponId, User user, Byte enable) {
		
		// 缓存数据
		Coupon coupon = CouponUtils.getCache(couponId);
		
		// 优惠券的有效期
		if (coupon == null || coupon.getStatus() == 1) {
			return null;
		}
		
		// 普通优惠券 -- 是否能使用，根据传入的状态
		if (coupon.getIsFission() == 0) {
			// 不能使用时提示什么时候可用
			String remarks = null;
			if (enable == 0) {
				remarks = StringUtils.format("预计%s可用", DateUtils.getFormatDate(DateUtils.addDays(DateUtils.getTodayTime(), ShopConstant.RECEIPT_DAYS), "MM月dd日"));
			}
			return this.assignOneCode(couponId, user, enable, remarks);
		}
		
		// 校验用户是否可以分享
		Boolean canAssigned = this.fissionService.canUserAssigned(coupon, user);
		if (!canAssigned) {
			return null;
		}
		
		// 锁定一张,如果已经领取完毕则返回
		CouponFission fission = fissionService.lockOneFission(coupon.getId());
		if (fission == null) {
			return null;
		}
		fission.setUserId(user.getId());
		fission.setUserName(user.getName());
		fission.setCreateDate(DateUtils.getTimeStampNow());
		fission.setIsEnabled(enable);
		this.fissionService.updateUserInfo(fission);
		this.update("updateGeted", coupon);
		
		// 裂变的结果
		int fissionNum = coupon.getFissionNum();
		List<Integer> fissions = RangeUtils.fission(fission.getVal(), fissionNum, 5); 
		List<CouponCode> codes = Lists.newArrayList(fissions.size());
		for(int i=0; i< fissions.size(); i++) {
			Integer val = fissions.get(i);
			String code = Codes.generatorCode();
			CouponCode _code = CouponCode.newCode(code);
			_code.setCoupon(coupon.getId());
			_code.setBeginDate(coupon.getBeginDate());
			_code.setEndDate(this.getEndDate(coupon, null));
			_code.setVal(val);
			_code.setFissionId(fission.getId());
			codes.add(_code);
			
			if (i % 15 == 0) {
				codeService.save(codes);
				codes.clear();
			}
		}
		if (codes.size() > 0) {
			codeService.save(codes);
		}
		return assignOneCode_supportFission(couponId, fission, user);
	}
	
	/**
	 * 分配一个裂变的红包
	 * --- 所有人都走这个服务
	 * --- 一次分享一个用户只能获取一个张
	 * @param couponId
	 * @param fissionId
	 * @param user
	 * @return
	 */
	@Transactional
	public CouponCode assignOneCode_supportFission(Long couponId, CouponFission fission, User user) {
		
		// 是否获取过(每人限制获取一份) -- 指定了分享包
		CouponCode fissionCode = codeService.fetchOneCodeByFission(user, fission.getId());
		if (fissionCode != null) {
			return fissionCode;
		}
				
		// 如果为空，则需要查询裂变壳
		if (couponId == null) {
			couponId = fission.getCoupon();
		}
		
		// 缓存数据
		Coupon coupon = CouponUtils.getCache(couponId);
		
		// 优惠券的有效期
		if (coupon == null || coupon.getStatus() == 1 || coupon.getIsFission() != 1) {
			return null;
		}
		
		// 校验用户是否可以领取(受领取总数的控制)
		Boolean canAssigned = this.codeService.canUserAssigned(coupon, user);
		if (!canAssigned) {
			return null;
		}
		
		// 锁定分裂红包壳 -- 防止订单数据变动，导致优惠券状态不能及时更新
		if (fission.getIsEnabled() == null) {
			fission = this.fissionService.getSimpleAndLock(fission.getId());
		}
		
		// 锁定一张,如果已经领取完毕则返回
		CouponCode one = codeService.lockOneCodeByFission(fission.getId());
		if (one == null) {
			return null;
		}
	
		// 不能使用时提示什么时候可用
		String remarks = null;
		if (fission.getIsEnabled() == 0) {
			remarks = StringUtils.format("预计%s可用", DateUtils.getFormatDate(DateUtils.addDays(fission.getCreateDate(), ShopConstant.RECEIPT_DAYS), "MM月dd日"));
		}
				
		// 分配优惠券
		this.assignOneCode(one, coupon, user, fission.getIsEnabled(), remarks);
		
		// 获取次数
		this.fissionService.updateGeted(fission);
		return one;
	}
	
	/**
	 * 获取一个券码(可以使用)
	 * @return
	 */
	@Transactional
	public CouponCode assignOneCode(Long couponId, User user) {
		return this.assignOneCode(couponId, user, Coupon.YES, null);
	}
	
	/**
	 * 获取一个券码 
	 * -- 可设置是否可以使用
	 * @return
	 */
	@Transactional
	public CouponCode assignOneCode(Long couponId, User user, Byte enable, String remarks) {
		
		// 缓存数据
		Coupon coupon = CouponUtils.getCache(couponId);
		
		// 校验是否可以获取
		Boolean canUserAssigned = this.canUserAssigned(coupon, user);
		if (!canUserAssigned) {
			return null;
		}
		
		// 锁定一张,如果已经领取完毕则返回
		CouponCode one = codeService.lockOneCode(couponId);
		if (one == null) {
			return null;
		}
		
		// 分配优惠券
		this.assignOneCode(one, coupon, user, enable, remarks);
		
		// 更新获取次数
		this.update("updateGeted", coupon);
		
		// 加上前台需要显示的字段
		one.setIsPrice(coupon.getIsPrice());
		one.setOrderPrice(coupon.getOrderPrice());
		return one;
	}
	
	/**
	 * 根据优惠码获取优惠券
	 * @param user
	 * @param code
	 * @return
	 */
	@Transactional
	public CouponCode assignOneCode(User user, String code){
		CouponCode couponCode = this.codeService.lockOneCodeByCode(code);
		if (couponCode == null || couponCode.getUserId() != null) {
			return null;
		}
		
		// 缓存数据
		Coupon coupon = CouponUtils.getCache(couponCode.getCoupon());
		
		// 校验是否可以获取
		Boolean canUserAssigned = this.canUserAssigned(coupon, user);
		if (!canUserAssigned) {
			return null;
		}
		
		// 分配优惠券
		this.assignOneCode(couponCode, coupon, user, Coupon.YES, null);
		
		// 更新获取次数
		this.update("updateGeted", coupon);
		
		return couponCode;
	}
	
	// 用户是否可以获取这中优惠券
	private Boolean canUserAssigned(Coupon coupon, User user) {
		
		// 裂变红包不支持这种方式的分配
		if (coupon == null || coupon.getStatus() == 1 || coupon.getIsFission() == 1) {
			return Boolean.FALSE;
		}
		
		// 校验用户是否可以领取
		Boolean canAssigned = this.codeService.canUserAssigned(coupon, user);
		if (!canAssigned) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	// 将这张优惠券分配给用户
	private void assignOneCode(CouponCode couponCode, Coupon coupon, User user, Byte enable, String remarks) {
		couponCode.setUserId(user.getId());
		couponCode.setUserName(user.getName());
		couponCode.setUserImg(user.getHeadimg());
		couponCode.setBeginDate(DateUtils.getTodayDate());
		couponCode.setEndDate(this.getEndDate(coupon, couponCode.getBeginDate()));
		couponCode.setCreateDate(DateUtils.getTimeStampNow());
		couponCode.setIsEnabled(enable);
		couponCode.setRemarks(enable == Coupon.YES?null:remarks); // 可用时不用设置提示
		this.codeService.updateUserInfo(couponCode);
	}
	
	/**
	 * 取消使用订单
	 * @param order
	 */
	@Transactional
	public void unuseCode(Order order) {
		CouponCode code = null;
		OrderCoupon orderCoupon = this.orderCouponDao.get(order.getId());
		if (orderCoupon != null && (code = this.codeService.get(orderCoupon.getCoupons())) != null) {
			code.setIsUsed(Coupon.NO);
			code.setUsedDate(null);
			boolean flag = this.codeService.updateUnUsed(code);
			if (flag) {
				Coupon coupon = new Coupon();
				coupon.setId(code.getCoupon());
				this.update("updateUnUsed", coupon);
			}
		}
	}
	
	/**
	 * 只能使用一次
	 * @param user
	 * @param codeId
	 */
	@Transactional
	public void useCode(Order order) {
		CouponMini _coupon = order.getCoupon();
		if (_coupon == null) {return;}
		CouponCode code = new CouponCode();
		code.setId(_coupon.getCouponCode());
		code.setUserId(order.getCreateId());
		code.setIsUsed(Coupon.YES);
		code.setUsedDate(DateUtils.getTimeStampNow());
		
		// 是否修改成功
		boolean flag = this.codeService.updateUsed(code);
		if (!flag) {
			throw new CouponErrorException("优惠码已经使用，请重新下单");
		}
		
		// 更新使用次数
		Coupon coupon = new Coupon();
		coupon.setId(_coupon.getCoupon());
		this.update("updateUsed", coupon);
		
		// 订单使用
		OrderCoupon orderCoupon = new OrderCoupon();
		orderCoupon.setCoupons(_coupon.getCouponCode());
		orderCoupon.setOrders(order.getId());
		orderCouponDao.insert(orderCoupon);
	}
	
	/**
	 * 订单相关的优惠券
	 * @param orderId
	 * @return
	 */
	public List<OrderCoupon> queryOrderCoupons(Long orderId) {
		return orderCouponDao.queryForList("queryOrderCoupons", orderId);
	}
	
	/**
	 * 查询用户可用的优惠券
	 * 时间和有效性过滤
	 * 避免多次获取，前端应缓存
	 *  --- 指定商品
	 *  相同类型的只需要返回一张
	 */
	@Override
	public List<CouponCode> queryUserEnabledCoupons(User user) {
		return this.codeService.queryUserEnabledCoupons(user);
	}

	/**
	 * 获取指定的优惠券
	 */
	@Override
	public CouponCode getUserEnabledCoupon(User user, Long codeId) {
		return this.codeService.getUserEnabledCoupon(user, codeId);
	}
	
	/**
	 * 分配裂变的红包
	 * 需要查询订单的状态
	 */
	@Transactional
	public CouponFission giveFissionRedenvelope(User user, Long fissionId) {
		CouponFission fission = this.fissionService.getSimpleAndLock(fissionId);
		CouponCode code = this.assignOneCode_supportFission(null, fission, user);
		fission = this.fissionService.get(fissionId);
		fission.setOneCode(code);
		return fission;
	}
	
	/**
	 * 将所有分裂的红包设置未可使用
	 */
	@Transactional
	public void enableFissions(List<CouponFission> fissions) {
		for(CouponFission fission: fissions) {
			this.codeService.enableFissionCoupons(fission.getId());
		}
		this.fissionService.enableCoupons(fissions);
	}
	
	/**
	 * 将所有优惠券设置为可使用
	 * @param coupons
	 */
	@Transactional
	public void enableCoupons(List<CouponCode> coupons) {
		this.codeService.enableCoupons(coupons);
	}
}