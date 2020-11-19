package com.tmt.shop.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.tmt.shop.entity.CartItem;

/**
 * 购物车排序
 * @author lifeng
 */
public class CartSort implements Comparator<CartItem> {

	private static CartSort INSTANCE = new CartSort();
	
	/**
	 * 购车车排序规则
	 */
	@Override
	public int compare(CartItem o1, CartItem o2) {
		if (o1.getPromotions() == null) {
			return 1;
		}
		if (o2.getPromotions() == null) {
			return -1;
		}
		return o1.getPromotions().compareTo(o2.getPromotions());
	}
	
	/**
	 * 排序
	 * @param items
	 */
	public static void sort(List<CartItem> items) {
		Collections.sort(items, INSTANCE);
	}
}