package com.tmt.shop.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.tmt.core.entity.AjaxResult;
import com.tmt.core.security.annotation.Token;
import com.tmt.core.utils.JsonMapper;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderImp;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.PaymentMethod;
import com.tmt.shop.entity.Product;
import com.tmt.shop.entity.Receiver;
import com.tmt.shop.script.DeliveryScriptExecutor;
import com.tmt.shop.service.OrderServiceFacade;
import com.tmt.shop.service.ProductServiceFacade;
import com.tmt.shop.service.ReceiverServiceFacade;
import com.tmt.shop.utils.PaymentMethodUtils;
import com.tmt.shop.utils.ShippingMethodUtils;
import com.tmt.system.entity.ExcelTemplate;
import com.tmt.system.entity.User;
import com.tmt.system.utils.ExcelImpUtil;
import com.tmt.system.utils.UserUtils;
import com.tmt.wechat.entity.App;
import com.tmt.wechat.utils.WechatUtils;

/**
 * 导入
 * 
 * @author lifeng
 */
@Controller("shopOrderImpController")
@RequestMapping(value = "${adminPath}/shop/order/batch_create")
public class OrderImpController {

	@Autowired
	private OrderServiceFacade orderService;
	@Autowired
	private ReceiverServiceFacade receiverService;
	@Autowired
	private ProductServiceFacade productService;
	private DeliveryScriptExecutor scriptExecutor = new DeliveryScriptExecutor();

	/**
	 * 导入初始化页面
	 * 
	 * @return
	 */
	@Token(save = true)
	@RequestMapping("init")
	public String batch_imp(Model model) {
		List<ExcelTemplate> templates = ExcelImpUtil.queryByTargetClass(OrderImp.class);
		model.addAttribute("templates", templates);
		return "/shop/OrderBatchCreate";
	}

	/**
	 * 导入
	 */
	@Token()
	@RequestMapping("doImport")
	public void importFile(Long templateId, HttpServletRequest request,
			HttpServletResponse response) {
		MultipartFile[] files = WebUtils.uploadFile(request);
		AjaxResult result = this.doImport(templateId, request, files[0]);
		try {
			if (result != null) {
				String sResult = JsonMapper.toJson(result);
				IOUtils.write(sResult, response.getWriter());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 导入
	 */
	protected AjaxResult doImport(Long templateId, HttpServletRequest request,
			MultipartFile file) {
		// 读取数据
		AjaxResult result = ExcelImpUtil.fetchObjectFromTemplate(templateId, file);
		if (result.getSuccess()) {
			List<OrderImp> imports = result.getObj();
			List<Order> orders = this.mergeOrder(imports);
			for (Order order : orders) {
				this.save(order);
			}
		}
		return result;
	}

	/**
	 * 合并
	 * 
	 * @param orders
	 * @return
	 */
	private List<Order> mergeOrder(List<OrderImp> imports) {
		// 订单
		Map<String, Order> orders = Maps.newHashMap();
		for (OrderImp imp : imports) {
			String key = imp.toString();
			Order order = orders.get(key);
			if (order == null) {
				order = new Order();
				// 设置用户信息
				User user = UserUtils.getUserByNo(imp.getCreateNo());
				if (user == null) {
					continue;
				}
				order.userOptions(user);

				// 使用默认的地址
				if (StringUtils.isBlank(imp.getConsignee())) {
					// 用户默认的收货方式
					Receiver receiver = receiverService
							.queryUserDefaultReceiver(user);
					if (receiver != null) {
						order.setConsignee(receiver.getConsignee());
						order.setArea(receiver.getArea());
						order.setAddress(receiver.getFullAddress());
						order.setPhone(receiver.getPhone());
					}
				}
				// 使用填写的地址
				else {
					order.setConsignee(imp.getConsignee());
					order.setArea(imp.getArea());
					order.setAddress(imp.getAddress());
					order.setPhone(imp.getPhone());
				}
				orders.put(key, order);
			}

			// 添加商品信息
			List<OrderItem> items = order.getItems();
			if (items == null) {
				items = Lists.newArrayList();
				order.setItems(items);
			}
			Product product = productService.getBySn(imp.getProductSn());
			if (product != null) {
				items.add(OrderItem.build(product, imp.getQuantity()));
			}
		}
		
		// 返回订单集合
		List<Order> _orders =  Lists.newArrayList(orders.values());
		for(Order order: _orders) {
			order.setItems(this.mergeOrderItem(order.getItems()));
		}
		return _orders;
	}

	// 合并商品信息
	private List<OrderItem> mergeOrderItem(List<OrderItem> items) {
		Map<Long, OrderItem> _items = Maps.newHashMap();
		for(OrderItem item : items) {
			OrderItem _item =  _items.get(item.getProductId());
			if (_item != null) {
				_item.setQuantity(_item.getQuantity() + item.getQuantity());
			} else {
				_items.put(item.getProductId(), item);
			}
		}
		
		// 返回合并之后的集合
		return Lists.newArrayList(_items.values());
	}

	/**
	 * 保存 -- 同一个人的
	 * 
	 * @param order
	 */
	private void save(Order order) {

		// 支付方式 - 和配送方式
		PaymentMethod paymentMethod = PaymentMethodUtils.getDefaultPaymentMethod();
	    order.initPaymentMethodObj(paymentMethod);
	    order.initShippingMethodObj(ShippingMethodUtils.getDefaultShippingMethod(paymentMethod));

		// 配送时间
		String shippingAlert = scriptExecutor.calculateDeliveryTimes(order);
		order.setConsigneeTime(shippingAlert);

		// 构建订单
		Order.defaultOrder(order);
		
		// 计算运费
		order.setFreight(ShippingMethodUtils.calculateFreight(order));
				
		String domain = WebUtils.getRequest().getServerName();
		App app = WechatUtils.getDomainApp(domain);
		if (app != null) {
			order.setShopId(app.getId());
			String orderDesc = StringUtils.abbr(
					StringUtils.format("%s-%s", app.getName(),
							order.getProductName()), 128);
			order.setOrderDesc(orderDesc);
		}

		// 下订单
		this.orderService.book(order);
	}
}
