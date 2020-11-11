package com.tmt.wechat.entity;

import java.io.Serializable;
import java.util.List;

import com.tmt.core.entity.BaseEntity;
import com.tmt.core.utils.time.DateUtils;
/**
 * 自定义菜单 管理
 * @author 超级管理员
 * @date 2016-09-13
 */
public class Menu extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long parentId;
	private String appId; // 对应公众号APP_ID
	private String name; // 按钮名称
	private Byte sort; // 排序
	private Byte type; // see com.tmt.wechat.entity.WechatConstant
	private String config; // 对应的配置内容
	private Byte status; // 状态：0不显示， 1显示
	private List<Menu> menus;// 子菜单
	private Byte publish; // 是否发布
    
	public Byte getPublish() {
		return publish;
	}
	public void setPublish(Byte publish) {
		this.publish = publish;
	}
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
    public Byte getSort() {
		return sort;
	}
	public void setSort(Byte sort) {
		this.sort = sort;
	}
    public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
    public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}
    public Byte getStatus() {
		return status;
	}
	public void setStatus(Byte status) {
		this.status = status;
	}
	public List<Menu> getMenus() {
		return menus;
	}
	public void setMenus(List<Menu> menus) {
		this.menus = menus;
	}
	@Override
	public Long prePersist() {
		this.createDate = DateUtils.getTimeStampNow();
		this.updateDate = this.createDate;
		return this.id;
	}
}