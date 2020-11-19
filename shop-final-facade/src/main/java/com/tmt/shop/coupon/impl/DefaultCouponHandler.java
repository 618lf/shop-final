package com.tmt.shop.coupon.impl;

import java.math.BigDecimal;
import java.util.List;

import com.tmt.core.utils.BigDecimalUtil;
import com.tmt.shop.coupon.CouponHandler;
import com.tmt.shop.entity.Coupon;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;

/**
 * 默认的验证规则
 * 
 * @author root
 *
 */
public class DefaultCouponHandler implements CouponHandler {

	/**
	 * 依据
	 * 
	 * @param code
	 * @param order
	 * @return
	 */
	protected BigDecimal getAmount(Coupon coupon, Order order) {
		// 计算的依据
		BigDecimal _amount = null;

		// 计算指定商品的总金额和数量
		if (coupon.getPs() != null && !coupon.getPs().isEmpty()) {
			List<OrderItem> items = order.getItems();
			for (OrderItem _item : items) {
				if (coupon.getPs().contains(_item.getProductId())) {
					_amount = BigDecimalUtil.add(_amount, _item.getTotal());
				}
			}
		} else {
			_amount = order.getPrice();
		}
		return _amount;
	}

	/**
	 * 处理是否能使用此优惠券
	 */
	@Override
	public boolean doHandler(Coupon coupon, Order order) {

		// 订单为空
		if (order == null) {
			return Boolean.FALSE;
		}

		BigDecimal amount = this.getAmount(coupon, order);

		// 说明无购买指定的商品
		if (amount == null || BigDecimalUtil.equalZERO(amount)) {
			return Boolean.FALSE;
		}

		// 实际支付金额=订单金额 - 优惠
		// BigDecimal actualAmont = amount.subtract(order.getTempDiscount());

		// 可以使用(1、不限制金额且订单实际金额大于等于优惠券金额，2、设置了限制金额，且订单金额大于限制金额)
		if (coupon.getIsPrice() != null && (coupon.getIsPrice() == 0 || (coupon.getIsPrice() == 1
				&& coupon.getOrderPrice() != null && !BigDecimalUtil.biggerThen(coupon.getOrderPrice(), amount)))) {
			return Boolean.TRUE;
		}

		// 不能使用
		return Boolean.FALSE;
	}
}