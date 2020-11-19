package com.tmt.shop.entity;

import java.io.Serializable;
import java.util.List;

import com.tmt.core.entity.BaseEntity;

/**
 * 购物车 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
public class Cart extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String cartKey; // 唯一ID
	private List<CartItem> items; // 列表
    private int checked;//是否全选
    
    public int getChecked() {
		return checked;
	}
	public void setChecked(int checked) {
		this.checked = checked;
	}
	public List<CartItem> getItems() {
		return items;
	}
	public void setItems(List<CartItem> items) {
		this.items = items;
	}
	public String getCartKey() {
		return cartKey;
	}
	public void setCartKey(String cartKey) {
		this.cartKey = cartKey;
	}
}