package com.tmt.shop.web;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tmt.core.entity.ColumnMapper;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.utils.BigDecimalUtil;
import com.tmt.core.utils.ExcelExpUtils;
import com.tmt.core.web.BaseExportController;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.service.OrderServiceFacade;

/**
 * 订单管理 管理
 * @author 超级管理员
 * @date 2015-11-04
 */
@Controller("shopOrderExpController")
@RequestMapping(value = "${adminPath}/shop/order_exp")
public class OrderExpController extends BaseExportController<Order>{

	@Autowired
	private OrderServiceFacade orderService;
	
	@Override
	public Map<String, Object> doExport(Order order, HttpServletRequest request) {
		QueryCondition qc = new QueryCondition();
		Criteria c = qc.getCriteria();
		qc.setOrderByClause("CREATE_DATE DESC");
		String[] ids = request.getParameterValues("export.idList");
		if (null != ids && ids.length != 0){
			List<String> idLst = Lists.newArrayList();
			idLst = Arrays.asList(ids);
			c.andIn("ID", idLst);
		}
		List<Order> orders = this.orderService.queryByCondition(qc);
		List<Map<String, Object>> values = Lists.newArrayList();
		for(Order item: orders) {
			Order richItem = this.orderService.getWithOrderItems(item.getId());
			Map<String, Object> value = Maps.newHashMap();
			value.put("sn", richItem.getSn());
			value.put("amount", BigDecimalUtil.toString(richItem.getAmount(), 2));
			value.put("amountPaid", BigDecimalUtil.toString(richItem.getAmountPaid(), 2));
			value.put("createName", richItem.getCreateName());
			value.put("createDate", DateUtils.getFormatDate(richItem.getCreateDate(), "yyyy-MM-dd HH-mm-ss"));
			value.put("orderStatus", richItem.getFlowState());
			
			// 商品名称优化
			String productName = richItem.getOrderDesc();
			List<OrderItem> items = richItem.getItems();
			if (items != null && items.size() > 0) {
			   StringBuilder desc = new StringBuilder("\"");
			   for(OrderItem _item: items) {
				   desc.append(_item.getProductName()).append("*").append(_item.getQuantity()).append("\n");
			   }
			   desc.append("\"");
			   productName = desc.toString();
			}
			value.put("productName", productName);
			value.put("productSn", richItem.getProductSn());
			value.put("productNum", richItem.getProductQuantity());
			value.put("freight", BigDecimalUtil.toString(richItem.getFreight(), 2));
			value.put("consignee", richItem.getConsignee());
			value.put("phone", richItem.getPhone());
			value.put("address", richItem.getAddress());
			value.put("remarks", richItem.getRemarks());
			value.put("special", richItem.getSpecial());
			values.add(value);
		}
		List<ColumnMapper> columns = Lists.newArrayList();
		columns.add(ColumnMapper.buildExpMapper("订单编号","B", DataType.STRING, "sn"));
		columns.add(ColumnMapper.buildExpMapper("订单金额","C", DataType.STRING, "amount"));
		columns.add(ColumnMapper.buildExpMapper("实付金额","D", DataType.STRING, "amountPaid"));
		columns.add(ColumnMapper.buildExpMapper("下单时间","E", DataType.STRING, "createDate"));
		columns.add(ColumnMapper.buildExpMapper("订单状态","F", DataType.STRING, "orderStatus"));
		columns.add(ColumnMapper.buildExpMapper("下单人","G", DataType.STRING, "createName"));
		columns.add(ColumnMapper.buildExpMapper("收货人","H", DataType.STRING, "consignee"));
		columns.add(ColumnMapper.buildExpMapper("联系电话","I", DataType.STRING, "phone"));
		columns.add(ColumnMapper.buildExpMapper("收货地址","J", DataType.STRING, "address"));
		columns.add(ColumnMapper.buildExpMapper("物流公司","K", DataType.STRING, ""));
		columns.add(ColumnMapper.buildExpMapper("运单号","L", DataType.STRING, ""));
		columns.add(ColumnMapper.buildExpMapper("运费","M", DataType.STRING, "freight"));
		columns.add(ColumnMapper.buildExpMapper("商品数量","N", DataType.NUMBER, "productNum"));
		columns.add(ColumnMapper.buildExpMapper("商品名称","O", DataType.STRING, "productName"));
		columns.add(ColumnMapper.buildExpMapper("商品编码","P", DataType.STRING, "productSn"));
		columns.add(ColumnMapper.buildExpMapper("订单备注","Q", DataType.STRING, "remarks"));
		columns.add(ColumnMapper.buildExpMapper("处理备注","R", DataType.STRING, "special"));
		return ExcelExpUtils.buildExpParams("订单导出", "订单导出", columns, values, "orderTemplate.xls", 2);
	}

	@Override
	protected Class<Order> getTargetClass() {
		return Order.class;
	}
}
