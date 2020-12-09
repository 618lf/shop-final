package com.tmt.shop.web;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.entity.AjaxResult;
import com.tmt.core.security.annotation.Token;
import com.tmt.core.utils.JsonMapper;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.OrderSplits;
import com.tmt.shop.entity.OrderStat;
import com.tmt.shop.entity.Product;
import com.tmt.shop.service.OrderServiceFacade;
import com.tmt.shop.service.OrderSplitServiceFacade;
import com.tmt.shop.service.ProductServiceFacade;

/**
 * 订单拆分
 * @author lifeng
 */
@Controller("shopOrderSplitController")
@RequestMapping(value = "${adminPath}/shop/order/split")
public class OrderSplitController {
	
	@Autowired
	private OrderServiceFacade orderService;
	@Autowired
	private ProductServiceFacade productService;
	@Autowired
	private OrderSplitServiceFacade orderSplitService;
	
	/**
	 * 订单拆分初始的页面
	 * @return
	 */
	@Token(save = true)
	@RequestMapping("init")
	public String init(Order order, Model model) {
		order = this.orderService.getWithOrderItems(order.getId());
		List<Product> products = Lists.newArrayList();
		List<OrderItem> items = order.getItems();
		for(OrderItem item: items) {
			int quantity = item.getQuantity();
			for(int i = 1; i<= quantity; i++) {
				Product product = new Product();
				product.setId(item.getProductId());
				product.setImage(item.getThumbnail());
				product.setName(item.getProductName());
				products.add(product);
			}
		}
		model.addAttribute("order", order);
		model.addAttribute("products", products);
		return "/shop/OrderSplit"; 
	}
	
	/**
	 * 实现拆分
	 * @return
	 */
	@Token
	@ResponseBody
	@RequestMapping("do")
	public AjaxResult split(HttpServletRequest request) {
		String postData = request.getParameter("postData");
		OrderSplits split = JsonMapper.fromJson(postData, OrderSplits.class);
		if (split == null) {return AjaxResult.error("参数错误!");}
		Order order = orderService.getWithOrderItems(split.getOrderId());
		
		// 所有的明细
		List<OrderItem> all = Lists.newArrayList();
		
		// 数据验证
		List<Order> orders = split.getOrders();
		List<Order> copys = Lists.newArrayList();
		for(Order item: orders) {
			// 有数据的订单
			if (item.getItems() != null && item.getItems().size() > 0) {
				List<OrderItem> items = this.mergeOrder(item.getItems());
				item.setItems(items);
				
				// 保存所有的明细，用于比较
				all.addAll(items);
				
				// 复制数据
				this.copyOrder(order, item);
				copys.add(item);
			}
		}
		if (copys.size() <=1) {
			return AjaxResult.error("不用拆分");
		}
		
		// 可以拆分订单
		if (this.checkOrder(order.getItems(), all)
				&& (this.orderSplitService.split(order, copys))) {
			return AjaxResult.success();
		}
		
		// 不能拆分订单
		return AjaxResult.error("订单需要到待发货状态才能拆分");
	}
	
	// 校验
	private Boolean checkOrder(List<OrderItem> masters, List<OrderItem> splits) {
		OrderStat masterStat = new OrderStat();
		for(OrderItem item: masters) {
			masterStat.addProduct(item.getProductId(), item.getQuantity());
		}
		OrderStat splitStat = new OrderStat();
		for(OrderItem item: splits) {
			splitStat.addProduct(item.getProductId(), item.getQuantity());
		}
		return masterStat.compareTo(splitStat) == 0;
	}
	
	// 合并订单
	private List<OrderItem> mergeOrder(List<OrderItem> items) {
		Map<Long, OrderItem> merges = Maps.newHashMap();
		for(OrderItem item: items) {
			item.setQuantity(1);
			if (!merges.containsKey(item.getProductId())) {
				merges.put(item.getProductId(), item);
			} else {
				OrderItem _item = merges.get(item.getProductId());
				_item.setQuantity(_item.getQuantity() + 1);
			}
		}
		List<OrderItem> values = Lists.newArrayList();
		List<OrderItem> copys = Lists.newArrayList(merges.values());
		for(OrderItem item: copys) {
			Product product = productService.get(item.getProductId());
			if (product != null) {
				values.add(OrderItem.build(product, item.getQuantity()));
			}
		}
		return values;
	}
	
	// 复制订单
	private void copyOrder(Order master, Order order) {
		order.setShopId(master.getShopId());
		order.setFlowState(master.getFlowState());
		order.setType(master.getType());
		order.setCreateId(master.getCreateId());
		order.setCreateName(master.getCreateName());
		order.setCreateDate(master.getCreateDate());
		order.setExpire(null);
		order.setOrderStatus(master.getOrderStatus());
		order.setPaymentMethod(master.getPaymentMethod());
		order.setPaymentMethodName(master.getPaymentMethodName());
		order.setPaymentMethodType(master.getPaymentMethodType());
		order.setPaymentMethodMethod(master.getPaymentMethodMethod());
		order.setPaymentStatus(master.getPaymentStatus());
		order.setShippingMethod(master.getShippingMethod());
		order.setShippingMethodName(master.getShippingMethodName());
		order.setShippingStatus(master.getShippingStatus());
		order.setConsigneeTime(master.getConsigneeTime());
		order.setAreaId(master.getAreaId());
		order.setArea(master.getArea());
		order.setAddress(master.getAddress());
		order.setPhone(master.getPhone());
		order.setConsignee(master.getConsignee());
		order.setOrderDesc(order.getProductName());
		
		// 初始化数据
		Order.defaultOrder(order);
		
		// 将订单金额设置为0
		order.setAmount(BigDecimal.ZERO);
	}
}
