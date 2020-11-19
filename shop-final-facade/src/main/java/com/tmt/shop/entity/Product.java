package com.tmt.shop.entity;

import java.io.Serializable;
import java.util.List;

import com.tmt.core.entity.BaseEntity;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.time.DateUtils;

/**
 * 商品库存 管理
 * 
 * @author 超级管理员
 * @date 2016-01-21
 */
public class Product extends BaseEntity<Long> implements Serializable, Salable {

	private static final long serialVersionUID = 1L;

	private Long goodsId; // 编号
	private String sn; // 唯一序列号
	private String name; // 名称
	private String tip; // 小标题根据规格设置来，如果没有则为空
	private String image; // 图片
	private Byte isMarketable; // 是否上架
	private java.math.BigDecimal cost; // 成本价
	private java.math.BigDecimal marketPrice; // 市场价
	private java.math.BigDecimal price; // 销售价
	private Integer rewardPoint = 0; // 赠送积分
	private String unit; // 单位
	private String weight; // 重量
	private Integer inStore = 0; // 入库库存
	private Integer outStore = 0; // 出库库存
	private Integer store = 0; // 现有库存
	private Integer freezeStore = 0; // 已冻结库存
	private Byte isDefault = Product.NO; // 是否默认
	private Byte isSalestate = Product.NO; // 是否启用
	private String specificationsId; // 是否默认
	private String specificationsValue; // 是否默认
	private List<GoodsSpecificationValue> specificationValues; // 规格值
	private String snapshot; // 商品快照地址

	// 对应的商品
	private Goods goods;
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public Byte getIsMarketable() {
		return isMarketable;
	}

	public void setIsMarketable(Byte isMarketable) {
		this.isMarketable = isMarketable;
	}

	public String getSnapshot() {
		return snapshot;
	}

	public void setSnapshot(String snapshot) {
		this.snapshot = snapshot;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getRewardPoint() {
		return rewardPoint;
	}

	public void setRewardPoint(Integer rewardPoint) {
		this.rewardPoint = rewardPoint;
	}

	public List<GoodsSpecificationValue> getSpecificationValues() {
		return specificationValues;
	}

	public void setSpecificationValues(List<GoodsSpecificationValue> specificationValues) {
		this.specificationValues = specificationValues;
	}

	public Long getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
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

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
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

	public Integer getInStore() {
		return inStore;
	}

	public void setInStore(Integer inStore) {
		this.inStore = inStore;
	}

	public Integer getOutStore() {
		return outStore;
	}

	public void setOutStore(Integer outStore) {
		this.outStore = outStore;
	}

	public Integer getStore() {
		return store;
	}

	public void setStore(Integer store) {
		this.store = store;
	}

	public Integer getFreezeStore() {
		return freezeStore;
	}

	public void setFreezeStore(Integer freezeStore) {
		this.freezeStore = freezeStore;
	}

	public Byte getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Byte isDefault) {
		this.isDefault = isDefault;
	}

	public Byte getIsSalestate() {
		return isSalestate;
	}

	public void setIsSalestate(Byte isSalestate) {
		this.isSalestate = isSalestate;
	}

	public String getSpecificationsId() {
		return specificationsId;
	}

	public void setSpecificationsId(String specificationsId) {
		this.specificationsId = specificationsId;
	}

	public String getSpecificationsValue() {
		return specificationsValue;
	}

	public void setSpecificationsValue(String specificationsValue) {
		this.specificationsValue = specificationsValue;
	}

	/**
	 * 是否没有可售的库存了
	 * 
	 * @return
	 */
	public String getIsOutOfStock() {
		if (this.store < 0 || this.store.compareTo(this.getFreezeStore()) <= 0) {
			return Boolean.TRUE.toString();
		}
		return Boolean.FALSE.toString();
	}

	// 初始化规格
	public void initSpecifications() {
		if (this.specificationValues != null && this.specificationValues.size() != 0) {
			StringBuilder ids = new StringBuilder();
			StringBuilder names = new StringBuilder();
			for (GoodsSpecificationValue value : specificationValues) {
				ids.append(value.getId()).append(",");
				names.append(value.getValue()).append(",");
			}
			if (ids.length() != 0) {
				ids.deleteCharAt(ids.length() - 1);
			}
			if (names.length() != 0) {
				names.deleteCharAt(names.length() - 1);
			}
			this.setSpecificationsId(ids.toString());
			this.setSpecificationsValue(names.toString());
			this.setTip(StringUtils.format("[%s]", names.toString()));
		}
	}

	/**
	 * 只修改基础信息
	 * 
	 * @param goods
	 */
	public void updateBase(Goods goods) {
		this.setUnit(goods.getUnit());
		this.setWeight(goods.getWeight());
		this.setName(goods.getName());
	}

	/**
	 * 修改所有的信息
	 * 
	 * @param goods
	 */
	public void updateMore(Goods goods) {
		this.setUnit(goods.getUnit());
		this.setWeight(goods.getWeight());
		this.setName(goods.getName());
		this.setCost(goods.getCost());
		this.setMarketPrice(goods.getMarketPrice());
		this.setPrice(goods.getPrice());
		this.setRewardPoint(goods.getRewardPoint());
	}

	/**
	 * 如果有默认值，则不需要新增主键
	 */
	@Override
	public Long prePersist() {
		if (!IdGen.isInvalidId(this.id)) {
			this.createDate = DateUtils.getTimeStampNow();
			this.updateDate = this.createDate;
			return this.id;
		}
		return super.prePersist();
	}

	/**
	 * 创建一个库存对象
	 * 
	 * @param goods
	 * @return
	 */
	public static Product newProduct(Goods goods) {
		Product product = new Product();
		product.setImage(goods.getImage());
		product.setUnit(goods.getUnit());
		product.setWeight(goods.getWeight());
		product.setSn(goods.getSn());
		product.setGoodsId(goods.getId());
		product.setName(goods.getName());
		product.setCost(goods.getCost());
		product.setMarketPrice(goods.getMarketPrice());
		product.setPrice(goods.getPrice());
		product.setIsDefault(Product.YES);
		product.setIsSalestate(Product.YES);
		product.setRewardPoint(goods.getRewardPoint());
		product.setStore(goods.getStore());
		product.setInStore(goods.getStore());
		return product;
	}
}