package com.tmt.cms.entity;

import java.io.Serializable;
import java.util.List;

import com.tmt.core.entity.BaseEntity;
/**
 * 页面 管理
 * @author 超级管理员
 * @date 2016-11-04
 */
public class Mpage extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String title; // 名称
	private String color; // 背景颜色
	private String description; // 页面描述
	private Byte footerAble; // 显示底部
	private Byte type; // 页面类型: 0 普通页面， 1 主页
	private Byte isEnabled; // 上架
	private String shopId; // 店铺ID
	private Byte goodsNum; // 商品数
	private Integer uv; // UV
	private Integer pv; // PV
	private Integer sort; // 排序
	private List<String> fields; // 组件
	private List<MpageField> mfields;// 真实的组件
	
	// 对外地址
	private String url;
	
    public Byte getFooterAble() {
		return footerAble;
	}
	public void setFooterAble(Byte footerAble) {
		this.footerAble = footerAble;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String getName() {
		return title;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
    public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
    public Byte getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Byte isEnabled) {
		this.isEnabled = isEnabled;
	}
    public String getShopId() {
		return shopId;
	}
	public void setShopId(String shopId) {
		this.shopId = shopId;
	}
    public Byte getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(Byte goodsNum) {
		this.goodsNum = goodsNum;
	}
    public Integer getUv() {
		return uv;
	}
	public void setUv(Integer uv) {
		this.uv = uv;
	}
    public Integer getPv() {
		return pv;
	}
	public void setPv(Integer pv) {
		this.pv = pv;
	}
    public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public List<String> getFields() {
		return fields;
	}
	public void setFields(List<String> fields) {
		this.fields = fields;
	}
	public List<MpageField> getMfields() {
		return mfields;
	}
	public void setMfields(List<MpageField> mfields) {
		this.mfields = mfields;
	}
}