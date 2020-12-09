package com.tmt.shop.front;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.tmt.core.persistence.incrementer.UUIdGenerator;
import com.tmt.core.utils.BigDecimalUtil;
import com.tmt.core.utils.CacheUtils;
import com.tmt.core.utils.CookieUtils;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;
import com.tmt.shop.entity.Cart;
import com.tmt.shop.entity.CartItem;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.Product;
import com.tmt.shop.entity.Promotion;
import com.tmt.shop.entity.ShopConstant;
import com.tmt.system.utils.UserUtils;

/**
 * 订单服务类
 * @author root
 *
 */
public class OrderUtils {
	
	// ----------------在购物车页面只需要构建一个临时订单，不管用户是否登录--------------------------------
	/**
	 * 购物车中的所有商品（选中的）
	 * @param orderKey
	 * @param cart
	 * @return
	 */
	public static Order build(String orderKey, Cart cart, HttpServletResponse response) {
		Order order = new Order();
		List<CartItem> items = cart.getItems();
		if(items != null && items.size() != 0) {
		   for(CartItem item: items) {
			   if (item.getChecked() == 1) {
				   OrderItem _item = OrderItem.build(item);
				   order.addItem(_item);
			   }
		   }
		}
		order = Order.defaultOrder(order);
		
		// 构建临时订单
		if (StringUtils.isBlank(orderKey)) {
			orderKey = UUIdGenerator.uuid();
	        CookieUtils.setCookie(response, ShopConstant.ORDER_KEY, orderKey, -1);
		}
		
		putCache(orderKey, order);
		return order;
	}
	
	/**
	 * 删除缓存
	 * @param orderKey
	 * @param cart
	 * @return
	 */
	public static Order delete(String orderKey, HttpServletResponse response) {
		Order order = new Order();
		order = Order.defaultOrder(order);
		
		// 构建临时订单
		if (StringUtils.isBlank(orderKey)) {
			orderKey = UUIdGenerator.uuid();
	        CookieUtils.setCookie(response, ShopConstant.ORDER_KEY, orderKey, -1);
		}
		//加入缓存系统
		putCache(orderKey, order);
		return order;
	}
    
	/**
	 * 删除一个商品（购物车的）
	 * @param orderKey
	 * @param cartItem
	 * @return
	 */
	public static Order delete(String orderKey, CartItem cartItem, HttpServletResponse response) {
		Order order = OrderUtils.getCache(orderKey);
		if(order == null) { order = new Order();}
		List<OrderItem> items = order.getItems();
		if(items != null && items.size() != 0) {
		   List<OrderItem> copys = Lists.newArrayList();
		   for(OrderItem item: items) {
			   if(!cartItem.getId().equals(item.getCartItemId())
					   && item.getCartItemId() != null) {
				  copys.add(item);
			   }
		   }
		   order.setItems(copys);
		}
		order = Order.defaultOrder(order);
		// 构建临时订单
		if (StringUtils.isBlank(orderKey)) {
			orderKey = UUIdGenerator.uuid();
	        CookieUtils.setCookie(response, ShopConstant.ORDER_KEY, orderKey, -1);
		}
		//加入缓存系统
		putCache(orderKey, order);
		return order;
	}
	
	/**
	 * 根据产品创建一个订单
	 * @param product
	 * @return
	 */
	public static Order build(String orderKey, CartItem cartItem, HttpServletResponse response) {
		Order order = OrderUtils.getCache(orderKey);
		if(order == null) { order = new Order();}
		List<OrderItem> items = order.getItems();
		if(items != null && items.size() != 0) {
		   List<OrderItem> copys = Lists.newArrayList();
		   for(OrderItem item: items) {
			   if (!cartItem.getId().equals(item.getCartItemId()) && item.getCartItemId() != null) {
				   copys.add(item);
			   }
		   }
		   order.setItems(copys);
		}
		OrderItem item = OrderItem.build(cartItem);
		order.addItem(item);
		order = Order.defaultOrder(order);
		//加入缓存系统
		if (StringUtils.isBlank(orderKey)) {
		    orderKey = UUIdGenerator.uuid();
	        CookieUtils.setCookie(response, ShopConstant.ORDER_KEY, orderKey, -1);
	    }
		putCache(orderKey, order);
		return order;
	}
	
	//----------用户订单---------------------------------
	/**
	 * 根据产品创建一个订单 (一定有用户)
	 * @param product
	 * @return
	 */
	public static void build(Product product, Integer quantity, Promotion promotion) {
		OrderItem item = OrderItem.build(product, quantity);
		if (promotion != null) {
			item.setPromotions(promotion.getId());
		}
		Order order = new Order();
		order.addItem(item);
		order = Order.defaultOrder(order);
		//加入缓存系统
		putCache(null, order);
	}
	
	/**
	 * 折扣明细（前端查看）
	 * @return
	 */
	public static String discountDetail(Order order) {
		StringBuilder detail = new StringBuilder();
		if (BigDecimalUtil.biggerThenZERO(order.getPostageDiscount())) {
			detail.append("<span>").append("会员包邮：").append(BigDecimalUtil.toString(order.getPostageDiscount(), 2)).append("</span>");
		}
		if (BigDecimalUtil.biggerThenZERO(order.getRankDiscount())) {
			detail.append("<span>").append("会员折扣：").append(BigDecimalUtil.toString(order.getRankDiscount(),2)).append("</span>");
		}
		if (BigDecimalUtil.biggerThenZERO(order.getPromotionDiscount())) {
			detail.append("<span>").append("促销活动：").append(BigDecimalUtil.toString(order.getPromotionDiscount(),2)).append("</span>");
		}
		if (BigDecimalUtil.biggerThenZERO(order.getCouponDiscount())) {
			detail.append("<span>").append("优惠抵扣：").append(BigDecimalUtil.toString(order.getCouponDiscount(),2)).append("</span>");
		}
		return detail.toString();
	}
	
	//----------临时订单缓存系统---------------------------------
	public static void putCache(String orderKey, Order order) {
		if (StringUtils.isBlank(orderKey) && UserUtils.isUser()) {
			orderKey = UserUtils.getUser().getId().toString();
		}
		if (StringUtils.isNotBlank(orderKey)) {
			String key = new StringBuilder(ShopConstant.SHOP_USER_ORDER_CACHE).append(orderKey).toString();
			CacheUtils.getSessCache().put(key, order);
		}
	}
	//有用户的订单
	public static Order getCache() {
		if (UserUtils.isUser()) {
		    String key = new StringBuilder(ShopConstant.SHOP_USER_ORDER_CACHE).append(UserUtils.getUser().getId()).toString();
		    return CacheUtils.getSessCache().get(key);
		}
		return null;
	}
	public static void removeCache() {
		if (UserUtils.isUser()) {
		    String key = new StringBuilder(ShopConstant.SHOP_USER_ORDER_CACHE).append(UserUtils.getUser().getId()).toString();
		    CacheUtils.getSessCache().evict(key);
		}
	}
	//无用户的订单
	public static Order getCache(String orderKey) {
		if (StringUtils.isBlank(orderKey)) {
		    return OrderUtils.getCache();
		}
		String key = new StringBuilder(ShopConstant.SHOP_USER_ORDER_CACHE).append(orderKey).toString();
		return CacheUtils.getSessCache().get(key);
	}
	public static void removeCache(String orderKey) {
		String key = new StringBuilder(ShopConstant.SHOP_USER_ORDER_CACHE).append(orderKey).toString();
		CacheUtils.getSessCache().evict(key);
	}
}