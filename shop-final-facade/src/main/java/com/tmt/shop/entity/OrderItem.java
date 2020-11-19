package com.tmt.shop.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.tmt.core.entity.IdEntity;
import com.tmt.core.utils.BigDecimalUtil;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;

/**
 * 订单明细 管理
 * 
 * @author 超级管理员
 * @date 2016-01-20
 */
public class OrderItem extends IdEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long orderId; // 订单
	private Long goodsId;// 产品 -- 临时
	private Long productId; // 产品
	private String productSn; // 产品
	private String productName; // 产品
	private String thumbnail; // 缩略图
	private String snapshot; // 商品快照地址
	private java.math.BigDecimal price; // 单价
	private Integer quantity; // 数量
	private Integer returnQuantity; // 退货数量
	private Integer shippedQuantity; // 发货数量
	private String weight; // 单重量
	private String unit; // 单位
	private Integer rewardPoint; // 单赠送积分
	private Long cartItemId; // 购物车ID

	// 临时字段
	private Integer productStore; // 实时库存

	// 临时订单才有这个
	private Long promotions; // 促销

	public Integer getProductStore() {
		return productStore;
	}

	public void setProductStore(Integer productStore) {
		this.productStore = productStore;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Long getPromotions() {
		return promotions;
	}

	public void setPromotions(Long promotions) {
		this.promotions = promotions;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public String getSnapshot() {
		return snapshot;
	}

	public void setSnapshot(String snapshot) {
		this.snapshot = snapshot;
	}

	public Integer getRewardPoint() {
		return rewardPoint;
	}

	public void setRewardPoint(Integer rewardPoint) {
		this.rewardPoint = rewardPoint;
	}

	public Long getCartItemId() {
		return cartItemId;
	}

	public void setCartItemId(Long cartItemId) {
		this.cartItemId = cartItemId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductSn() {
		return productSn;
	}

	public void setProductSn(String productSn) {
		this.productSn = productSn;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public java.math.BigDecimal getPrice() {
		return price;
	}

	public void setPrice(java.math.BigDecimal price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getReturnQuantity() {
		return returnQuantity;
	}

	public void setReturnQuantity(Integer returnQuantity) {
		this.returnQuantity = returnQuantity;
	}

	public Integer getShippedQuantity() {
		return shippedQuantity;
	}

	public void setShippedQuantity(Integer shippedQuantity) {
		this.shippedQuantity = shippedQuantity;
	}

	public String getWeight() {
		return StringUtils.isBlank(weight) ? "0" : weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public java.math.BigDecimal getTotal() {
		return BigDecimalUtil.mul(this.getPrice(), BigDecimal.valueOf(this.getQuantity()));
	}

	/**
	 * 根据产品生成订单项
	 * 
	 * @param product
	 * @return
	 */
	public static OrderItem build(Product product, Integer quantity) {
		OrderItem item = new OrderItem();
		item.setGoodsId(product.getGoodsId());
		item.setProductId(product.getId());
		item.setProductSn(product.getSn());
		item.setProductName(StringUtils.isNotBlank(product.getTip())
				? StringUtils.format("%s%s", product.getName(), product.getTip())
				: product.getName());
		item.setPrice(product.getPrice());
		item.setThumbnail(product.getImage());
		item.setSnapshot(product.getSnapshot());
		item.setWeight(product.getWeight());
		item.setRewardPoint(product.getRewardPoint());
		item.setQuantity(quantity == null ? 1 : quantity);
		item.setReturnQuantity(0);
		item.setShippedQuantity(0);
		return item;
	}

	/**
	 * 单条记录
	 * 
	 * @param product
	 * @return
	 */
	public static OrderItem build(CartItem cartItem) {
		OrderItem item = new OrderItem();
		if (cartItem.getType() == 0) {
			Product product = cartItem.getProduct();
			item.setGoodsId(product.getGoodsId());
			item.setProductId(product.getId());
			item.setProductSn(product.getSn());
			item.setProductName(StringUtils.isNotBlank(product.getTip())
					? StringUtils.format("%s%s", product.getName(), product.getTip())
					: product.getName());
			item.setPrice(product.getPrice());
			item.setWeight(product.getWeight());
			item.setUnit(product.getUnit());
			item.setRewardPoint(product.getRewardPoint());
			item.setThumbnail(product.getImage());
			item.setSnapshot(product.getSnapshot());
			item.setQuantity(cartItem.getQuantity());
			item.setCartItemId(cartItem.getId());
			item.setReturnQuantity(0);
			item.setShippedQuantity(0);
			item.setPromotions(cartItem.getPromotions());
		} else {
			Complex complex = cartItem.getComplex();
			item.setGoodsId(null);
			item.setProductId(complex.getId());
			item.setProductSn(null);
			item.setProductName(complex.getName());
			item.setPrice(complex.getPrice());
			item.setWeight(complex.getWeight());
			item.setQuantity(cartItem.getQuantity());
			item.setCartItemId(cartItem.getId());
			item.setReturnQuantity(0);
			item.setShippedQuantity(0);
			item.setPromotions(complex.getId());
			StringBuilder imgs = new StringBuilder();
			List<ComplexProduct> products = complex.getProducts();
			for (ComplexProduct cp : products) {
				imgs.append(
						StringUtils.format("<span class=\"image-wrap goods-image\"><img alt=\"\" src=\"%s\"></span>",
								cp.getProduct().getImage()));
			}
			item.setThumbnail(imgs.toString());
		}
		return item;
	}

	/**
	 * 多条记录
	 * 
	 * @param cartItem
	 * @return
	 */
	public static List<OrderItem> builds(OrderItem cartItem, Complex complex) {
		List<OrderItem> items = Lists.newArrayList();
		List<ComplexProduct> products = complex.getProducts();
		for (ComplexProduct cp : products) {
			OrderItem item = new OrderItem();
			Product product = cp.getProduct();
			item.setGoodsId(product.getGoodsId());
			item.setProductId(product.getId());
			item.setProductSn(product.getSn());
			item.setProductName(StringUtils.isNotBlank(product.getTip())
					? StringUtils.format("%s%s", product.getName(), product.getTip())
					: product.getName());
			item.setPrice(product.getPrice());
			item.setWeight(product.getWeight());
			item.setUnit(product.getUnit());
			item.setRewardPoint(product.getRewardPoint());
			item.setThumbnail(product.getImage());
			item.setSnapshot(product.getSnapshot());
			item.setQuantity(cartItem.getQuantity() * cp.getQuantity());
			item.setCartItemId(cartItem.getCartItemId());
			item.setReturnQuantity(0);
			item.setShippedQuantity(0);
			item.setPromotions(null);
			items.add(item);
		}
		return items;
	}
}