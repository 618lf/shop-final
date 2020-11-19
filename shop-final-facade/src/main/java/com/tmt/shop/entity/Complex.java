package com.tmt.shop.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.tmt.core.entity.BaseEntity;
import com.tmt.core.utils.BigDecimalUtil;

/**
 * 商品组合 管理
 * @author 超级管理员
 * @date 2016-11-28
 */
public class Complex extends BaseEntity<Long> implements Serializable, Salable{
	
	private static final long serialVersionUID = 1L;
	
	private String name; // 名称
	private Byte type; // 类型：1 优惠 2 人气
	private java.math.BigDecimal prefer; // 优惠金额
	private List<ComplexProduct> products;// 商品
    
    public List<ComplexProduct> getProducts() {
		return products;
	}
	public void setProducts(List<ComplexProduct> products) {
		this.products = products;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
    public java.math.BigDecimal getPrefer() {
		return prefer;
	}
	public void setPrefer(java.math.BigDecimal prefer) {
		this.prefer = prefer;
	}
	public java.math.BigDecimal getPrice() {
		java.math.BigDecimal price = BigDecimal.ZERO;
		if (products != null) {
			for(ComplexProduct product: products) {
				price = BigDecimalUtil.add(price, product.getPrice());
			}
		}
		return BigDecimalUtil.smallerThenZERO(price) ? BigDecimal.ZERO : price;
	}
	public java.math.BigDecimal getDisPrice() {
		java.math.BigDecimal price = this.getPrice();
		if (this.type == 1) {
			price = BigDecimalUtil.sub(price, this.prefer);
		}
		return BigDecimalUtil.smallerThenZERO(price) ? BigDecimal.ZERO : price;
	}
	public String getWeight() {
		java.math.BigDecimal weight = BigDecimal.ZERO;
		if (products != null) {
			for(ComplexProduct product: products) {
				weight = BigDecimalUtil.add(weight, product.getPrice());
			}
		}
		return (BigDecimalUtil.smallerThenZERO(weight) ? BigDecimal.ZERO : weight).toPlainString();
	}
	
	/**
	 * 默认是上架
	 */
	@Override
	public Byte getIsMarketable() {
		return BaseEntity.YES;
	}
	
	/**
	 * 默认是可售
	 */
	@Override
	public Byte getIsSalestate() {
		return BaseEntity.YES;
	}
}