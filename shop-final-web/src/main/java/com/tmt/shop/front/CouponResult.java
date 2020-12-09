package com.tmt.shop.front;

import java.io.Serializable;

/**
 * 优惠券返回
 * @author root
 */
public class CouponResult extends OrderResult implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private Integer val;
	private Boolean isValid = Boolean.FALSE; // 是否有效
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getVal() {
		return val;
	}
	public void setVal(Integer val) {
		this.val = val;
	}
	public Boolean getIsValid() {
		return isValid;
	}
	public void setIsValid(Boolean isValid) {
		this.isValid = isValid;
	}
}
