package com.tmt.wechat.bean.card;

import com.tmt.wechat.bean.base.BaseResult;

/**
 * 微信卡券
 * @author lifeng
 */
public class CardResult extends BaseResult {

	private static final long serialVersionUID = 1L;

	private String openId;
	private Card card;
	private String userCardStatus;
	private Boolean canConsume;
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Card getCard() {
		return card;
	}
	public void setCard(Card card) {
		this.card = card;
	}
	public String getUserCardStatus() {
		return userCardStatus;
	}
	public void setUserCardStatus(String userCardStatus) {
		this.userCardStatus = userCardStatus;
	}
	public Boolean getCanConsume() {
		return canConsume;
	}
	public void setCanConsume(Boolean canConsume) {
		this.canConsume = canConsume;
	}
}