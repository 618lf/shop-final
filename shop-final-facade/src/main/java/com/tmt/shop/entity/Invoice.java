package com.tmt.shop.entity;

import java.io.Serializable;
import java.util.Date;

import com.tmt.core.entity.BaseEntity;
import com.tmt.core.utils.StringUtils;

/**
 * 发票申领 管理
 * 
 * @author 超级管理员
 * @date 2016-05-26
 */
public class Invoice extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long orderId;// 订单ID
	private String orderSns; // 订单编号
	private Integer goodsNum; // 商品数量
	private java.math.BigDecimal amount; // 发票金额
	private String company; // 公司名称
	private String taxpayerNumber; // 单位纳税人识别号
	private String consignee; // 收货人
	private String address; // 送货的地址
	private String phone; // 联系方式
	private Long applyId;// 申请人ID
	private String applyName;// 申请人名称
	private Date openDate;// 开具时间
	private Byte status = Invoice.NO; // 处理标记
	private Byte send = Invoice.NO; // 发送标记
	private String invoiceUrl;// 电子发票的地址

	public String getInvoiceUrl() {
		return invoiceUrl;
	}

	public void setInvoiceUrl(String invoiceUrl) {
		this.invoiceUrl = invoiceUrl;
	}

	public Long getApplyId() {
		return applyId;
	}

	public void setApplyId(Long applyId) {
		this.applyId = applyId;
	}

	public String getApplyName() {
		return applyName;
	}

	public void setApplyName(String applyName) {
		this.applyName = applyName;
	}

	public Date getOpenDate() {
		return openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	public Byte getSend() {
		return send;
	}

	public void setSend(Byte send) {
		this.send = send;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderSns() {
		return orderSns;
	}

	public void setOrderSns(String orderSns) {
		this.orderSns = orderSns;
	}

	public Integer getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(Integer goodsNum) {
		this.goodsNum = goodsNum;
	}

	public java.math.BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(java.math.BigDecimal amount) {
		this.amount = amount;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getTaxpayerNumber() {
		return taxpayerNumber;
	}

	public void setTaxpayerNumber(String taxpayerNumber) {
		this.taxpayerNumber = taxpayerNumber;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	/**
	 * 设置收货地址
	 * 
	 * @param receiver
	 */
	public void setReceiver(Receiver receiver) {
		if (receiver != null) {
			this.address = StringUtils.format("%s%s%s", receiver.getAreaName(), receiver.getAddress(),
					receiver.getHouseNumber());
			this.phone = receiver.getPhone();
			this.consignee = receiver.getConsignee();
		}
	}
}