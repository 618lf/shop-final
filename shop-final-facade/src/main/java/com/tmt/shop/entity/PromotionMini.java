package com.tmt.shop.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.tmt.shop.utils.PromotionTypes;

/**
 * 存储计算过程中的值
 * @author root
 */
public class PromotionMini implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private Byte type;
	private String name;
	private String describe; // 描述
	private BigDecimal reduce;
	private Boolean promotionValid = Boolean.FALSE; // 优惠券是否有效
	private String validReason; // 无效的原因
	
	public void setPromotion(Promotion promotion) {
		this.id = promotion.getId();
		this.type = promotion.getType();
		this.name = promotion.getName();
		this.describe = promotion.getRemarks();
	}
	public void setPromotion(Complex complex) {
		this.id = complex.getId();
		this.type = Promotion.TZ;
		this.name = complex.getName();
		this.describe = complex.getRemarks();
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public Boolean getPromotionValid() {
		return promotionValid;
	}
	public void setPromotionValid(Boolean promotionValid) {
		this.promotionValid = promotionValid;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getReduce() {
		return reduce;
	}
	public void setReduce(BigDecimal reduce) {
		this.reduce = reduce;
	}
	public String getTypeName() {
		return PromotionTypes.getTypeName(type);
	}
	public String getValidReason() {
		return validReason;
	}
	public void setValidReason(String validReason) {
		this.validReason = validReason;
	}
}