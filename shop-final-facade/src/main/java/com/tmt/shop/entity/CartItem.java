package com.tmt.shop.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.tmt.core.entity.IdEntity;
import com.tmt.core.utils.BigDecimalUtil;
import com.tmt.core.utils.time.DateUtils;

/**
 * 购物车明细 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
public class CartItem extends IdEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long cartId; // 购物车编号
	private Long productId; // 产品 - 组合
	private Integer quantity; // 数量
	private Product product; // 产品
	private Complex complex; // 组合
	private Byte checked;//选择
	private Date createDate;
	private Long promotions; // 促销
	private Byte type; // 类型 0 商品、1 组合
	private Promotion promotion; // 实际的促销对象
	private Boolean hasPromotion = Boolean.FALSE; // 有促销
	
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public Complex getComplex() {
		return complex;
	}
	public void setComplex(Complex complex) {
		this.complex = complex;
	}
	public Boolean getHasPromotion() {
		return hasPromotion;
	}
	public void setHasPromotion(Boolean hasPromotion) {
		this.hasPromotion = hasPromotion;
	}
	public Promotion getPromotion() {
		return promotion;
	}
	public void setPromotion(Promotion promotion) {
		this.promotion = promotion;
		if (this.promotion != null) {
			this.setPromotions(this.promotion.getId());
			this.setHasPromotion(Boolean.TRUE);
		} else {
			this.setPromotions(null);
			this.setHasPromotion(Boolean.FALSE);
		}
	}
    public Long getPromotions() {
		return promotions;
	}
	public void setPromotions(Long promotions) {
		this.promotions = promotions;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Byte getChecked() {
		return checked;
	}
	public void setChecked(Byte checked) {
		this.checked = checked;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Long getCartId() {
		return cartId;
	}
	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}
    public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
    public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public java.math.BigDecimal getPrice() {
		return BigDecimalUtil.mul(BigDecimal.valueOf(this.getQuantity()), this.getProduct().getPrice());
	}
	@Override
	public Long prePersist() {
		this.createDate = DateUtils.getTimeStampNow();
		return super.prePersist();
	}
	
	// 复制
	public CartItem copy() {
		CartItem item = new CartItem();
		item.setProductId(this.getProductId());
		item.setQuantity(this.getQuantity());
		item.setChecked(this.getChecked());
		item.setType(this.getType());
		return item;
	}
}