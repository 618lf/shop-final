package com.tmt.shop.entity;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.tmt.core.entity.BaseEntity;

/**
 * 商品管理 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
public class Goods extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long categoryId; // 分类
	private String categoryName; // 分类
	private Integer categorySort;//分类排序
	private String sn; // 唯一序列号
	private String name; // 名称
	private Long brand; // 品牌
	private String brandName;// 品牌名称
	private java.util.Date expire; // 产品有效期
	private String fullName; // 副标题
	private String image; // 图片
	private Byte isMarketable = Goods.YES; // 是否上架
	private Byte isList = Goods.YES; // 是否列出
	private Byte isTop = Goods.YES; // 是否置顶
	private Byte isDelivery= Goods.YES; // 是否需要物流
	private String keyword; // 搜索关键字
	private java.math.BigDecimal cost; // 成本价
	private java.math.BigDecimal marketPrice; // 市场价
	private java.math.BigDecimal price; // 销售价
	private String introduction; // 介绍
	private String unit; // 单位
	private String weight; // 重量
	private Integer sweight; // 权重
	private String seoDescription; // 页面描述
	private String seoKeywords; // 页面关键字
	private String seoTitle; // 页面标题
	private Long sales = 0L; // 销量
	private Float score = 0.0f; // 平均得分
	private Long scoreCount = 0L; // 评分次数
	private Long totalScore = 0L; // 总得分
	private String relatedProductIds; // 相关联的库存
	private List<GoodsImage> images; //商品图片
	private List<GoodsParameterGroup> parameterGroups;// 商品参数值
	private List<GoodsTag> tags;// 商品标签
	private List<GoodsAttribute> attributes;// 商品属性
	private List<GoodsSpecification> specifications; // 商品规格
	private List<Product> products; //对应的产品
	private GoodsEvaluate evaluate;// 商品评价
	private GoodsLimit limit; // 商品限购
	private GoodsDelivery delivery; // 商品配送
	//库存中的字段
	private Integer rewardPoint = 0; //赠送积分
	private Integer store = 0; //库存
	
	//规格描述
	private String specificationNames;
	private Boolean hasSpecification;
	
	//临时字段
	private Category category;

	public Boolean getHasSpecification() {
		return hasSpecification;
	}
	public void setHasSpecification(Boolean hasSpecification) {
		this.hasSpecification = hasSpecification;
	}
	public GoodsDelivery getDelivery() {
		return delivery;
	}
	public void setDelivery(GoodsDelivery delivery) {
		this.delivery = delivery;
	}
	public GoodsLimit getLimit() {
		return limit;
	}
	public void setLimit(GoodsLimit limit) {
		this.limit = limit;
	}
	public GoodsEvaluate getEvaluate() {
		return evaluate;
	}
	public void setEvaluate(GoodsEvaluate evaluate) {
		this.evaluate = evaluate;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public Integer getCategorySort() {
		return categorySort;
	}
	public void setCategorySort(Integer categorySort) {
		this.categorySort = categorySort;
	}
	public Integer getSweight() {
		return sweight;
	}
	public void setSweight(Integer sweight) {
		this.sweight = sweight;
	}
	public String getSpecificationNames() {
		return specificationNames;
	}
	public void setSpecificationNames(String specificationNames) {
		this.specificationNames = specificationNames;
	}
	public Integer getStore() {
		return store;
	}
	public void setStore(Integer store) {
		this.store = store;
	}
	public Integer getRewardPoint() {
		return rewardPoint;
	}
	public void setRewardPoint(Integer rewardPoint) {
		this.rewardPoint = rewardPoint;
	}
	public List<Product> getProducts() {
		return products;
	}
	public void setProducts(List<Product> products) {
		this.products = products;
	}
	public List<GoodsSpecification> getSpecifications() {
		return specifications;
	}
	public void setSpecifications(List<GoodsSpecification> specifications) {
		this.specifications = specifications;
		StringBuilder _specifications = new StringBuilder();
		if(specifications != null && specifications.size() != 0) {
			for(GoodsSpecification specification: specifications) {
				List<GoodsSpecificationOption> options = specification.getOptions();
				for(GoodsSpecificationOption option: options) {
					_specifications.append(option.getSpecificationsOptionName()).append(",");
				}
			}
			if(_specifications.length() != 0) {
				_specifications.deleteCharAt(_specifications.length()-1);
			}
		}
		this.setSpecificationNames(_specifications.toString());
	}
	public List<GoodsAttribute> getAttributes() {
		return attributes;
	}
	public void setAttributes(List<GoodsAttribute> attributes) {
		this.attributes = attributes;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public List<GoodsParameterGroup> getParameterGroups() {
		return parameterGroups;
	}
	public void setParameterGroups(List<GoodsParameterGroup> parameterGroups) {
		this.parameterGroups = parameterGroups;
	}
	public List<GoodsTag> getTags() {
		return tags;
	}
	public void setTags(List<GoodsTag> tags) {
		this.tags = tags;
	}
	public List<GoodsImage> getImages() {
		return images;
	}
	public void setImages(List<GoodsImage> images) {
		this.images = images;
	}
	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
    public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
    public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    public Long getBrand() {
		return brand;
	}
	public void setBrand(Long brand) {
		this.brand = brand;
	}
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
    public java.util.Date getExpire() {
		return expire;
	}
	public void setExpire(java.util.Date expire) {
		this.expire = expire;
	}
    public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
    public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
    public Byte getIsMarketable() {
		return isMarketable;
	}
	public void setIsMarketable(Byte isMarketable) {
		this.isMarketable = isMarketable;
	}
	public Byte getIsList() {
		return isList;
	}
	public void setIsList(Byte isList) {
		this.isList = isList;
	}
	public Byte getIsTop() {
		return isTop;
	}
	public void setIsTop(Byte isTop) {
		this.isTop = isTop;
	}
	public Byte getIsDelivery() {
		return isDelivery;
	}
	public void setIsDelivery(Byte isDelivery) {
		this.isDelivery = isDelivery;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
    public java.math.BigDecimal getCost() {
		return cost;
	}
	public void setCost(java.math.BigDecimal cost) {
		this.cost = cost;
	}
    public java.math.BigDecimal getMarketPrice() {
		return marketPrice;
	}
	public void setMarketPrice(java.math.BigDecimal marketPrice) {
		this.marketPrice = marketPrice;
	}
    public java.math.BigDecimal getPrice() {
		return price;
	}
	public void setPrice(java.math.BigDecimal price) {
		this.price = price;
	}
    public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
    public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
    public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
    public String getSeoDescription() {
		return seoDescription;
	}
	public void setSeoDescription(String seoDescription) {
		this.seoDescription = seoDescription;
	}
    public String getSeoKeywords() {
		return seoKeywords;
	}
	public void setSeoKeywords(String seoKeywords) {
		this.seoKeywords = seoKeywords;
	}
    public String getSeoTitle() {
		return seoTitle;
	}
	public void setSeoTitle(String seoTitle) {
		this.seoTitle = seoTitle;
	}
    public Long getSales() {
		return sales;
	}
	public void setSales(Long sales) {
		this.sales = sales;
	}
    public Float getScore() {
		return score;
	}
	public void setScore(Float score) {
		this.score = score;
	}
	public Long getScoreCount() {
		return scoreCount;
	}
	public void setScoreCount(Long scoreCount) {
		this.scoreCount = scoreCount;
	}
    public Long getTotalScore() {
		return totalScore;
	}
	public void setTotalScore(Long totalScore) {
		this.totalScore = totalScore;
	}
    public String getRelatedProductIds() {
		return relatedProductIds;
	}
	public void setRelatedProductIds(String relatedProductIds) {
		this.relatedProductIds = relatedProductIds;
	}
}