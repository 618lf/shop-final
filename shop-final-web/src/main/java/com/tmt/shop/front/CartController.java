package com.tmt.shop.front;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.incrementer.UUIdGenerator;
import com.tmt.core.utils.CacheUtils;
import com.tmt.core.utils.CookieUtils;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.RegexpUtil;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;
import com.tmt.shop.entity.Cart;
import com.tmt.shop.entity.CartItem;
import com.tmt.shop.entity.Complex;
import com.tmt.shop.entity.ComplexProduct;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.Promotion;
import com.tmt.shop.entity.Receiver;
import com.tmt.shop.entity.ShopConstant;
import com.tmt.shop.service.CartServiceFacade;
import com.tmt.shop.service.ReceiverServiceFacade;
import com.tmt.shop.utils.ComplexUtils;
import com.tmt.shop.utils.PromotionUtils;
import com.tmt.system.entity.User;
import com.tmt.system.utils.UserUtils;

/**
 * 购物车
 * @author root
 */
@Controller("frontCartController")
@RequestMapping(value = "${frontPath}/shop/cart")
public class CartController {

	@Autowired
	private CartServiceFacade cartService;
	@Autowired
	private ReceiverServiceFacade receiverService;
	
	/**
	 * 列表页面
	 * @return
	 */
	@RequestMapping(value="list.html")
	public String list(Model model, HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> _model = this.list(request, response);
		model.addAllAttributes(_model);
		return "/front/goods/CartList";
	}
	
	/**
	 * 列表页面
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="list.json")
	public Map<String, Object> list(HttpServletRequest request, HttpServletResponse response) {
		Cart cart = null; String cartKey = CookieUtils.getCookie(request, ShopConstant.CART_KEY); String orderKey = CookieUtils.getCookie(request, ShopConstant.ORDER_KEY); Order order = null; User user = UserUtils.getUser();
		if (UserUtils.isGuest()) {
		    cart = this.cartService.getByKey(cartKey);
		} else {
		    if (StringUtils.isNotBlank(cartKey)) {
			    this.cartService.merge(cartKey, user);
			    CookieUtils.remove(request, response, ShopConstant.CART_KEY);
		    }
		    cart = this.cartService.getByUserId(user);
		}
		if (cart != null) {
		    cart.setItems(this.cartService.queryProductsByCartId(cart.getId()));
		}
		
		// 只需要获取临时订单(不用管用户订单)
		if (StringUtils.isNotBlank(orderKey)) {
			order = OrderUtils.getCache(orderKey);
		}
		
		// 清除临时订单支付方式和货运方式
		if (order != null && order.getPaymentMethod() != null) {
			order.initPaymentMethodObj(null);
		    order.initShippingMethodObj(null);
		    order.initReceiver(null);
		    Order.defaultOrder(order);
		    OrderUtils.putCache(orderKey, order);
		}
		
		// cart 构建订单
		if (cart != null && cart.getItems() != null && cart.getItems().size() != 0) {
			int checked = 1; List<CartItem> items = cart.getItems();
		    for(CartItem item: items) {
			    if (item.getChecked() != 1) {
				    checked = 0;
			    }
		    }
		    // 是否全选
		    cart.setChecked(checked);
		    
		    // 构建订单
		    order = OrderUtils.build(orderKey, cart, response);
		}
		
		// 清除缓存
		UserUtils.removeAttribute(ShopConstant.CART_QUANTITY);
		
		// 计算优惠
		PromotionUtils.calcula(order, user);
		Receiver receiver = this.receiverService.queryUserDefaultReceiver(user);
		Map<String, Object> model = Maps.newHashMap();
		model.put("cart", cart);
		model.put("amount", order!=null?order.getAmount():0);
		model.put("discount", order!=null?order.getPromotionDiscount():0);
		model.put("receiver", receiver);
		return model;
	}
	
	/**
	 * 购物车的数量
	 * @return
	 */
	@ResponseBody
	@RequestMapping("quantity.json")
	public AjaxResult quantity(HttpServletRequest request, HttpServletResponse response) {
		String cartKey = CookieUtils.getCookie(request, ShopConstant.CART_KEY);
		if (UserUtils.isGuest()) {
		    if (StringUtils.isBlank(cartKey)) {
			    cartKey = UUIdGenerator.uuid();
			    CookieUtils.setCookie(response, ShopConstant.CART_KEY, cartKey, 60*60*24*30);
		    } else {
		    	Integer count = CacheUtils.getSessCache().get(cartKey);
		    	if (count == null) {
		    		count = cartService.countQuantityByCartKey(cartKey);
		    		CacheUtils.getSessCache().put(cartKey, count);
		    		return AjaxResult.success(count);
		    	}
		    	return AjaxResult.success(count);
		    }
		} else {
		    User user = UserUtils.getUser();
		    if (StringUtils.isNotBlank(cartKey)) {
			    this.cartService.merge(cartKey, user);
			    CookieUtils.remove(request, response, ShopConstant.CART_KEY);
			    CacheUtils.getSessCache().delete(cartKey);
		    }
		    Integer count = UserUtils.getAttribute(ShopConstant.CART_QUANTITY);
		    if (count == null) {
		    	count = cartService.countQuantityByUserId(user);
		    	UserUtils.setAttribute(ShopConstant.CART_QUANTITY, count);
		    	return AjaxResult.success(count);
		    }
		    return AjaxResult.success(count);
		}
		return AjaxResult.success(0);
	}
	
	/**
	 * 添加购物车
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="add_complex.json", method = RequestMethod.POST)
	public AjaxResult addComplex(Long productId, HttpServletRequest request, HttpServletResponse response) {
		Cart cart =  null;  User user = null;
		String cartKey = CookieUtils.getCookie(request, ShopConstant.CART_KEY);
		if (UserUtils.isGuest()) { //未登录的用户
		    if (StringUtils.isBlank(cartKey)) {
			    cartKey = UUIdGenerator.uuid();
		        CookieUtils.setCookie(response, ShopConstant.CART_KEY, cartKey, 60*60*24*30); // 7天
		    }
		    cart = this.cartService.getByKey(cartKey);
		    CacheUtils.getSessCache().delete(cartKey);
		} else {
		    user = UserUtils.getUser();
		    cart = this.cartService.getByUserId(user);
		    UserUtils.removeAttribute(ShopConstant.CART_QUANTITY);
		}
		if (cart == null) {
		    cart = new Cart();
		    cart.setCartKey(cartKey);
		}
		if (user != null) {
			cart.userOptions(user);
		}
		
		// 将组合商品中的商品全部添加
		Complex complex = ComplexUtils.getComplex(productId);
		if (complex.getType() == 1) {
			this.cartService.add(cart, complex.getType(), productId, 1);
		} else {
			List<ComplexProduct> cps = complex.getProducts();
			for(ComplexProduct cp: cps) {
				this.cartService.add(cart, null, cp.getProductId(), cp.getQuantity());
			}
		}
		return AjaxResult.success();
	}
	
	/**
	 * 添加购物车
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="add.json", method = RequestMethod.POST)
	public AjaxResult addProduct(Long productId, HttpServletRequest request, HttpServletResponse response) {
		Cart cart =  null;  User user = null;
		String cartKey = CookieUtils.getCookie(request, ShopConstant.CART_KEY);
		if (UserUtils.isGuest()) { //未登录的用户
		    if (StringUtils.isBlank(cartKey)) {
			    cartKey = UUIdGenerator.uuid();
		        CookieUtils.setCookie(response, ShopConstant.CART_KEY, cartKey, 60*60*24*30); // 7天
		    }
		    cart = this.cartService.getByKey(cartKey);
		    CacheUtils.getSessCache().delete(cartKey);
		} else {
		    user = UserUtils.getUser();
		    cart = this.cartService.getByUserId(user);
		    UserUtils.removeAttribute(ShopConstant.CART_QUANTITY);
		}
		if (cart == null) {
		    cart = new Cart();
		    cart.setCartKey(cartKey);
		}
		if (user != null) {
			cart.userOptions(user);
		}
		
		// 商品数量
		String _quantity = WebUtils.getCleanParam("quantity");
		Integer quantity = StringUtils.isNotBlank(_quantity) && RegexpUtil.isNumber(_quantity) ? Integer.parseInt(_quantity) : 1;
		this.cartService.add(cart, null, productId, quantity);
		return AjaxResult.success();
	}
	
	/**
	 * 扣减购物车
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="reduce.json", method = RequestMethod.POST)
	public AjaxResult reduce(Long productId, HttpServletRequest request, HttpServletResponse response) {
		Cart cart =  null;  User user = null;
		String cartKey = CookieUtils.getCookie(request, ShopConstant.CART_KEY);
		if (UserUtils.isGuest()) { //未登录的用户
		    if (StringUtils.isBlank(cartKey)) {
			    cartKey = UUIdGenerator.uuid();
		        CookieUtils.setCookie(response, ShopConstant.CART_KEY, cartKey, 60*60*24*30); // 7天
		    }
		    cart = this.cartService.getByKey(cartKey);
		    CacheUtils.getSessCache().delete(cartKey);
		} else {
		    user = UserUtils.getUser();
		    cart = this.cartService.getByUserId(user);
		    UserUtils.removeAttribute(ShopConstant.CART_QUANTITY);
		}
		if (cart == null) {
		    cart = new Cart();
		    cart.setCartKey(cartKey);
		}
		if (user != null) {
			cart.userOptions(user);
		}
		
		// 商品数量
		this.cartService.reduce(cart, productId);
		return AjaxResult.success();
	}
	
	/**
	 * 删除商品
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/item/delete_one.json", method = RequestMethod.POST)
	public AjaxResult delete(Long id, HttpServletRequest request, HttpServletResponse response) {
		// 删除一个
		CartItem cartItem = new CartItem();
		cartItem.setId(id);
		this.cartService.delete(cartItem);
		 
		// 删除环寻
		if (UserUtils.isGuest()) { //未登录的用户
			String cartKey = CookieUtils.getCookie(request, ShopConstant.CART_KEY);
			CacheUtils.getSessCache().delete(cartKey);
		} else {
			UserUtils.removeAttribute(ShopConstant.CART_QUANTITY);
		}
		// 重置临时订单
		return this.resetCart(request, response);
	}
	
	// 重置临时订单
	private AjaxResult resetCart(HttpServletRequest request, HttpServletResponse response) {
		Cart cart = null; Order order = null; String orderKey = CookieUtils.getCookie(request, ShopConstant.ORDER_KEY);
		if (UserUtils.isGuest()) {
		    String cartKey = CookieUtils.getCookie(request, ShopConstant.CART_KEY);
		    cart = this.cartService.getByKey(cartKey);
		} else {
		    User user = UserUtils.getUser();
		    cart = this.cartService.getByUserId(user);
		    UserUtils.removeAttribute(ShopConstant.CART_QUANTITY);
		}
		if (cart != null) {
		    cart.setItems(this.cartService.queryProductsByCartId(cart.getId()));
			order = OrderUtils.build(orderKey, cart, response);
			return this.orderResult(order);
		}
		order = OrderUtils.getCache(orderKey);
		return this.orderResult(order);
	}
	
	/**
	 * 删除商品
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/item/delete.json", method = RequestMethod.POST)
	public AjaxResult delete(Long[] ids, HttpServletRequest request, HttpServletResponse response) {
		for (Long id: ids) {
			 CartItem cartItem = new CartItem();
			 cartItem.setId(id);
			 this.cartService.delete(cartItem);
		}
		// 删除环寻
		if (UserUtils.isGuest()) { //未登录的用户
			String cartKey = CookieUtils.getCookie(request, ShopConstant.CART_KEY);
			CacheUtils.getSessCache().delete(cartKey);
		} else {
			UserUtils.removeAttribute(ShopConstant.CART_QUANTITY);
		}
		// 取消临时订单
		return this.cancelCart(request, response);
	}
	
	/**
	 * 编辑一件商品，数量
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/edit.json", method = RequestMethod.POST)
	public AjaxResult edit(Long id, Integer quantity, HttpServletRequest request) {
		CartItem cartItem = new CartItem();
		cartItem.setId(id);
		cartItem.setQuantity(quantity);
		this.cartService.edit(cartItem);
		
		// 删除缓存
		if (UserUtils.isGuest()) {
			String cartKey = CookieUtils.getCookie(request, ShopConstant.CART_KEY);
			if (StringUtils.isNotBlank(cartKey)) {
				CacheUtils.getSessCache().delete(cartKey);
			}
		} else {
		    UserUtils.removeAttribute(ShopConstant.CART_QUANTITY);
		}
		return AjaxResult.success();
	}
	
	// ------------ 修改临时订单 --------
	/**
	 * 选择所有商品
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/all_select.json")
	public AjaxResult selectCart(HttpServletRequest request, HttpServletResponse response) {
		Cart cart = null; Order order = null; String orderKey = CookieUtils.getCookie(request, ShopConstant.ORDER_KEY);
		if (UserUtils.isGuest()) {
		    String cartKey = CookieUtils.getCookie(request, ShopConstant.CART_KEY);
		    cart = this.cartService.getByKey(cartKey);
		} else {
		    User user = UserUtils.getUser();
		    cart = this.cartService.getByUserId(user);
		    UserUtils.removeAttribute(ShopConstant.CART_QUANTITY);
		}
		if (cart != null) {
			this.cartService.allSelected(cart);
		    cart.setItems(this.cartService.queryProductsByCartId(cart.getId()));
			order = OrderUtils.build(orderKey, cart, response);
			return this.orderResult(order);
		}
		order = OrderUtils.getCache(orderKey);
		return this.orderResult(order);
	}
	
	/**
	 * 取消选择所有商品
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/all_cancel.json")
	public AjaxResult cancelCart(HttpServletRequest request, HttpServletResponse response) {
		Cart cart = null; Order order = null; String orderKey = CookieUtils.getCookie(request, ShopConstant.ORDER_KEY);
		if (UserUtils.isGuest()) {
		    String cartKey = CookieUtils.getCookie(request, ShopConstant.CART_KEY);
		    cart = this.cartService.getByKey(cartKey);
		    if (StringUtils.isNotBlank(cartKey)) {
				CacheUtils.getSessCache().delete(cartKey);
			}
		} else {
		    User user = UserUtils.getUser();
		    cart = this.cartService.getByUserId(user);
		}
		if (cart != null) {
			this.cartService.allCanceled(cart);
			order = OrderUtils.delete(orderKey, response);
			OrderResult result = new OrderResult();
			result.setOrder(order);
			return AjaxResult.success(result);
		}
		order = OrderUtils.getCache(orderKey);
		OrderResult result = new OrderResult();
		result.setOrder(order);
		return AjaxResult.success(result);
	}
	
	/**
	 * 选择一件商品
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/item/select.json")
	public AjaxResult selectItem(Long id, HttpServletRequest request, HttpServletResponse response) {
		Order order = null; String orderKey = CookieUtils.getCookie(request, ShopConstant.ORDER_KEY);
		CartItem cartItem = this.cartService.getProductByCartItemId(id);
		if (cartItem != null) {
			cartItem.setChecked(Cart.YES);
			this.cartService.checked(cartItem);
			order = OrderUtils.build(orderKey, cartItem, response);
		} else {
			order = OrderUtils.getCache(orderKey);
		}
		return this.orderResult(order);
	}
	
	/**
	 * 取消选择一件商品
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/item/cancel.json")
	public AjaxResult cancelItem(Long id, HttpServletRequest request, HttpServletResponse response) {
		Order order = null; String orderKey = CookieUtils.getCookie(request, ShopConstant.ORDER_KEY);
		CartItem cartItem = this.cartService.getProductByCartItemId(id);
		if (cartItem != null) {
			cartItem.setChecked(Cart.NO);
			this.cartService.checked(cartItem);
			order = OrderUtils.delete(orderKey, cartItem, response);
		} else {
			order = OrderUtils.getCache(orderKey);
		}
		return this.orderResult(order);
	}
	
	/**
	 * 切换优惠
	 * @param id
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/item/change_pm.json")
	public AjaxResult promotionChange(Long id, Long productId, Long promotionId, HttpServletRequest request, HttpServletResponse response) {
		CartItem cartItem = new CartItem();
    	cartItem.setId(id); cartItem.setPromotions(promotionId);
    	this.cartService.changePm(cartItem);
    	Promotion promotion = null;
		if (promotionId != null) {
			Map<Long, Promotion> promotions = PromotionUtils.queryProductAllEnabledPromotions(productId);
	        if (!promotions.isEmpty() && promotions.containsKey(promotionId)) {
	        	promotion = promotions.get(promotionId);
	        }
        }
		if (promotionId == null) {
			return AjaxResult.success();
		} else if(promotionId != null && promotion == null) {
			return AjaxResult.error("无效的促销，请重新设置");
		}
        return AjaxResult.success(promotion);
	}
	
	// 购物车的变化引起临时订单的变化
	private AjaxResult orderResult(Order order) {
		User user = UserUtils.getUser();
		PromotionUtils.calcula(order, user);
		OrderResult result = new OrderResult();
		result.setOrder(order);
		result.setDiscount(order.getPromotionDiscount());
		return AjaxResult.success(result);
	}
}