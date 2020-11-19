package com.tmt.shop.entity;

import java.io.Serializable;
import java.util.List;

import com.tmt.core.entity.BaseTreeEntity;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;

/**
 * 商品分类 管理
 * @author 超级管理员
 * @date 2016-01-21
 */
public class Category extends BaseTreeEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String name; // 栏目名称
	private Integer sort; // 排序（升序）
	private String path; // 路径
	private String seoDescription; // 页面描述
	private String seoKeywords; // 页面关键字
	private String seoTitle; // 页面标题
	private String large; // 大图
	private String medium; // 中图
	private String thumbnail; // 小图
	private Byte isList = Category.YES; // 是否显示
	private List<CategoryBrand> brands;// 品牌
	private List<Category> categorys; // 子分类
	private String url;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<Category> getCategorys() {
		return categorys;
	}
	public void setCategorys(List<Category> categorys) {
		this.categorys = categorys;
	}
	public void addCategory(Category category) {
		if (this.categorys == null) {
			this.categorys = Lists.newArrayList();
		}
		this.categorys.add(category);
	}
	public List<CategoryBrand> getBrands() {
		return brands;
	}
	public void setBrands(List<CategoryBrand> brands) {
		this.brands = brands;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
    public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
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
    public String getLarge() {
		return large;
	}
	public void setLarge(String large) {
		this.large = large;
	}
    public String getMedium() {
		return medium;
	}
	public void setMedium(String medium) {
		this.medium = medium;
	}
    public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
    public Byte getIsList() {
		return isList;
	}
	public void setIsList(Byte isList) {
		this.isList = isList;
	}
	/**
	 * 父节点的Path修改之后，所有的子节点都要修改
	 * 
	 * @param parent
	 * @param oldParentIds
	 */
	public void updatePathByParent(Category parent, String oldParentIds, String oldPaths, Integer oldLevel) {
		super.updateIdsByParent(parent, oldParentIds, oldPaths, oldLevel);
		String _paths = (this.getPath()).replace(oldPaths, parent.getPath());
		if (StringUtils.isBlank(_paths)) {
			_paths = new StringBuilder(PATH_SEPARATE).append(this.getName()).toString() ;
		}
		this.setPath(_paths);
	}
}
