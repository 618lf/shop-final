package com.tmt.shop.entity;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import com.tmt.core.utils.Maps;

/**
 * 订单统计
 * 
 * @author lifeng
 *
 */
public class OrderStat implements Serializable, Comparable<OrderStat> {

	private static final long serialVersionUID = 1L;

	private int product = 0; // 商品数
	private Map<Long, Integer> products = Maps.newHashMap(); // 商品统计

	public int getProduct() {
		return product;
	}

	public void setProduct(int product) {
		this.product = product;
	}

	public Map<Long, Integer> getProducts() {
		return products;
	}

	public void setProducts(Map<Long, Integer> products) {
		this.products = products;
	}

	public void addProduct(Long productId, int quantity) {
		int _product = 0;
		if (products.containsKey(productId)) {
			_product = products.get(productId);
		}
		;

		// 明细
		products.put(productId, _product + quantity);

		// 总数
		product = product + quantity;
	}

	/**
	 * 比较大小
	 */
	@Override
	public int compareTo(OrderStat o) {

		// 总数是否相等
		if (o.getProduct() != this.getProduct()) {
			return -1;
		}
		if (this.getProducts().keySet().size() != o.getProducts().keySet().size()) {
			return -1;
		}

		// 只要有一个没找到
		Iterator<Long> ids = this.products.keySet().iterator();
		while (ids.hasNext()) {
			Long id = ids.next();
			Integer num1 = this.products.get(id);
			Integer num2 = o.getProducts().get(id);
			if (!o.getProducts().containsKey(id) || num1 == null || num2 == null || num1 != num2) {
				return -1;
			}
		}
		return 0;
	}
}
