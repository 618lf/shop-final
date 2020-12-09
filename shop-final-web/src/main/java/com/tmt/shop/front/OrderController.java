package com.tmt.shop.front;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.config.Globals;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.utils.BigDecimalUtil;
import com.tmt.core.utils.CookieUtils;
import com.tmt.core.utils.Ints;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;
import com.tmt.shop.check.OrderCheckResult;
import com.tmt.shop.coupon.CouponHandler;
import com.tmt.shop.coupon.impl.DefaultCouponHandler;
import com.tmt.shop.entity.Cart;
import com.tmt.shop.entity.Coupon;
import com.tmt.shop.entity.CouponCode;
import com.tmt.shop.entity.CouponMini;
import com.tmt.shop.entity.Epay;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.OrderPromotion;
import com.tmt.shop.entity.Payment;
import com.tmt.shop.entity.PaymentMethod;
import com.tmt.shop.entity.PaymentMethod.Type;
import com.tmt.shop.entity.Product;
import com.tmt.shop.entity.Promotion;
import com.tmt.shop.entity.Shipping;
import com.tmt.shop.entity.ShippingMethod;
import com.tmt.shop.entity.ShopConstant;
import com.tmt.shop.entity.Store;
import com.tmt.shop.entity.UserRank;
import com.tmt.shop.enums.ShippingStatus;
import com.tmt.shop.exception.CouponErrorException;
import com.tmt.shop.script.DeliveryScriptExecutor;
import com.tmt.shop.service.CartServiceFacade;
import com.tmt.shop.service.CouponCodeServiceFacade;
import com.tmt.shop.service.CouponServiceFacade;
import com.tmt.shop.service.OrderServiceFacade;
import com.tmt.shop.service.ProductServiceFacade;
import com.tmt.shop.service.ReceiverServiceFacade;
import com.tmt.shop.service.ShippingServiceFacade;
import com.tmt.shop.service.UserRankServiceFacade;
import com.tmt.shop.utils.CouponUtils;
import com.tmt.shop.utils.EpayUtils;
import com.tmt.shop.utils.PaymentMethodUtils;
import com.tmt.shop.utils.PromotionUtils;
import com.tmt.shop.utils.RankUtils;
import com.tmt.shop.utils.ShippingMethodUtils;
import com.tmt.shop.utils.StoreUtils;
import com.tmt.system.entity.User;
import com.tmt.system.utils.UserUtils;
import com.tmt.wechat.entity.App;
import com.tmt.wechat.utils.WechatUtils;

/**
 * 商城 -- 订单
 * 
 * 1. 加一个改进项，必须是会员才能购买商品
 * @author root
 */
@Controller("frontShopOrderController")
@RequestMapping(value = "${frontPath}/member/shop/order")
public class OrderController extends PaymentController {

	@Autowired
	private ProductServiceFacade productService;
	@Autowired
	private ReceiverServiceFacade receiverService;
	@Autowired
	private OrderServiceFacade orderService;
	@Autowired
	private ShippingServiceFacade shippingService;
	@Autowired
	private CouponServiceFacade couponService;
	@Autowired
	private CouponCodeServiceFacade couponCodeService;
	@Autowired
	private UserRankServiceFacade userRankService;
	@Autowired
	private CartServiceFacade cartService;
	
	private CouponHandler couponHandler = new DefaultCouponHandler();
	private DeliveryScriptExecutor scriptExecutor = new DeliveryScriptExecutor();
	
	/**
	 * 1. 第一步
	 * 轻松购（单个商品的购物模式）
	 * @return
	 */
	@RequestMapping(value = "easyBuy/{pid}.html")
	public String easyBuy(@PathVariable Long pid, Integer quantity, HttpServletRequest request, HttpServletResponse response) {
		Product product = productService.get(pid);
		if (product != null) {
			
			// 相关促销
			Map<Long, Promotion> _promotions = PromotionUtils.queryProductAllEnabledPromotions(pid);
			OrderUtils.build(product, quantity, Maps.getFirst(_promotions));
			
			// 删除临时订单标识
			CookieUtils.remove(request, response, ShopConstant.ORDER_KEY);
		}
		return WebUtils.redirectTo(Globals.getFrontPath(), "/member/shop/order/confirm.html");
	}
	
	/**
	 * 1. 第一步
	 * 轻松购（单个商品的购物模式）
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "easyBuy/{pid}.json")
	public AjaxResult easyBuy_ajax(@PathVariable Long pid, Integer quantity, HttpServletRequest request, HttpServletResponse response) {
		Product product = productService.get(pid);
		if (product != null) {
			// 相关促销
			Map<Long, Promotion> _promotions = PromotionUtils.queryProductAllEnabledPromotions(pid);
			OrderUtils.build(product, quantity, Maps.getFirst(_promotions));
			
			// 删除临时订单标识
			CookieUtils.remove(request, response, ShopConstant.ORDER_KEY);
		}
		return AjaxResult.success();
	}
	
	/**
	 * 2. 查询缓存中的订单
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "confirm.html", method = RequestMethod.GET)
	public String confirm(Model model, HttpServletRequest request, HttpServletResponse response) {
		
		// 店铺
		Store store = StoreUtils.getDefaultStore();
		
		// 当前用户
		User user = UserUtils.getUser();
		if (store != null && store.getOrderLimit() ==1) {
			UserRank rank = userRankService.getSimple(user.getId());
			if (rank == null || !rank.isEnabled()) {
				return "/front/member/OrderLimit";
			}
		}
		
		// 临时订单
		String orderKey = CookieUtils.getCookie(request, ShopConstant.ORDER_KEY);
		
		// 优先从临时订单中获取
		Order order = OrderUtils.getCache(orderKey);
		
		//读取收货地址(只读取默认的)
		if (order != null && StringUtils.isBlank(order.getPhone())) {
		    order.initReceiver(this.receiverService.queryUserDefaultReceiver(user));
		}
		
		//使用默认的支付方式和默认的配送方式(并计算价格)
		if (order != null && order.getPaymentMethod() == null) {
			PaymentMethod paymentMethod = PaymentMethodUtils.getDefaultPaymentMethod();
		    order.initPaymentMethodObj(paymentMethod);
		    order.initShippingMethodObj(ShippingMethodUtils.getDefaultShippingMethod(paymentMethod));
		}
		
		// 默认的配送时间（以后可以用户配置） --- 现在只有配送时间
		if (order != null && StringUtils.isBlank(order.getConsigneeTime())) {
			String shippingAlert = scriptExecutor.calculateDeliveryTimes(order);
			order.setConsigneeTime(shippingAlert);
		}
		
		// 初始化订单
		order = Order.defaultOrder(order);
		
		// 运费
		order.setFreight(ShippingMethodUtils.calculateFreight(order));
		
		// 计算会员折扣（以商品价格来计算）
		RankUtils.calcula(order, user);
		
		// 计算优惠（以商品价格来计算） -- 限购需要用户的信息
		PromotionUtils.calcula(order, user);
		
		// 刷新缓存（转为用户缓存订单）
		if (StringUtils.isNotBlank(orderKey)) {
			CookieUtils.remove(request, response, ShopConstant.ORDER_KEY);
			// 删除临时订单
			OrderUtils.removeCache(orderKey);
		}
		OrderUtils.putCache(null, order);
		
		// 设置前端显示
		model.addAttribute("promotions", Lists.newArrayList(order.getPromotions().values()));
		model.addAttribute("order", order);
		model.addAttribute("discountDetail", OrderUtils.discountDetail(order));
	    return "/front/member/OrderConfirm";
	}
	
	/**
	 * 2. 查询缓存中的订单
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "confirm.json", method = RequestMethod.GET)
	public AjaxResult confirm_ajax(HttpServletRequest request, HttpServletResponse response) {
		
		// 店铺
		Store store = StoreUtils.getDefaultStore();
		
		// 当前用户
		User user = UserUtils.getUser();
		if (store != null && store.getOrderLimit() ==1) {
			UserRank rank = userRankService.getSimple(user.getId());
			if (rank == null || !rank.isEnabled()) {
				return AjaxResult.error("限制购买");
			}
		}
		
		// 临时订单
		String orderKey = CookieUtils.getCookie(request, ShopConstant.ORDER_KEY);
		
		// 优先从临时订单中获取
		Order order = OrderUtils.getCache(orderKey);
		
		//读取收货地址(只读取默认的)
		if (order != null && StringUtils.isBlank(order.getPhone())) {
		    order.initReceiver(this.receiverService.queryUserDefaultReceiver(user));
		}
		
		//使用默认的支付方式和默认的配送方式(并计算价格)
		if (order != null && order.getPaymentMethod() == null) {
			PaymentMethod paymentMethod = PaymentMethodUtils.getDefaultPaymentMethod();
		    order.initPaymentMethodObj(paymentMethod);
		    order.initShippingMethodObj(ShippingMethodUtils.getDefaultShippingMethod(paymentMethod));
		}
		
		// 默认的配送时间（以后可以用户配置） --- 现在只有配送时间
		if (order != null && StringUtils.isBlank(order.getConsigneeTime())) {
			String shippingAlert = scriptExecutor.calculateDeliveryTimes(order);
			order.setConsigneeTime(shippingAlert);
		}
		
		// 初始化订单
		order = Order.defaultOrder(order);
		
		// 运费
		order.setFreight(ShippingMethodUtils.calculateFreight(order));
		
		// 计算会员折扣（以商品价格来计算）
		RankUtils.calcula(order, user);
		
		// 计算优惠（以商品价格来计算） -- 限购需要用户的信息
		PromotionUtils.calcula(order, user);
		
		// 刷新缓存（转为用户缓存订单）
		if (StringUtils.isNotBlank(orderKey)) {
			CookieUtils.remove(request, response, ShopConstant.ORDER_KEY);
			// 删除临时订单
			OrderUtils.removeCache(orderKey);
		}
		OrderUtils.putCache(null, order);
		
		// 设置前端显示
		Map<String, Object> model = Maps.newHashMap();
		model.put("promotions", Lists.newArrayList(order.getPromotions().values()));
		model.put("order", order);
		model.put("discountDetail", OrderUtils.discountDetail(order));
	    return AjaxResult.success(model);
	}
	
	/**
	 * 自定选择一个金额最大的优惠券
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/coupon/auto_select.json", method = RequestMethod.POST)
	public AjaxResult autoCoupon() {
		User user = UserUtils.getUser();
		Order order = OrderUtils.getCache();
		
		// 促销设置不能使用优惠券
		if (order != null && !order.isCouponAble()) {
			return AjaxResult.error("促销设置不能使用优惠券");
		}
		
		// 判断
		if (order != null && order.getItems() != null) {
			// 可用的优惠券 - 时间和有效性
			List<CouponCode> coupons = couponService.queryUserEnabledCoupons(user);
			Map<Long,Boolean> handlers = Maps.newHashMap();
			for(CouponCode code: coupons) {
				Coupon coupon = CouponUtils.getCache(code.getCoupon());
				Boolean flag = handlers.get(code.getCoupon());
				if (flag == null) {
					flag = couponHandler.doHandler(coupon, order);
					handlers.put(code.getCoupon(), flag);
				}
				
				// 可用
				if (flag) {
					CouponMini _coupon = new CouponMini();
					_coupon.setCoupon(code.getCoupon());
					_coupon.setCouponCode(code.getId());
					order.setCoupon(_coupon);
					// 设置优惠券优惠
					order.setCouponDiscount(BigDecimal.valueOf(code.getVal()));
					OrderUtils.putCache(null, order);
					
					// 返回成功的数据
					CouponResult result = new CouponResult();
					result.setId(code.getId());
					result.setName(coupon.getName());
					result.setVal(code.getVal());
					
					// 订单的返回
					result.setDiscount(order.getTempDiscount());
					result.setAmount(order.getTempAmount());
					result.setDetail(OrderUtils.discountDetail(order));
					return AjaxResult.success(result);
				}
			}
		}
		
		// 没有可用的优惠券
		return AjaxResult.error("没有可用的优惠券");
	}
	
	/**
	 * 选择优惠券
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/coupon/select.json", method = RequestMethod.GET)
	public AjaxResult couponSelect() {
		User user = UserUtils.getUser();
		Order order = OrderUtils.getCache();
		
		List<CouponResult> _coupons = Lists.newArrayList();
		
		// 订单无效
		if (order == null) {return AjaxResult.success(_coupons);}
		
		// 促销设置不能使用优惠券
		if (!order.isCouponAble()) {
			return AjaxResult.success(_coupons);
		}
		
		// 可用的优惠券 - 时间和有效性
		List<CouponCode> coupons = couponService.queryUserEnabledCoupons(user);
		
		Map<Long,Boolean> handlers = Maps.newHashMap();
		
		// 判断是否可以使用 - 订单金额
		for(CouponCode code: coupons) {
			
			Coupon coupon = CouponUtils.getCache(code.getCoupon());
			
			// 校验是否可用
			Boolean flag = handlers.get(code.getCoupon());
			if (flag == null) {
				flag = couponHandler.doHandler(coupon, order);
				handlers.put(code.getCoupon(), flag);
			}
			
			// 可用的才显示
			if (flag) {
				CouponResult result = new CouponResult();
				result.setId(code.getId());
				result.setName(coupon.getName());
				result.setVal(code.getVal());
				_coupons.add(result);
			}
		}
		return AjaxResult.success(_coupons);
	}
	
	/**
	 * 选择优惠券
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/coupon/select.json", method = RequestMethod.POST)
	public AjaxResult couponSelect(Long id) {
		Order order = OrderUtils.getCache(); CouponCode code = null; User user = UserUtils.getUser();
		// 不使用优惠券
		if (order != null && id == null) {
			order.setCoupon(null);
			order.setCouponDiscount(BigDecimal.ZERO);
			OrderUtils.putCache(null, order);
			
			// 返回成功的数据
			CouponResult result = new CouponResult();
			result.setDiscount(order.getTempDiscount());
			result.setAmount(order.getTempAmount());
			result.setDetail(OrderUtils.discountDetail(order));
			return AjaxResult.success(result);
		}
		// 使用优惠券
		else if (order != null && (code = this.couponService.getUserEnabledCoupon(user, id)) != null) {
			
			Coupon coupon = CouponUtils.getCache(code.getCoupon());
			
			// 验证可用性
			boolean flag = couponHandler.doHandler(coupon, order);
			if (flag) {
				CouponMini _coupon = new CouponMini();
				_coupon.setCoupon(code.getCoupon());
				_coupon.setCouponCode(code.getId());
				order.setCoupon(_coupon);
				// 设置优惠券优惠
				order.setCouponDiscount(BigDecimal.valueOf(code.getVal()));
				OrderUtils.putCache(null, order);
				
				// 返回成功的数据
				CouponResult result = new CouponResult();
				result.setId(code.getId());
				result.setName(coupon.getName());
				result.setVal(code.getVal());
				
				// 订单的返回
				result.setDiscount(order.getTempDiscount());
				result.setAmount(order.getTempAmount());
				result.setDetail(OrderUtils.discountDetail(order));
				return AjaxResult.success(result);
			}
			return AjaxResult.error("不可用");
		}
		return AjaxResult.error("优惠券不可用");
	}
	
	/**
	 * 默认自动去免邮费
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/postage/free.json", method = RequestMethod.POST)
	public AjaxResult postage() {
		User user = UserUtils.getUser(); Order order = OrderUtils.getCache(); UserRank rank = null;
		if (order != null && !order.isPostageAble()) {
			return AjaxResult.error("已使用促销包邮");
		}
		
		// 需要支付邮费， 用户有包邮次数
		if (order != null && BigDecimalUtil.biggerThenZERO(order.getFreight()) 
				&& (rank = this.userRankService.touch(user.getId())) != null && rank.getUseAble() != 0 
				&& Ints.nullToZero(rank.getShipping()) != 0
				&& (Ints.nullToZero(rank.getShipping()) == -1 || rank.getRemainShipping() >= 1 )) {
			order.setPostageDiscount(order.getFreight());
			OrderUtils.putCache(null, order);
			// 返回成功的数据
			PostageResult result = new PostageResult();
			if (Ints.nullToZero(rank.getShipping()) == -1) {
				result.setPostage("全年包邮");
			} else {
				result.setPostage(StringUtils.format("还剩%s次", rank.getRemainShipping()));
			}
			// 订单的返回
			result.setDiscount(order.getTempDiscount());
			result.setAmount(order.getTempAmount());
			result.setDetail(OrderUtils.discountDetail(order));
			return AjaxResult.success(result);
		}
		return AjaxResult.error("不可用");
	}
	
	/**
	 * 默认自动去免邮费
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/postage/close.json", method = RequestMethod.POST)
	public AjaxResult not_postage() {
		User user = UserUtils.getUser(); Order order = OrderUtils.getCache(); UserRank rank = this.userRankService.touch(user.getId());
		if (order != null && BigDecimalUtil.biggerThenZERO(order.getPostageDiscount())) {
			order.setPostageDiscount(null);
			OrderUtils.putCache(null, order);
			// 返回成功的数据
			PostageResult result = new PostageResult();
			if (rank != null) {
				if (Ints.nullToZero(rank.getShipping()) == -1) {
					result.setPostage("全年包邮");
				} else {
					result.setPostage(StringUtils.format("还剩%s次", rank.getRemainShipping()));
				}
			}
			// 订单的返回
			result.setDiscount(order.getTempDiscount());
			result.setAmount(order.getTempAmount());
			result.setDetail(OrderUtils.discountDetail(order));
			return AjaxResult.success(result);
		}
		return AjaxResult.error("不可用");
	}
	
	/**
	 * 2.第二步
	 *   -- 动态选择支付方式
	 * @return
	 */
	@Deprecated
	@ResponseBody
	@RequestMapping(value = "/paymentmethod/select.json", method = RequestMethod.POST)
	public AjaxResult paymentmethodSelect(Long id) {
		Order order = OrderUtils.getCache();
		if (order != null) {
			PaymentMethod paymentMethod = PaymentMethodUtils.getPaymentMethod(id);
		    order.initPaymentMethodObj(paymentMethod);
		    order.initShippingMethodObj(ShippingMethodUtils.getDefaultShippingMethod(paymentMethod));
			OrderUtils.putCache(null, order);
			return this.checkMethod()?AjaxResult.success(order):AjaxResult.error("支付方式和配送方式不支持");
		}
		return AjaxResult.error("至少选择一件商品");
	}
	
	/**
	 * 2.第二步
	 *   -- 动态选择配送方式
	 * @return
	 */
	@Deprecated
	@ResponseBody
	@RequestMapping(value = "/shippingmethod/select.json", method = RequestMethod.POST)
	public AjaxResult shippingMethodSelect(Long id) {
		Order order = OrderUtils.getCache();
		if (order != null) {
			order.initShippingMethodObj(ShippingMethodUtils.getShippingMethod(id));
			OrderUtils.putCache(null, order);
			return this.checkMethod()?AjaxResult.success():AjaxResult.error("支付方式和配送方式不支持");
		}
		return AjaxResult.error("至少选择一件商品");
	}
	
	/**
	 * 校验支付方式和配送方式是否符合
	 * @return
	 */
	private Boolean checkMethod() {
		Order order = OrderUtils.getCache();
		PaymentMethod paymentMethodObj = null;
		ShippingMethod shippingMethodObj = null;
		if (order != null && (paymentMethodObj = PaymentMethodUtils.getPaymentMethod(order.getPaymentMethod())) != null
		    && (shippingMethodObj = ShippingMethodUtils.getShippingMethod(order.getShippingMethod())) != null ) {
		    return shippingMethodObj.isSupport(paymentMethodObj);
		}
		return Boolean.TRUE;
 	}
	
	/**
	 * 2.第二步
	 *   -- 动态选择收货地址
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/receiver/select.json", method = RequestMethod.POST)
	public AjaxResult receiverSelect(Long id) {
		Order order = OrderUtils.getCache();
		if (order != null) {
			order.initReceiver(this.receiverService.get(id));
			order.setFreight(ShippingMethodUtils.calculateFreight(order));
			OrderUtils.putCache(null, order);
			
			// 设置返回
			OrderResult result = new OrderResult();
			result.setFreight(order.getFreight());
			result.setAmount(order.getTempAmount());
			result.setDetail(StringUtils.format("<h4>%s<small>%s</small></h4><p>%s</p>", 
					order.getConsignee(), order.getPhone(), order.getAddress()));
			return AjaxResult.success(result);
		}
		return AjaxResult.error("至少选择一件商品");
	}
	
	/**
	 * 2.第二步
	 *   -- 备注
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/remarks.json", method = RequestMethod.POST)
	public AjaxResult remarks(String remarks) {
		Order order = OrderUtils.getCache();
		if (order != null) {
			order.setRemarks(remarks);
			OrderUtils.putCache(null, order);
			return AjaxResult.success();
		}
		return AjaxResult.error("至少选择一件商品");
	}
	
	/**
	 * 2.第二步
	 *   -- 送货时间
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/consignee.json", method = RequestMethod.POST)
	public AjaxResult consignee(String consignee) {
		Order order = OrderUtils.getCache();
		if (order != null) {
			order.setConsigneeTime(consignee);
			OrderUtils.putCache(null, order);
			return AjaxResult.success();
		}
		return AjaxResult.error("至少选择一件商品");
	}
	
	/**
	 * 2.第二步
	 *   -- 开具发票
	 * @return
	 */
	@Deprecated
	@ResponseBody
	@RequestMapping(value = "/invoice.json", method = RequestMethod.POST)
	public AjaxResult invoice(String title) {
		Order order = OrderUtils.getCache();
		if (order != null) { 
			order.setInvoiceTitle(title);
			OrderUtils.putCache(null, order);
			order = OrderUtils.getCache();
			return AjaxResult.success();
		}
		return AjaxResult.error("至少选择一件商品");
	}
	
	/**
	 * 2.第二步
	 *   -- 提交校验 (验证商品库存，价格，状态等信息)
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/check.json", method = RequestMethod.POST)
	public AjaxResult check() {
		
		// 缓存订单
		Order order = OrderUtils.getCache();
		
		// 商品校验结果
		OrderCheckResult result = OrderCheckResult.orderInvalid();
		
		// 当前用户
		User user = UserUtils.getUser();
		
		// 校验
		if (order != null && order.getItems() != null && order.getPaymentMethod() != null && StringUtils.isNotBlank(order.getAddress())
				&& StringUtils.isNotBlank(order.getPhone()) && ((result = this.orderService.checkProduct(order, user)).isSuccess())) {
		   return AjaxResult.success();
		} else if(order != null && order.getItems() != null && order.getPaymentMethod() == null) {
		   return AjaxResult.error("请选择支付方式");
		} else if(order != null && order.getItems() != null && (StringUtils.isBlank(order.getAddress()) || StringUtils.isBlank(order.getPhone()))) {
		   return AjaxResult.error("请选择收货地址");
		} else if(order != null && order.getItems() != null && !result.isSuccess()){
		   return AjaxResult.error(result.getMsg(), result.getItem());
		}
		return AjaxResult.error("订单已失效");
	}
	
	/**
	 * 3. 第三步(下单)
	 *    只是提交订单(需要使用最新的价格)
	 *    获取店铺（一个公众号就是一个店铺）
	 * @return
	 */
	@RequestMapping(value = "book.html")
	public String book(Model model, HttpServletRequest request) {
		Order order = OrderUtils.getCache();
		
		// 删除缓存
		UserUtils.removeAttribute(ShopConstant.CART_QUANTITY);
		
		//下单
		if (order != null) { 
			User user = UserUtils.getUser();
			order.userOptions(user);
			
			// 根据域名获取店铺
			Store store = StoreUtils.getDefaultStore();
			App app = WechatUtils.get(store.getWxApp());
			if (app != null) {
				order.setShopId(app.getId());
				// 商品描述 -- 和想得有点不一样，但差别不大
				String orderDesc = StringUtils.abbr(StringUtils.format("%s-%s", app.getName(), order.getProductName()), 128);
				order.setOrderDesc(orderDesc);
			}
			try {
				// 下定单
				orderService.book(order);
				// 预支付订单
				PrepayOrderUtils.put(order);
			}catch(CouponErrorException e) {
				model.addAttribute("message", e.getMessage());
				return "/front/member/OrderError"; 
			}
			OrderUtils.removeCache();
			return WebUtils.redirectTo(Globals.getFrontPath(), "/member/shop/order/payment/", order.getId().toString(), ".html");
		}
		// 返回订单失效页面
		return WebUtils.redirectTo(Globals.getFrontPath(), "/member/shop/order/error.html");
	}
	
	/**
	 * 3. 第三步(下单)
	 *    只是提交订单(需要使用最新的价格)
	 *    获取店铺（一个公众号就是一个店铺）
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "book.json")
	public AjaxResult book_ajax(HttpServletRequest request) {
		Order order = OrderUtils.getCache();
		
		// 删除缓存
		UserUtils.removeAttribute(ShopConstant.CART_QUANTITY);
		
		//下单
		if (order != null) { 
			User user = UserUtils.getUser();
			order.userOptions(user);
			
			// 根据域名获取店铺
			Store store = StoreUtils.getDefaultStore();
			App app = WechatUtils.get(store.getWxApp());
			if (app != null) {
				order.setShopId(app.getId());
				// 商品描述 -- 和想得有点不一样，但差别不大
				String orderDesc = StringUtils.abbr(StringUtils.format("%s-%s", app.getName(), order.getProductName()), 128);
				order.setOrderDesc(orderDesc);
			}
			try {
				// 下定单
				orderService.book(order);
				// 预支付订单
				PrepayOrderUtils.put(order);
			}catch(CouponErrorException e) {
				return AjaxResult.error(e.getMessage());
			}
			OrderUtils.removeCache();
			return AjaxResult.success();
		}
		// 返回订单失效页面
		return AjaxResult.error("订单失效");
	}
	
	/**
	 * 订单错误
	 * @return
	 */
	@RequestMapping(value = "error.html")
	public String orderError() {
		return "/front/member/OrderError"; 
	}
	
	/**
	 * 4. 支付页面 -- 统一的支付页面
	 * @return
	 */
	@RequestMapping(value = "payment/{id}.html", method = RequestMethod.GET)
	public String payment(@PathVariable Long id, Model model) {
		Order order = PrepayOrderUtils.get(id);
		if (order == null) {
			order = this.orderService.get(id);
		}
		if (order != null && order.isPayable() && order.getPaymentMethodType() == Type.BEFORE_PAYED) {
			model.addAttribute("order", order);
			model.addAttribute("epays", EpayUtils.getEpays());
			return "/front/member/OrderPaymentOn";
		} else if (order != null && order.isPayable() && order.getPaymentMethodType() == Type.BEFORE_DELIVER) {
			model.addAttribute("order", order);
			return "/front/member/OrderPaymentOff";
		} else {
			return WebUtils.redirectTo(Globals.getFrontPath(), "/member/shop/order/view/", id.toString(), ".html");
		}
	}
	
	/**
	 * 支付初始化
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/payment/init.json", method = RequestMethod.POST)
	public PayResult payment_init(Long id, Long epayId, HttpServletRequest request) {
		
		// 当前用户
		User user = UserUtils.getUser();
		
		// 订单
		Order order = PrepayOrderUtils.get(id);
		if (order == null) {
			order = this.orderService.get(id);
		}
		if (order == null || !order.isPayable()) {
			return PayResult.fail("订单已支付或已过期");
		}
		
		// 支付插件(epayId == null 会返回默认的插件)
		Epay epay = EpayUtils.get(epayId);
		if (epay == null) {
			return PayResult.fail("系统未开放支付");
		}
		
		// 支付
		Payment payment = this.paymentService.initPayment(epay, order, user);
		if (payment == null) {
			return PayResult.fail("请返回重试！");
		}
		return PayResult.needPay(payment.getId());
	}
	
	// 订单的相关操作
	/**
	 * 列表
	 * @return
	 */
	@RequestMapping("/list.html")
	public String list(Model model) {
		return "/front/member/OrderList";
	}
	
	/**
	 * 待付款
	 * @return
	 */
	@RequestMapping("/list/unpay.html")
	public String list_unpay(Model model) {
		model.addAttribute("queryStatus", 2);
		return "/front/member/OrderUnpayList";
	}
	
	/**
	 * 待发货
	 * @return
	 */
	@RequestMapping("/list/unshipped.html")
	public String list_unshipped(Model model) {
		model.addAttribute("queryStatus", 3);
		return "/front/member/OrderUnshippedList";
	}
	
	/**
	 * 待收货
	 * @return
	 */
	@RequestMapping("/list/unreceipted.html")
	public String list_unreceipted(Model model) {
		model.addAttribute("queryStatus", 5);
		return "/front/member/OrderUnreceiptedList";
	}
	
	/**
	 * 列表数据
	 * @param page
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/list/data.json")
	public Page page(String queryStatus, Page page) {
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria();
		c.andEqualTo("CREATE_ID", UserUtils.getUser().getId());
		this.initQc(queryStatus, c);
		qc.setOrderByClause("CREATE_DATE DESC");
		PageParameters param = page.getParam();
		param.setPageSize(15);
		// 填充明细
		return this.orderService.queryUserOrdersForPage(qc, param);
	}
	
	/**
	 * 待办数据
	 */
	@ResponseBody
	@RequestMapping(value = "/count_order_state.json", method = RequestMethod.POST)
	public AjaxResult count_order_state() {
		User user = UserUtils.getUser();
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria();
		this.initQc("2", c);
		c.andEqualTo("CREATE_ID", user.getId());
		int unpay = this.orderService.countByCondition(qc);
		qc.clear();
		c = qc.getCriteria();
        this.initQc("3", c);
        c.andEqualTo("CREATE_ID", user.getId());
        int unshipped = this.orderService.countByCondition(qc);
        qc.clear();
		c = qc.getCriteria();
        this.initQc("5", c);
        c.andEqualTo("CREATE_ID", user.getId());
        int unreceipted = this.orderService.countByCondition(qc);
    	
        //可用优惠券数量
        int usableCount = couponCodeService.countUserUsableStat(user);
        
        // 返回相应数据
        Map<String, Object> param = Maps.newHashMap();
        param.put("unpay", unpay);
        param.put("unshipped", unshipped);
        param.put("unreceipted", unreceipted);
        param.put("usableCount", usableCount);
		return AjaxResult.success(param);
	}
	
	//查询条件
	private void initQc(String queryStatus, Criteria c) {
		// 待付款（单可以付款的）
		if ("2".equals(queryStatus)) {
			c.andIsNotNull("EXPIRE");
			c.andDateGreaterThanOrEqualTo("EXPIRE", new Date());
			c.andEqualTo("FLOW_STATE", "待付款");
		}
		
		// 待发货(是否需要包含申请退款) 4 是申请退款
		else if ("3".equals(queryStatus)) {
			c.andEqualTo("FLOW_STATE", "待发货");
		}
		
		// 待收货
		else if ("5".equals(queryStatus)) {
			c.andEqualTo("FLOW_STATE", "待收货");
		}
	}
	
	/**
	 * 明细
	 * @return
	 */
	@RequestMapping("view/{id}.html")
	public String view(@PathVariable Long id, Model model) { 
		Order order = this.orderService.getWithOrderItems(id);
		if (order == null) {
			return WebUtils.redirectTo(Globals.getFrontPath(), "/404.html");
		}
		// 物流信息
		if (order.getShippingStatus() == ShippingStatus.shipped) {
			List<Shipping> shippings = shippingService.queryShippingsByOrderId(order.getId());
			model.addAttribute("shipping", shippings!= null&& shippings.size()>0?shippings.get(0):new Shipping());
		}
		// 已赠送的分享
		if (order.isGiveRedenvelopeable()) {
			List<OrderPromotion> promotions = this.orderService.queryOrderGivedFissionPromotion(order.getId());
			model.addAttribute("promotions", promotions);
		}
		model.addAttribute("order", order);
		return "/front/member/OrderView";
	}
	
	/**
	 * 取消订单
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/cancel.json", method = RequestMethod.POST)
	public AjaxResult cancel(Long id) {
		Order order = this.orderService.get(id);
		if(order != null && order.isCancelable()) {
			this.orderService.cancel(order, UserUtils.getUser());
			return AjaxResult.success();
		}
		return AjaxResult.error("订单状态错误，请重新刷新页面!");
	}
	
	/**
	 * 确认收货(现在不需要)
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/receipt.json", method = RequestMethod.POST)
	public AjaxResult receipt(Long id) {
		Order order = new Order(); order.setId(id);
		return this.orderService.receipt(order, null)?AjaxResult.success():AjaxResult.error("订单状态错误，请重新刷新页面!");
	}
	
	/**
	 * 申请退货
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/apply_return.json", method = RequestMethod.POST)
	public AjaxResult applyReturn(Long id) {
		Order order = new Order(); order.setId(id);
		return this.orderService.applyReturns(order)?AjaxResult.success():AjaxResult.error("订单状态错误，请重新刷新页面!");
	}
	
	/**
	 * 申请退款
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/apply_refund.json", method = RequestMethod.POST)
	public AjaxResult applyRefund(Long id) {
		Order order = new Order(); order.setId(id);
		return this.orderService.applyRefund(order)?AjaxResult.success():AjaxResult.error("订单状态错误，请重新刷新页面!");
	}
	
	/**
	 * 是否有需要处理的红包
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/has_redenvelope.json", method = RequestMethod.POST)
	public AjaxResult hasRedenvelope(Long id) {
		int mzs = this.orderService.hasOrderGiveAbleMzsPromotion(id);
		if (mzs >= 1) {
			return AjaxResult.success();
		}
		return AjaxResult.error("没有促销");
	}
	
	/**
	 * 发货步骤
	 * @return
	 */
	@RequestMapping(value = "/shipping_step/{id}.html")
	public String shipping_step(@PathVariable Long id, Model model) {
		return "/front/member/OrderShippingStep"; 
	}
	
	/**
	 * 再次购买
	 * @return
	 */
	@RequestMapping(value = "/againbuy/{id}.html")
	public String againbuy(@PathVariable Long id) { 
		Order order = this.orderService.getWithOrderItems(id);
		if (order != null) {
			// 我的购物车
			Cart cart = this.cartService.getByUserId(UserUtils.getUser());
			
			// 将所有商品全部加入购物车(默认已商品的形式添加到购物车-暂时)
			List<OrderItem> items = order.getItems();
			for(OrderItem item: items) {
				cartService.add(cart, null, item.getProductId(), item.getQuantity());
			}
			
			// 删除计数
			UserUtils.removeAttribute(ShopConstant.CART_QUANTITY);
		}
		return WebUtils.redirectTo(Globals.getFrontPath(), "/shop/cart/list.html");
	}
	
	/**
	 * 再次购买 -- 最后一次下的订单
	 * @return
	 */
	@RequestMapping(value = "/againbuy_newest.html")
	public String againbuy_newest() { 
		User user = UserUtils.getUser();
		Long id = this.orderService.getNewestOrder(user.getId());
		if (id != null) {
			return WebUtils.redirectTo(Globals.getFrontPath(), "/member/shop/order/againbuy/", id.toString(), ".html");
		}
		return WebUtils.redirectTo(Globals.getFrontPath(), "/shop/cart/list.html");
	}
}