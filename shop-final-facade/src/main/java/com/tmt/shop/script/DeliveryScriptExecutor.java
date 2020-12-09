package com.tmt.shop.script;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.tmt.core.groovy.ScriptExecutor;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.SpringContextHolder;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.shop.entity.GoodsDelivery;
import com.tmt.shop.entity.Order;
import com.tmt.shop.entity.OrderItem;
import com.tmt.shop.service.GoodsServiceFacade;

/**
 * 发货表达式计算
 * @author root
 */
public class DeliveryScriptExecutor extends ScriptExecutor{

	@Override
	protected String getBaseScriptClass() {
		return DeliveryScript.class.getName();
	}
	
	// 商品服务
	private GoodsServiceFacade goodsService;
	private String noDelivery = "配送方案自动设置中";
	
	/**
	 * 初始化默认参数
	 */
	public DeliveryScriptExecutor() {
		goodsService = SpringContextHolder.getBean(GoodsServiceFacade.class);
	}
	
	/**
	 * 计算配送时间
	 * @return
	 */
	public String calculateDeliveryTimes(Order order) {
		
		// 找出一个最晚的配送时间
		List<OrderItem> items = order.getItems();
		
		// 没商品信息，不需要设置配送方案
		if (items == null || items.isEmpty()) {
			return noDelivery;
		}
		
		// 大部分是这种情况
		else if(items.size() == 1) {
			GoodsDelivery delivery = goodsService.getGoodsDelivery(items.get(0).getGoodsId());
			return delivery == null?noDelivery:this.getMaxDeliveryTimes(DateUtils.getTodayTime(), delivery);
		}
		
		// 购买了多件商品
		else {
			Map<Long, GoodsDelivery> deliverys = Maps.newHashMap();
			for(OrderItem item: items) {
				GoodsDelivery delivery = goodsService.getGoodsDelivery(item.getGoodsId());
				if (delivery != null) {
					deliverys.put(delivery.getDeliveryId(), delivery);
				}
			}
			
			String max = null; Date now = DateUtils.getTodayTime();
			Iterator<Long> keys = deliverys.keySet().iterator();
			while(keys.hasNext()) {
				Long key = keys.next();
				String temp = getMaxDeliveryTimes(now, deliverys.get(key));
				if (temp == null) {continue;}
				
				if (max == null || max.compareToIgnoreCase(temp) < 0) {
					max = temp;
				}
			}
			return max == null?noDelivery:max;
		}
	}
	
	// 获取最大的
	private String getMaxDeliveryTimes(Date orderDate, GoodsDelivery delivery) {
		Map<String, Object> context = Maps.newHashMap();
		context.put("orderDate", DateUtils.getTodayTime());
		context.put("currDay", DateUtils.getTodayDate());
		context.put("currDate", DateUtils.getTodayTime());
		Object o = this.execute(delivery.getDeliveryExpression(), context);
		
		// 一般不会出现这种情况
		if (o == null) {
			return null;
		} 
		else if(o instanceof Date){
			Date o_date = (Date)o;
			return DateUtils.getFormatDate(o_date, "yyyy-MM-dd HH:mm:ss");
		}
		
		// 其他情况 string 表示
		return o.toString();
	}
}
