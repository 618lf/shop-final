package com.tmt.system.entity;

import java.io.Serializable;

import com.tmt.common.entity.BaseEntity;

/**
 * 角色
 * 数据范围，
 *       本人
 *       本部门（当用用户所在部门的所有人）
 *       本公司（当用用户所在公司门的所有人）
 *       本区域（当用用户所在公司的区域的所有人）
 *       所有数据
 * @author lifeng
 */
public class Role extends BaseEntity<Long> implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Long officeId;
	private String officeCode;
    private String officeName;
    private String code;
    private DataScope dataScope;
    private String menuIds;
    private String menuNames;
    private String optionIds;
    private String optionNames;
    private String permission;
    
	public String getOfficeCode() {
		return officeCode;
	}
	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}
	public String getPermission() {
		return permission;
	}
	public void setPermission(String permission) {
		this.permission = permission;
	}
	public String getOptionIds() {
		return optionIds;
	}
	public void setOptionIds(String optionIds) {
		this.optionIds = optionIds;
	}
	public String getOptionNames() {
		return optionNames;
	}
	public void setOptionNames(String optionNames) {
		this.optionNames = optionNames;
	}
	public String getOfficeName() {
		return officeName;
	}
	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
	public String getMenuIds() {
		return menuIds;
	}
	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}
	public String getMenuNames() {
		return menuNames;
	}
	public void setMenuNames(String menuNames) {
		this.menuNames = menuNames;
	}
	public Long getOfficeId() {
		return officeId;
	}
	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public DataScope getDataScope() {
		return dataScope;
	}
	public void setDataScope(DataScope dataScope) {
		this.dataScope = dataScope;
	}
	/**
	 * 数据范围，
	 *       本人
	 *       本部门（当用用户所在部门的所有人）
	 *       本公司（当用用户所在公司门的所有人）
	 *       本区域（当用用户所在公司的区域的所有人）
	 *       所有数据
	 * @author lifeng
	 */
	public enum DataScope {
		USER("我创建的数据"), OFFICE("我所在的组"), DEPART("我所在的部门"), COMPANY("我所在的公司"), AREA("我所在的区域"), ALL("所有数据");
		private String name;
		private DataScope(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
}