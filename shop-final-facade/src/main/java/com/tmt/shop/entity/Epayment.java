package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.system.entity.User;

/**
 * 企业支付 管理
 * @author 超级管理员
 * @date 2015-12-21
 */
public class Epayment extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long epayId; // 支出账户
	private String paySn; // 唯一序列号
	private String openid; // 如果是微信，则为微信的openid
	private Long userId; // 用户
	private String userName; // 用户
	private String userNo; // 用户
	private java.math.BigDecimal amount; // 订单金额
	private Byte payResult; // 支付结果
	private String payResultMsg; // 支付结果描述
	private String paymentNo; // 业务订单号
	private String paymentTime; // 支付成功时间
	
	//批量支付
	private String payees;
	
    public String getPayees() {
		return payees;
	}
	public void setPayees(String payees) {
		this.payees = payees;
	}
	public Long getEpayId() {
		return epayId;
	}
	public void setEpayId(Long epayId) {
		this.epayId = epayId;
	}
	public String getPaySn() {
		return paySn;
	}
	public void setPaySn(String paySn) {
		this.paySn = paySn;
	}
    public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
    public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
    public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
    public java.math.BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(java.math.BigDecimal amount) {
		this.amount = amount;
	}
    public Byte getPayResult() {
		return payResult;
	}
	public void setPayResult(Byte payResult) {
		this.payResult = payResult;
	}
	public String getPayResultMsg() {
		return payResultMsg;
	}
	public void setPayResultMsg(String payResultMsg) {
		this.payResultMsg = payResultMsg;
	}
    public String getPaymentNo() {
		return paymentNo;
	}
	public void setPaymentNo(String paymentNo) {
		this.paymentNo = paymentNo;
	}
    public String getPaymentTime() {
		return paymentTime;
	}
	public void setPaymentTime(String paymentTime) {
		this.paymentTime = paymentTime;
	}
	
	/**
	 * 提现支付
	 * @param sn
	 * @return
	 */
    public static Epayment cashingEpayment(String sn, String remarks, java.math.BigDecimal amount, User user) {
    	Epayment epayment = new Epayment();
    	epayment.setPaySn(sn);
    	epayment.setUserId(user.getId());
    	epayment.setUserNo(user.getNo());
    	epayment.setUserName(user.getName());
    	epayment.setAmount(amount);
    	epayment.setRemarks(remarks);
    	epayment.setCreateDate(DateUtils.getTimeStampNow());
    	return epayment;
    }
}
