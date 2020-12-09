package com.tmt.shop.front;

import java.io.Serializable;

/**
 * 包邮结果
 * @author lifeng
 */
public class PostageResult extends OrderResult implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String postage; // 包邮的次数

	public String getPostage() {
		return postage;
	}
	public void setPostage(String postage) {
		this.postage = postage;
	}
}
