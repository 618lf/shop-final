package com.tmt.shop.entity;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.tmt.bbs.entity.Topic;
import com.tmt.core.entity.BaseEntity;

/**
 * 商品评价 管理
 * 
 * @author 超级管理员
 * @date 2017-04-12
 */
public class ProductAppraise extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long orderId; // 订单ID
	private Long productId; // 商品ID
	private String productName; // 商品名称
	private String productImage; // 商品图片
	private Byte productGrade; // 商品满意度
	private Byte packageGrade; // 包装满意度
	private Byte deliveryGrade; // 物流满意度
	private String mood; // 心情颜色
	private String tags; // 标签
	private String createRank; // 用户等级图片
	private String createImage; // 用户图像
	private Byte isTop; // 是否置顶
	private Byte isShow; // 是否显示
	private String content; // 评论
	private String addContent; // 追评
	private String images; // 图片
	private Integer points; // 赠送的积分
	private ProductAppraiseContent pcontent; // 内容

	// 编辑时的状态
	private Byte state;

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}

	public String getAddContent() {
		return addContent;
	}

	public void setAddContent(String addContent) {
		this.addContent = addContent;
	}

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}

	public ProductAppraiseContent getPcontent() {
		return pcontent;
	}

	public void setPcontent(ProductAppraiseContent pcontent) {
		this.pcontent = pcontent;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public Byte getProductGrade() {
		return productGrade;
	}

	public void setProductGrade(Byte productGrade) {
		this.productGrade = productGrade;
	}

	public Byte getPackageGrade() {
		return packageGrade;
	}

	public void setPackageGrade(Byte packageGrade) {
		this.packageGrade = packageGrade;
	}

	public Byte getDeliveryGrade() {
		return deliveryGrade;
	}

	public void setDeliveryGrade(Byte deliveryGrade) {
		this.deliveryGrade = deliveryGrade;
	}

	public String getMood() {
		return mood;
	}

	public void setMood(String mood) {
		this.mood = mood;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getCreateRank() {
		return createRank;
	}

	public void setCreateRank(String createRank) {
		this.createRank = createRank;
	}

	public String getCreateImage() {
		return createImage;
	}

	public void setCreateImage(String createImage) {
		this.createImage = createImage;
	}

	public Byte getIsTop() {
		return isTop;
	}

	public void setIsTop(Byte isTop) {
		this.isTop = isTop;
	}

	public Byte getIsShow() {
		return isShow;
	}

	public void setIsShow(Byte isShow) {
		this.isShow = isShow;
	}

	/**
	 * 需要生成相关标签
	 * 
	 * @return
	 */
	@JSONField(serialize = false)
	public Topic getTopic() {
		Topic topic = new Topic();
		topic.setId(this.getId());
		topic.setCreateId(this.getCreateId());
		topic.setCreateName(this.getCreateName());
		topic.setCreateImage(this.getCreateImage());
		topic.setCreateDate(this.getCreateDate());
		topic.setIsGood(Topic.NO);
		topic.setIsTop(Topic.NO);
		topic.setIsShow(Topic.YES);
		topic.setSectionId(this.getProductId());
		topic.setSectionName(this.getProductName());
		topic.setTags(this.getTags());
		topic.setImages(this.getImages());
		return topic;
	}
}
