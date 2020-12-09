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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.core.config.Globals;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.JsonMapper;
import com.tmt.core.web.BaseController;
import com.tmt.core.web.security.session.SessionProvider;
import com.tmt.shop.entity.DeliveryCorp;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.entity.Product;
import com.tmt.shop.entity.Shipping;
import com.tmt.shop.entity.ShippingItem;
import com.tmt.shop.entity.ShippingMethod;
import com.tmt.shop.service.DeliveryCorpServiceFacade;
import com.tmt.shop.service.OrderItemServiceFacade;
import com.tmt.shop.service.OrderServiceFacade;
import com.tmt.shop.service.ProductServiceFacade;
import com.tmt.shop.service.ShippingMethodServiceFacade;
import com.tmt.shop.service.ShippingServiceFacade;
import com.tmt.system.entity.ExcelTemplate;
import com.tmt.system.service.ExcelTemplateServiceFacade;
import com.tmt.system.utils.ExcelImpUtil;
import com.tmt.system.utils.UserUtils;

/**
 * 发货管理 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
@Controller("shopShippingController")
@RequestMapping(value = "${adminPath}/shop/shipping")
public class ShippingController extends BaseController{
	
	@Autowired
	private OrderServiceFacade orderService;
	@Autowired
	private OrderItemServiceFacade orderItemService;
	@Autowired
	private ShippingServiceFacade shippingService;
	@Autowired
	private DeliveryCorpServiceFacade deliveryCorpService;
	@Autowired
	private ShippingMethodServiceFacade shippingMethodService;
	@Autowired
	private ExcelTemplateServiceFacade excelTemplateService;
	@Autowired
	private ProductServiceFacade productService;
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Shipping shipping, Model model){
		return "/shop/ShippingList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param shipping
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Shipping shipping, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(shipping, c);
		return shippingService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param shipping
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Shipping shipping, Model model) {
	    if(shipping != null && !IdGen.isInvalidId(shipping.getId())) {
			shipping = this.shippingService.getWithItems(shipping.getId());
		} else {
		   if( shipping == null) {
			   shipping = new Shipping();
		   }
		   shipping.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("shipping", shipping);
		return "/shop/ShippingForm";
	}
	
	/**
	 * 表单
	 * @param shipping
	 * @param model
	 */
	@RequestMapping("order")
	public String order(Shipping shipping, Model model) {
		if(shipping != null && !IdGen.isInvalidId(shipping.getId())) {
		   shipping = this.shippingService.get(shipping.getId());
		}
		Order order = orderService.getWithOrderItems(shipping.getOrderId());
		if (order != null) {
			List<OrderItem> items = order.getItems();
			for(OrderItem item: items) {
				Product product = this.productService.getStore(item.getProductId());
				item.setProductStore(product.getStore());
			}
			shipping.setOrderId(order.getId());
			shipping.setOrderSn(order.getSn());
			model.addAttribute("shippingMethod", shippingMethodService.getShippingMethod(order.getShippingMethod()));
			model.addAttribute("shippingMethods", shippingMethodService.queryShippingMethods());
			model.addAttribute("deliveryCorps", deliveryCorpService.queryDeliveryCorps());
			model.addAttribute("shipping", shipping);
			model.addAttribute("order", order);
		}
		return "/shop/ShippingMinForm";
	}
	
	/**
	 * 发货
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("save")
	public AjaxResult save(Shipping shipping, HttpServletRequest request) {
		if (!UserUtils.isPermitted("admin:order:shipping")) {
			return AjaxResult.error("没有发货的权限");
		}
		Order order = this.orderService.getWithOrderItems(shipping.getOrderId());
		ShippingMethod shippingMethod = shippingMethodService.get(shipping.getShippingMethodId());
		DeliveryCorp deliveryCorp = deliveryCorpService.get(shipping.getDeliveryCorpId());
		shipping.setShippingMethod(shippingMethod.getName());
		shipping.setDeliveryCorp(deliveryCorp.getName());
		shipping.setDeliveryCorpCode(deliveryCorp.getCode());
		shipping.setDeliveryCorpUrl(deliveryCorp.getUrl());
		List<ShippingItem> items = WebUtils.fetchItemsFromRequest(request, ShippingItem.class, "sitems.");
		List<ShippingItem> copys = Lists.newArrayList();
		for(ShippingItem item: items) {
			OrderItem _item = null; Product product = null;
			if (item != null && StringUtils.isNotBlank(item.getSn()) && item.getQuantity() != null && item.getQuantity()> 0 
			    && ((_item = order.getOrderItem(item.getItemId())) != null) && ((product = this.productService.lockStoreProduct(_item.getProductId())) != null)
			    && item.getQuantity() <= _item.getQuantity() - _item.getShippedQuantity() && product.getStore()>= item.getQuantity()) {
				item.setName(_item.getProductName());
				item.setSn(_item.getProductSn());
				item.setPrice(_item.getPrice());
				item.setWeight(_item.getWeight());
				item.setUnit(_item.getUnit());
				copys.add(item);
			}
		}
		if (copys.size() == 0) {
		    return AjaxResult.error("商品发货数量设置错误，请检查库存和发货数量");
		}
		shipping.setItems(copys);
		shipping.userOptions(UserUtils.getUser());
		this.shippingService.shipping(order, UserUtils.getUser(), shipping);
		return AjaxResult.success();
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save/simple")
	public String save(Shipping shipping, Model model, RedirectAttributes redirectAttributes) {
		if (UserUtils.isPermitted("admin:order:md-shippinged")) {
			this.shippingService.save(shipping);
			addMessage(redirectAttributes, "修改发货信息成功");
			redirectAttributes.addAttribute("id", shipping.getId());
			return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/shipping/form");
		}
		addMessage(redirectAttributes, "修改发货信息失败，没有相应权限");
		redirectAttributes.addAttribute("id", shipping.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/shop/shipping/form");
	}
	
	/**
	 * 删除
	 * @param idList
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete")
	public AjaxResult delete(Long[] idList , Model model, HttpServletResponse response) {
		List<Shipping> shippings = Lists.newArrayList();
		for(Long id: idList) {
			Shipping shipping = new Shipping();
			shipping.setId(id);
			shippings.add(shipping);
		}
		this.shippingService.delete(shippings);
		return AjaxResult.success();
	}
	
	/**
	 * 批量发货
	 * @return
	 */
	@RequestMapping(value = "batch_shipping", method = RequestMethod.GET)
	public String batch_shipping(Model model) {
		ExcelTemplate template = null;
		List<ExcelTemplate> templates = excelTemplateService.queryByTargetClass(Shipping.class.getName());
		if (templates.size() == 0) {
			template = new ExcelTemplate();
			template.setId(IdGen.INVALID_ID);
		} else {
			template = templates.get(0);
		}
		model.addAttribute("template", template);
		return "/shop/ShippingBatchForm";
	}
	
	/**
	 * 批量发货 -- 导入数据
	 * --- 不需要返回数据
	 * @return
	 */
	@RequestMapping(value = "batch_shipping_imp", method = RequestMethod.POST)
	public void batch_shipping_imp(Long templateId, HttpServletRequest request, HttpServletResponse response) {
		
		// 校验结果
		Map<String, Object> checkResult = Maps.newHashMap();
		MultipartFile[] files = WebUtils.uploadFile(request);
		AjaxResult result = ExcelImpUtil.fetchObjectFromTemplate(templateId, files[0]);
		try {
			
			// 先放入缓存
			if (result.getSuccess()) {
				List<Shipping> shippings = result.getObj();
				List<Map<String, Object>> errors = Lists.newArrayList();
				int size = shippings.size(); int totalProduct = 0;
				for(Shipping shipping: shippings) {
					Map<String, Object> error = this.checkShipping(shipping);
					if (error != null) {
						errors.add(error);
					} else {
						totalProduct = totalProduct + shipping.getProductQuantity();
					}
				}
				// 如果为空则置为导入失败
				if (!errors.isEmpty()) {
					checkResult.put("type", 2); // 订单校验错误
					checkResult.put("success", Boolean.FALSE);
					checkResult.put("obj", errors);
				} else {
					// 成功，只需要返回一个总数
					checkResult.put("success", Boolean.TRUE);
					checkResult.put("size", size);
					checkResult.put("totalProduct", totalProduct);
					
					// 放入session 中
					SessionProvider.setAttribute("batch_shipping", shippings);
				}
			} else {
				checkResult.put("type", 1); // 导入校验错误
				checkResult.put("msg", result.getMsg());
				checkResult.put("success", Boolean.FALSE);
				checkResult.put("obj", result.getObj());
			}
			
			// 返回结果信息
			String sResult =  JsonMapper.toJson(checkResult);
			IOUtils.write(sResult, response.getWriter());
		} catch (IOException e) {}
	}
	
	// 校验发货
	private Map<String, Object> checkShipping(Shipping shipping) {
		Long orderId = this.orderService.getIdBySn(shipping.getOrderSn());
		if (orderId == null) {
			Map<String, Object> error = Maps.newHashMap();
			error.put("order", shipping.getOrderSn());
			error.put("msg", StringUtils.format("订单【%s】,不存在", shipping.getOrderSn()));
			return error;
		}
		Order order = this.orderService.get(orderId);
		if (order == null || !order.isShippingable()) {
			Map<String, Object> error = Maps.newHashMap();
			error.put("order", shipping.getOrderSn());
			error.put("msg", StringUtils.format("订单【%s】,不能发货", shipping.getOrderSn()));
			return error;
		}
		order.setItems(orderItemService.queryItemsByOrderId(order.getId()));
		if (shipping.getProductQuantity() == null || order.getProductQuantity() != shipping.getProductQuantity()) {
			Map<String, Object> error = Maps.newHashMap();
			error.put("order", shipping.getOrderSn());
			error.put("msg", StringUtils.format("订单【%s】,填入的商品数量不一致", shipping.getOrderSn()));
			return error;
		}
		if (!order.getProductSn().equals(shipping.getProductSn())) {
			Map<String, Object> error = Maps.newHashMap();
			error.put("order", shipping.getOrderSn());
			error.put("msg", StringUtils.format("订单【%s】,发货的商品不一致", shipping.getOrderSn()));
			return error;
		}
		shipping.setOrderId(orderId);
		return null;
	}
	
	/**
	 * 提交批量发货
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "batch_shipping_save", method = RequestMethod.POST)
	public AjaxResult batch_shipping_save() {
		if (!UserUtils.isPermitted("admin:order:shipping")) {
			return AjaxResult.error("没有发货的权限");
		}
		// 从缓存中读取数据
		List<Shipping> shippings = SessionProvider.getAttribute("batch_shipping");
		SessionProvider.removeAttribute("batch_shipping");
		if (shippings != null && shippings.size() != 0) {
			// 发货
			for(Shipping shipping: shippings) {
				Order order = this.orderService.getWithOrderItems(shipping.getOrderId());
				ShippingMethod shippingMethod = shippingMethodService.getShippingMethod(order.getShippingMethod());
				DeliveryCorp deliveryCorp = deliveryCorpService.get(shippingMethod.getDeliveryCorpId());
				shipping.setShippingMethodId(shippingMethod.getId());
				shipping.setShippingMethod(shippingMethod.getName());
				shipping.setDeliveryCorpId(deliveryCorp.getId());
				shipping.setDeliveryCorp(deliveryCorp.getName());
				shipping.setDeliveryCorpCode(deliveryCorp.getCode());
				shipping.setDeliveryCorpUrl(deliveryCorp.getUrl());
				
				List<OrderItem> items = order.getItems();
				List<ShippingItem> _items = Lists.newArrayList();
				for(OrderItem item: items) {
					ShippingItem _item = new ShippingItem();
					_item.setName(item.getProductName());
					_item.setSn(item.getProductSn());
					_item.setQuantity(item.getQuantity());
					_item.setPrice(item.getPrice());
					_item.setWeight(item.getWeight());
					_item.setUnit(item.getUnit());
					_items.add(_item);
				}
				if (_items.size() == 0) {
				    return AjaxResult.error("商品发货数量设置错误，请检查库存和发货数量");
				}
				shipping.setItems(_items);
				shipping.userOptions(UserUtils.getUser());
				this.shippingService.shipping(order, UserUtils.getUser(), shipping);
			}
		}
		return AjaxResult.success();
	}
	
	//查询条件
	private void initQc(Shipping shipping, Criteria c) {
        if(StringUtils.isNotBlank(shipping.getSn())) {
           c.andEqualTo("SN", shipping.getSn());
        }
        if(StringUtils.isNotBlank(shipping.getOrderSn())) {
           c.andEqualTo("ORDER_SN", shipping.getOrderSn());
        }
        if(StringUtils.isNotBlank(shipping.getTrackingNo())) {
           c.andEqualTo("TRACKING_NO", shipping.getTrackingNo());
        }
	}
}