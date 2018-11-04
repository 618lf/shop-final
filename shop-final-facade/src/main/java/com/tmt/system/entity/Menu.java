package com.tmt.system.entity;

import java.io.Serializable;
import java.util.List;

import com.tmt.common.entity.BaseTreeEntity;
import com.tmt.common.utils.Lists;

/**
 * 菜单
 * @author lifeng
 */
public class Menu extends BaseTreeEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
    private String href;
    private String subMenu; // 副菜单
    private String target;
    private String iconClass;
    private String isShow;
    private String isActiviti;
    private String permission;
    private Byte type;//菜单类型 ： 1 目录 。。。
    private String degree;
    private String quickMenu;//快捷菜单图标，如果设置了此菜单
    private String topMenu;//置顶菜单图标，暂时没用到
    
	private int childrenCount;
    private List<Menu> children;
    private Menu parentMenu;
    
	public String getSubMenu() {
		return subMenu == null ? "":subMenu;
	}
	public void setSubMenu(String subMenu) {
		this.subMenu = subMenu;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getQuickMenu() {
		return quickMenu;
	}
	public void setQuickMenu(String quickMenu) {
		this.quickMenu = quickMenu;
	}
	public String getTopMenu() {
		return topMenu;
	}
	public void setTopMenu(String topMenu) {
		this.topMenu = topMenu;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Menu getParentMenu() {
		return parentMenu;
	}
	public void setParentMenu(Menu parentMenu) {
		this.parentMenu = parentMenu;
	}
	public List<Menu> getChildren() {
		return children;
	}
	public void setChildren(List<Menu> children) {
		this.children = children;
	}
	public void addChild( Menu child) {
		if( this.children == null ) {
			this.children = Lists.newArrayList();
		}
		this.children.add(child);
	}
	public int getChildrenCount() {
		return childrenCount;
	}
	public void setChildrenCount(int childrenCount) {
		this.childrenCount = childrenCount;
	}
	public Byte getType() {
		return type;
	}
	public void setType(Byte type) {
		this.type = type;
	}
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public String getIconClass() {
		return iconClass;
	}
	public void setIconClass(String icon) {
		this.iconClass = icon;
	}
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	public String getIsActiviti() {
		return isActiviti;
	}
	public void setIsActiviti(String isActiviti) {
		this.isActiviti = isActiviti;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
}