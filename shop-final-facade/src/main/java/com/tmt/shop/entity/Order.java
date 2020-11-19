package com.tmt.shop.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;
import com.tmt.core.entity.BaseEntity;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.BigDecimalUtil;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.time.DateUtils;
import com.tmt.shop.entity.PaymentMethod.Method;
import com.tmt.shop.entity.PaymentMethod.Type;
import com.tmt.shop.enums.OrderStatus;
import com.tmt.shop.enums.PayStatus;
import com.tmt.shop.enums.ShippingStatus;

/**
 * 订单 管理 订单所属的公众号
 * 
 * @author 超级管理员
 * @date 2016-01-20
 */
public class Order extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sn; // 唯一序列号
	private String orderDesc; // 订单描述（下单的时候生成此字段）
	private Byte type; // 订单类型 1、普通订单 2、加星订单 3、维权订单
	private java.math.BigDecimal amount; // 订单金额
	private java.math.BigDecimal amountPaid; // 已支付金额
	private java.math.BigDecimal discount; // 折扣
	private java.math.BigDecimal promotionDiscount; // 优惠券折扣 - 临时字段
	private java.math.BigDecimal couponDiscount; // 优惠券折扣 - 临时字段
	private java.math.BigDecimal rankDiscount; // 会员折扣
	private java.math.BigDecimal postageDiscount; // 包邮优惠和促销中的包邮不一样（会员卡中的包邮或包邮卡）
	private java.math.BigDecimal freight; // 运费
	private java.math.BigDecimal fee; // 手续费
	private java.math.BigDecimal tax; // 税费
	private java.util.Date expire; // 订单有效期
	private String invoiceTitle; // 发票题头
	private String invoiceUrl;// 电子发票地址
	private OrderStatus orderStatus; // 订单状态
	private Long paymentMethod; // 支付方式
	private String paymentMethodName; // 支付方式名称
	private Type paymentMethodType;// 支付方式
	private Method paymentMethodMethod;// 支付方式
	private PayStatus paymentStatus; // 支付状态
	private Long shippingMethod; // 送货方式
	private String shippingMethodName; // 送货方式
	private ShippingStatus shippingStatus; // 送货状态
	private String consigneeTime; // 送货时间
	private CouponMini coupon; // 使用的优惠券
	private Rank rank; // 用户等级
	private boolean couponAble; // 是否能使用优惠券
	private boolean postageAble; // 是否能使用包邮（如果有包邮的促销则优先使用促销，包邮不能使用）
	private String flowState; // 流程状态

	// 需要调整 --- 开始（地址：区域-详细地址）
	private String areaId; // 区域
	private String area;// 区域
	private String address; // 送货的地址 深圳市南山区海岸时代公寓（深南大道12069号）
	private String phone; // 联系方式
	private String consignee; // 收货人
	// 需要调整 --- 结束

	private Payment payment; // 第三方支付回调对象
	private List<OrderItem> items;
	private List<Payment> payments; // 支付记录
	private List<Refunds> refunds; // 退款记录
	private String transactionUrl; // 业务相关的地址
	private String special;// 特殊处理
	private boolean isAllocatedStock;// 是否分配库存（可以设置在哪个阶段分配库存）暂时在发货的时候分配库存
	private String shopId; // 店铺

	// 查询
	private String queryPaymethod;// 查询支付方式
	private Date queryStartDate; // 查询的开始时间
	private Date queryEndDate; // 查询的结束时间

	// 中间计算结果
	private Map<Long, PromotionMini> promotions; // 合并显示

	public Map<Long, PromotionMini> getPromotions() {
		return promotions;
	}

	public void setPromotions(Map<Long, PromotionMini> promotions) {
		this.promotions = promotions;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getFlowState() {
		return flowState;
	}

	public void setFlowState(String flowState) {
		this.flowState = flowState;
	}

	public boolean isPostageAble() {
		return postageAble;
	}

	public void setPostageAble(boolean postageAble) {
		this.postageAble = postageAble;
	}

	public boolean isCouponAble() {
		return couponAble;
	}

	public void setCouponAble(boolean couponAble) {
		this.couponAble = couponAble;
	}

	public Rank getRank() {
		return rank;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
	}

	public java.math.BigDecimal getRankDiscount() {
		return rankDiscount;
	}

	public void setRankDiscount(java.math.BigDecimal rankDiscount) {
		this.rankDiscount = rankDiscount;
	}

	public java.math.BigDecimal getPostageDiscount() {
		return postageDiscount;
	}

	public void setPostageDiscount(java.math.BigDecimal postageDiscount) {
		this.postageDiscount = postageDiscount;
	}

	public CouponMini getCoupon() {
		return coupon;
	}

	public void setCoupon(CouponMini coupon) {
		this.coupon = coupon;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}

	public String getOrderDesc() {
		return orderDesc;
	}

	public Byte getType() {
		return type;
	}

	public void setType(Byte type) {
		this.type = type;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public Date getQueryStartDate() {
		return queryStartDate;
	}

	public void setQueryStartDate(Date queryStartDate) {
		this.queryStartDate = queryStartDate;
	}

	public Date getQueryEndDate() {
		return queryEndDate;
	}

	public void setQueryEndDate(Date queryEndDate) {
		this.queryEndDate = queryEndDate;
	}

	public String getInvoiceUrl() {
		return invoiceUrl;
	}

	public void setInvoiceUrl(String invoiceUrl) {
		this.invoiceUrl = invoiceUrl;
	}

	public String getQueryPaymethod() {
		return queryPaymethod;
	}

	public void setQueryPaymethod(String queryPaymethod) {
		this.queryPaymethod = queryPaymethod;
	}

	public String getConsigneeTime() {
		return consigneeTime;
	}

	public void setConsigneeTime(String consigneeTime) {
		this.consigneeTime = consigneeTime;
	}

	public Type getPaymentMethodType() {
		return paymentMethodType;
	}

	public void setPaymentMethodType(Type paymentMethodType) {
		this.paymentMethodType = paymentMethodType;
	}

	public boolean isAllocatedStock() {
		return isAllocatedStock;
	}

	public void setAllocatedStock(boolean isAllocatedStock) {
		this.isAllocatedStock = isAllocatedStock;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

	public String getTransactionUrl() {
		return transactionUrl;
	}

	public void setTransactionUrl(String transactionUrl) {
		this.transactionUrl = transactionUrl;
	}

	public java.math.BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(java.math.BigDecimal amount) {
		this.amount = amount;
	}

	public java.math.BigDecimal getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(java.math.BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

	public java.math.BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(java.math.BigDecimal discount) {
		this.discount = discount;
	}

	@JSONField(serialize = false)
	public java.math.BigDecimal getCouponDiscount() {
		return couponDiscount;
	}

	public void setCouponDiscount(java.math.BigDecimal couponDiscount) {
		this.couponDiscount = couponDiscount;
	}

	@JSONField(serialize = false)
	public java.math.BigDecimal getPromotionDiscount() {
		return promotionDiscount;
	}

	public void setPromotionDiscount(java.math.BigDecimal promotionDiscount) {
		this.promotionDiscount = promotionDiscount;
	}

	public java.math.BigDecimal getFreight() {
		return freight;
	}

	public void setFreight(java.math.BigDecimal freight) {
		this.freight = freight;
	}

	public java.math.BigDecimal getFee() {
		return fee;
	}

	public void setFee(java.math.BigDecimal fee) {
		this.fee = fee;
	}

	public java.math.BigDecimal getTax() {
		return tax;
	}

	public void setTax(java.math.BigDecimal tax) {
		this.tax = tax;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public java.util.Date getExpire() {
		return expire;
	}

	/**
	 * 剩余的分钟数（防止和用户的时间不同步导致显示问题）
	 * 
	 * @return
	 */
	public Integer getExpireSeconds() {
		return (this.expire != null) ? (int) ((this.expire.getTime() - DateUtils.getTimeStampNow().getTime()) / 1000)
				: -1;
	}

	public void setExpire(java.util.Date expire) {
		this.expire = expire;
	}

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public Long getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(Long paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public Method getPaymentMethodMethod() {
		return paymentMethodMethod;
	}

	public void setPaymentMethodMethod(Method paymentMethodMethod) {
		this.paymentMethodMethod = paymentMethodMethod;
	}

	public String getPaymentMethodName() {
		return paymentMethodName;
	}

	public void setPaymentMethodName(String paymentMethodName) {
		this.paymentMethodName = paymentMethodName;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public PayStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PayStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public Long getShippingMethod() {
		return shippingMethod;
	}

	public void setShippingMethod(Long shippingMethod) {
		this.shippingMethod = shippingMethod;
	}

	public String getShippingMethodName() {
		return shippingMethodName;
	}

	public void setShippingMethodName(String shippingMethodName) {
		this.shippingMethodName = shippingMethodName;
	}

	public ShippingStatus getShippingStatus() {
		return shippingStatus;
	}

	public void setShippingStatus(ShippingStatus shippingStatus) {
		this.shippingStatus = shippingStatus;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * address 实际的地址
	 * 
	 * @return
	 */
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

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public List<Refunds> getRefunds() {
		return refunds;
	}

	public void setRefunds(List<Refunds> refunds) {
		this.refunds = refunds;
	}

	public String getSpecial() {
		return special;
	}

	public void setSpecial(String special) {
		this.special = special;
	}

	/**
	 * 得到订单的介绍
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public String getOrderImage() {
		List<OrderItem> items = this.getItems();
		if (items != null && items.size() != 0) {
			return items.get(0).getThumbnail();
		}
		return null;
	}

	/**
	 * 是否过期
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isOrderExpire() {
		return (this.getExpire() != null && DateUtils.after(new Date(), this.getExpire()));
	}

	/**
	 * 商品的描述
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public String getProductName() {
		List<OrderItem> items = this.getItems();
		if (items != null && items.size() > 0) {
			StringBuilder desc = new StringBuilder();
			for (OrderItem item : items) {
				desc.append(item.getProductName()).append(";");
			}
			// 删除最后的字符“;”
			desc.deleteCharAt(desc.length() - 1);
			return desc.toString();
		}
		return this.orderDesc;
	}

	/**
	 * 得到商品数量
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public int getProductQuantity() {
		int quantity = 0;
		if (this.getItems() != null) {
			for (OrderItem item : this.getItems()) {
				quantity += item.getQuantity();
			}
		}
		return quantity;
	}

	/**
	 * 得到商品重量
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public Double getProductWeight() {
		Double weight = 0.0;
		if (this.getItems() != null) {
			for (OrderItem item : this.getItems()) {
				weight = BigDecimalUtil.add(weight,
						BigDecimalUtil.mul(item.getQuantity(), Double.valueOf(item.getWeight())));
			}
		}
		return weight;
	}

	/**
	 * 得到商品重量
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public String getProductSn() {
		StringBuilder desc = new StringBuilder();
		if (this.getItems() != null) {
			for (OrderItem item : this.getItems()) {
				desc.append(item.getProductSn()).append(";");
			}
		}
		return desc.toString();
	}

	/**
	 * 得到赠送的积分
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public int getRewardPoint() {
		int point = 0;
		if (this.getItems() != null) {
			for (OrderItem item : this.getItems()) {
				point += item.getQuantity() * item.getRewardPoint();
			}
		}
		return point;
	}

	/**
	 * 得到发货的数量
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public int getShippedQuantity() {
		int quantity = 0;
		if (this.getItems() != null) {
			for (OrderItem item : this.getItems()) {
				quantity += item.getShippedQuantity();
			}
		}
		return quantity;
	}

	/**
	 * 得到退后的数量
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public int getReturnQuantity() {
		int quantity = 0;
		if (this.getItems() != null) {
			for (OrderItem item : this.getItems()) {
				quantity += item.getReturnQuantity();
			}
		}
		return quantity;
	}

	/**
	 * 添加订单项
	 * 
	 * @param item
	 * @return
	 */
	@JSONField(serialize = false)
	public List<OrderItem> addItem(OrderItem item) {
		List<OrderItem> items = this.getItems();
		if (items == null) {
			items = Lists.newArrayList();
			this.setItems(items);
		}
		items.add(item);
		return items;
	}

	/**
	 * 添加多订单项
	 * 
	 * @param item
	 * @return
	 */
	@JSONField(serialize = false)
	public List<OrderItem> addItems(List<OrderItem> item) {
		List<OrderItem> items = this.getItems();
		if (items == null) {
			items = Lists.newArrayList();
			this.setItems(items);
		}
		items.addAll(item);
		return items;
	}

	/**
	 * 单纯的商品价格
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public BigDecimal getPrice() {
		List<OrderItem> items = this.getItems();
		if (items != null) {
			BigDecimal _price = BigDecimal.ZERO;
			for (OrderItem item : items) {
				_price = BigDecimalUtil.add(_price, item.getTotal());
			}
			return _price;
		}
		return BigDecimal.ZERO;
	}

	/**
	 * 是否需要支付
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public BigDecimal getAnountPayAble() {
		BigDecimal pay = BigDecimalUtil.sub(this.getAmount(), this.getAmountPaid());
		return BigDecimalUtil.biggerThen(pay, BigDecimal.ZERO) ? pay : BigDecimal.ZERO;
	}

	/**
	 * 现阶段 默认设置税率为0
	 * 
	 * @return
	 */
	public BigDecimal calculateTax() {
		return BigDecimal.ZERO;
	}

	/**
	 * 返回订单订单状态 --- 用 flow_status 代替
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public String initStatus() {
		if (this.orderStatus == null) {
			return null;
		}
		// 确认之后的操作
		if (this.orderStatus == OrderStatus.confirmed) {

			// 先发货
			if (this.getPaymentMethodType() != null && this.getPaymentMethodType() == Type.BEFORE_DELIVER) {
				if (this.shippingStatus == ShippingStatus.unshipped) {
					return "待发货";
				} else if (this.paymentStatus == PayStatus.paid && this.shippingStatus == ShippingStatus.shipped) {
					return "已完成";
				} else if ((this.shippingStatus == ShippingStatus.shipped
						|| this.shippingStatus == ShippingStatus.partialshipment)
						&& (this.paymentStatus == PayStatus.unpaid || this.paymentStatus == PayStatus.paid
								|| this.paymentStatus == PayStatus.partialpaid)) {
					return "待收货";
				} else if (this.shippingStatus == ShippingStatus.return_request) {
					return "申请退货";
				} else if (this.shippingStatus == ShippingStatus.partialreturns) {
					return "待退货";
				} else if ((this.paymentStatus == PayStatus.paid || this.paymentStatus == PayStatus.partialpaid
						|| this.paymentStatus == PayStatus.partialrefunds)
						&& this.shippingStatus == ShippingStatus.returned) {
					return "待退款";
				} else if (this.paymentStatus == PayStatus.unpaid && this.shippingStatus == ShippingStatus.returned) {
					return "已完成";
				}
				return this.paymentStatus.getName();
			}

			// 先付款
			else if (this.getPaymentMethodType() != null && this.getPaymentMethodType() == Type.BEFORE_PAYED) {

				// 先付款的才有过期判断
				if (this.isOrderExpire()) {
					return "已过期";
				}

				if (this.paymentStatus == PayStatus.unpaid || this.paymentStatus == PayStatus.partialpaid) {
					return "待付款";
				} else if (this.paymentStatus == PayStatus.paid && (this.shippingStatus == ShippingStatus.unshipped
						|| this.shippingStatus == ShippingStatus.partialshipment)) {
					return "待发货";
				} else if (this.paymentStatus == PayStatus.paid && this.shippingStatus == ShippingStatus.shipped) {
					return "待收货";
				} else if (this.paymentStatus == PayStatus.paid && this.shippingStatus == ShippingStatus.receipted) {
					return "已完成";
				} else if (this.paymentStatus == PayStatus.paid
						&& this.shippingStatus == ShippingStatus.return_request) {
					return "申请退货";
				} else if (this.paymentStatus == PayStatus.paid
						&& this.shippingStatus == ShippingStatus.partialreturns) {
					return "待退货";
				} else if ((this.paymentStatus == PayStatus.paid || this.paymentStatus == PayStatus.partialrefunds)
						&& this.shippingStatus == ShippingStatus.returned) {
					return "待退款";
				}
				return this.paymentStatus.getName();
			}
		}
		return this.getOrderStatus().getName();
	}

	/**
	 * 返回订单订单状态
	 * 
	 * @return
	 */
	public String getPayStatus() {
		return this.paymentStatus != null ? this.getPaymentStatus().getName() : null;
	}

	/**
	 * 返回订单订单状态
	 * 
	 * @return
	 */
	public String getShipStatus() {
		return this.shippingStatus != null ? this.getShippingStatus().getName() : null;
	}

	/**
	 * 订单是否可以审核(货到付款)
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isConfirmable() {
		if (this.getOrderStatus() == OrderStatus.unconfirmed) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 订单是否可以改价
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isMdamountable() {
		if (!this.isOrderExpire() && this.getOrderStatus() == OrderStatus.confirmed
				&& this.getPaymentStatus() == PayStatus.unpaid
				&& this.getShippingStatus() == ShippingStatus.unshipped) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 订单是否可以改发货信息
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isMdshippingable() {
		if (this.getShippingStatus() == ShippingStatus.unshipped) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 订单是否可以支付(JS 需要)
	 * 
	 * @return
	 */
	public boolean isPayable() {
		if (this.isOrderExpire()) {
			return Boolean.FALSE;
		} else if (this.getPaymentMethodType() == Type.BEFORE_DELIVER
				&& (this.getShippingStatus() == ShippingStatus.receipted
						|| this.getShippingStatus() == ShippingStatus.shipped
						|| this.getShippingStatus() == ShippingStatus.partialshipment
						|| this.getShippingStatus() == ShippingStatus.return_request)
				&& this.orderStatus == OrderStatus.confirmed
				&& (this.paymentStatus == PayStatus.unpaid || this.paymentStatus == PayStatus.partialpaid)) {
			return Boolean.TRUE;
		} else if (this.getPaymentMethodType() == Type.BEFORE_PAYED && this.orderStatus == OrderStatus.confirmed
				&& (this.paymentStatus == PayStatus.unpaid || this.paymentStatus == PayStatus.partialpaid)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 是否可以申请退款(前端显示为取消订单) 后端显示为申请退款
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isApplyRefund() {
		// 确认状态下 - 未发货 - 已支付
		if (this.getOrderStatus() == OrderStatus.confirmed && this.getShippingStatus() == ShippingStatus.unshipped
				&& this.paymentStatus == PayStatus.paid) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 订单是否可以退款 -- 确认状态下(退货之后可以退款)
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isRefundable() {
		if (this.getOrderStatus() == OrderStatus.confirmed
				&& (this.shippingStatus == ShippingStatus.returned || this.shippingStatus == ShippingStatus.unshipped)
				&& (this.getPaymentStatus() == PayStatus.paid || this.getPaymentStatus() == PayStatus.partialpaid
						|| this.getPaymentStatus() == PayStatus.refunds_request
						|| this.getPaymentStatus() == PayStatus.partialrefunds)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 订单是否可以退款处理 -- 确认状态下(退货之后可以退款)
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isRefundProcessable() {
		if (this.getOrderStatus() == OrderStatus.confirmed && this.getPaymentStatus() == PayStatus.refunds_process) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 订单是否可以发货 1.货到付款，已确认 2.款到发货，已支付
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isShippingable() {
		if (this.getPaymentMethodType() == Type.BEFORE_DELIVER && this.getOrderStatus() == OrderStatus.confirmed
				&& (this.shippingStatus == ShippingStatus.unshipped
						|| this.shippingStatus == ShippingStatus.partialshipment)) {
			return Boolean.TRUE;
		} else if (this.getPaymentMethodType() == Type.BEFORE_PAYED
				&& (this.getPaymentStatus() == PayStatus.paid || this.getPaymentStatus() == PayStatus.refunds_request)
				&& (this.shippingStatus == ShippingStatus.unshipped
						|| this.shippingStatus == ShippingStatus.partialshipment)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 订单是否可以取消发货 -- 标记为已发货则不能取消了
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isUnShippingable() {
		if (this.getPaymentMethodType() == Type.BEFORE_DELIVER && this.getOrderStatus() == OrderStatus.confirmed
				&& (this.shippingStatus == ShippingStatus.unshipped
						|| this.shippingStatus == ShippingStatus.partialshipment)) {
			return Boolean.TRUE;
		} else if (this.getPaymentMethodType() == Type.BEFORE_PAYED
				&& (this.getPaymentStatus() == PayStatus.paid || this.getPaymentStatus() == PayStatus.refunds_request)
				&& (this.shippingStatus == ShippingStatus.unshipped)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 确认收货 -- 货到付款不支持签收，直接支付就好了
	 * 
	 * @return
	 */
	public boolean isReceiptable() {
		if (this.getPaymentMethodType() == Type.BEFORE_PAYED && this.getOrderStatus() == OrderStatus.confirmed
				&& this.getShippingStatus() == ShippingStatus.shipped) {
			return Boolean.TRUE;
		} else if (this.getPaymentMethodType() == Type.BEFORE_DELIVER) {
			return Boolean.FALSE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 是否可以申请退货
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isApplyReturn() {
		// 确认状态下
		if (this.getPaymentMethodType() == Type.BEFORE_PAYED && this.getOrderStatus() == OrderStatus.confirmed
				&& (this.getShippingStatus() == ShippingStatus.shipped
						|| this.getShippingStatus() == ShippingStatus.partialshipment
						|| this.getShippingStatus() == ShippingStatus.partialreturns)) {
			return Boolean.TRUE;
		} else if (this.getPaymentMethodType() == Type.BEFORE_DELIVER && this.getOrderStatus() == OrderStatus.confirmed
				&& this.getShippingStatus() == ShippingStatus.shipped && this.getPaymentStatus() == PayStatus.paid) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 同意退货 （需要同意退货权限）
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isReturnable() {
		if (this.getOrderStatus() == OrderStatus.confirmed && (this.getShippingStatus() == ShippingStatus.shipped
				|| this.getShippingStatus() == ShippingStatus.partialshipment
				|| this.getShippingStatus() == ShippingStatus.return_request
				|| this.getShippingStatus() == ShippingStatus.partialreturns
				|| this.getShippingStatus() == ShippingStatus.receipted || this.isApplyReturn())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 订单是否可以完成
	 * 
	 * 先付款: 已收款 - 已收货 的才能点击完成 已退款 - 并且已退款（或未支付）的
	 * 
	 * 先发货: 已发货 - 已收款 已退款
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isCompleteable() {
		if (!this.isOrderExpire()) {
			if (this.getPaymentMethodType() == Type.BEFORE_PAYED && this.getOrderStatus() == OrderStatus.confirmed
					&& ((this.paymentStatus == PayStatus.paid && (this.shippingStatus == ShippingStatus.receipted
							|| this.shippingStatus == ShippingStatus.shipped
							|| this.shippingStatus == ShippingStatus.return_request))
							|| ((this.shippingStatus == ShippingStatus.returned
									|| this.shippingStatus == ShippingStatus.unshipped)
									&& (this.paymentStatus == PayStatus.refunded
											|| this.paymentStatus == PayStatus.unpaid)))) {
				return Boolean.TRUE;
			} else if (this.getPaymentMethodType() == Type.BEFORE_DELIVER
					&& this.getOrderStatus() == OrderStatus.confirmed
					&& ((this.paymentStatus == PayStatus.paid && (this.shippingStatus == ShippingStatus.receipted
							|| this.shippingStatus == ShippingStatus.shipped
							|| this.shippingStatus == ShippingStatus.return_request))
							|| ((this.shippingStatus == ShippingStatus.returned)
									&& (this.paymentStatus == PayStatus.refunded
											|| this.paymentStatus == PayStatus.unpaid)))) {
				return Boolean.TRUE;
			}
		}

		return Boolean.FALSE;
	}

	/**
	 * 订单是否可以取消
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isCancelable() {
		if (this.getOrderStatus() == OrderStatus.unconfirmed || (this.getOrderStatus() == OrderStatus.confirmed && (this
				.isOrderExpire()
				|| (this.paymentStatus == PayStatus.unpaid && this.shippingStatus == ShippingStatus.unshipped)))) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 是否可以发放红包
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public boolean isGiveRedenvelopeable() {
		if ((this.getOrderStatus() == OrderStatus.confirmed && this.getPaymentStatus() == PayStatus.paid
				&& (this.shippingStatus != ShippingStatus.return_request
						|| this.shippingStatus != ShippingStatus.returned
						|| this.shippingStatus != ShippingStatus.partialreturns))
				|| this.getOrderStatus() == OrderStatus.completed) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 根据商品编号获取商品信息
	 * 
	 * @param sn
	 * @return
	 */
	@JSONField(serialize = false)
	public OrderItem getOrderItem(Long itemId) {
		List<OrderItem> items = this.getItems();
		if (items != null && items.size() != 0) {
			for (OrderItem item : items) {
				if (item.getId().compareTo(itemId) == 0) {
					return item;
				}
			}
		}
		return null;
	}

	// 加几个壳字段 -- 避免中途计算中切换时需要回退上次的选择
	@JSONField(serialize = false)
	public BigDecimal getTempAmount() {
		BigDecimal amount = this.getAmount();
		amount = BigDecimalUtil.add(amount, this.getFreight()); // 运费
		amount = BigDecimalUtil.sub(amount, this.getPostageDiscount()); // 包邮
		amount = BigDecimalUtil.sub(amount, this.getRankDiscount()); // 会员折扣
		amount = BigDecimalUtil.sub(amount, this.getPromotionDiscount());
		amount = BigDecimalUtil.sub(amount, this.getCouponDiscount());
		return BigDecimalUtil.smallerThenZERO(amount) ? BigDecimal.ZERO : amount;
	}

	@JSONField(serialize = false)
	public BigDecimal getTempDiscount() {
		return BigDecimalUtil.add(this.getPostageDiscount(), this.getRankDiscount(), this.getPromotionDiscount(),
				this.getCouponDiscount());
	}

	/**
	 * 设置收货地址
	 * 
	 * @param receiver
	 */
	public void initReceiver(Receiver receiver) {
		if (receiver != null) {
			this.areaId = receiver.getAreaId();
			this.area = receiver.getArea(); // 区域/街道
			this.address = receiver.getFullAddress();
			this.phone = receiver.getPhone();
			this.consignee = receiver.getConsignee();
		} else {
			this.areaId = null;
			this.area = null;
			this.address = null;
			this.phone = null;
			this.consignee = null;
		}
	}

	/**
	 * 初始化支付方式
	 * 
	 * @param paymentMethod
	 */
	public void initPaymentMethodObj(PaymentMethod paymentMethod) {
		if (paymentMethod != null) {
			this.setPaymentMethod(paymentMethod.getId());
			this.setPaymentMethodName(paymentMethod.getName());
			this.setPaymentMethodType(paymentMethod.getType());
			this.setPaymentMethodMethod(paymentMethod.getMethod());
		} else {
			this.setPaymentMethod(null);
			this.setPaymentMethodName(null);
			this.setPaymentMethodType(null);
			this.setPaymentMethodMethod(null);
		}
	}

	/**
	 * 初始化送货方式
	 * 
	 * @param shippingMethod
	 */
	public void initShippingMethodObj(ShippingMethod shippingMethod) {
		if (shippingMethod != null) {
			this.setShippingMethod(shippingMethod.getId());
			this.setShippingMethodName(shippingMethod.getName());
		} else {
			this.setShippingMethod(null);
			this.setShippingMethodName(null);
		}
	}

	/**
	 * confirm 之后不能调用这个方法 否则会清空折扣等信息
	 * 
	 * @param order
	 * @return
	 */
	public static Order defaultOrder(Order order) {
		if (order == null) {
			order = new Order();
		}
		order.setId((Long) IdGen.key());
		order.setAmountPaid(BigDecimal.ZERO);
		order.setDiscount(BigDecimal.ZERO);
		order.setPromotionDiscount(BigDecimal.ZERO);
		order.setCouponDiscount(BigDecimal.ZERO);
		order.setRankDiscount(BigDecimal.ZERO);
		order.setPostageDiscount(BigDecimal.ZERO);
		order.setCoupon(null);
		order.setCouponAble(Boolean.TRUE);
		order.setPostageAble(Boolean.TRUE);
		order.setFreight(BigDecimal.ZERO);
		order.setFee(BigDecimal.ZERO);
		order.setTax(order.calculateTax());
		order.setPromotions(null);
		return calcula(order);
	}

	private static Order calcula(Order order) {
		BigDecimal _price = order.getPrice();
		_price = BigDecimalUtil.sub(_price, order.getDiscount());
		_price = BigDecimalUtil.add(_price, order.getFreight());
		_price = BigDecimalUtil.add(_price, order.getFee());
		_price = BigDecimalUtil.add(_price, order.getTax());
		order.setAmount(_price);
		return order;
	}
}