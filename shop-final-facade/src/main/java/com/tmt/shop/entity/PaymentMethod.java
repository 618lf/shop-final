package com.tmt.shop.entity;

import java.io.Serializable;

import com.tmt.core.entity.BaseEntity;

/**
 * 支付方式 管理
 * 
 * @author 超级管理员
 * @date 2015-11-04
 */
public class PaymentMethod extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name; // 名称
	private Integer sort; // 排序
	private String icon; // 图标
	private Integer timeout; // 超时时间
	private Type type; // 类型
	private Method method; // 方式
	private String content; // 内容

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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getTimeout() {
		return timeout;
	}

	public void setTimeout(Integer timeout) {
		this.timeout = timeout;
	}

	public Type getType() {
		return type;
	}

	public String getTypeName() {
		return type != null ? type.getName() : "";
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Method getMethod() {
		return method;
	}

	public String getMethodName() {
		return method != null ? method.getName() : "";
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	// 定义枚举,自行定义枚举具体值
	public enum Type {
		BEFORE_DELIVER("货到付款"), BEFORE_PAYED("款到发货");

		private String name;

		private Type(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	// 定义枚举,自行定义枚举具体值
	public enum Method {
		ON_LINE("在线支付"), OFF_LINE("线下支付");

		private String name;

		private Method(String name) {
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
