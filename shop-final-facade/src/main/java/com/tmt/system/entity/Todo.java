package com.tmt.system.entity;

import java.io.Serializable;

/**
 * 待办事项
 * @author root
 *
 */
public class Todo implements Serializable, Comparable<Todo>{

	public static final int NORMAL = 0;
	public static final int MEDIUM = 1;
	public static final int IMPORTANT = 2;
	public static final int SERIOUS = 3;
	private static final long serialVersionUID = 1L;
	private String icon;
	private String name;
	private int count;
	private String url;
	private int sort;
	private int level; // 级别：0 一般， 1 中等， 2 重要， 3 严重（2，3加入弹出底部提示）
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getSort() {
		return sort;
	}
	public void setSort(int sort) {
		this.sort = sort;
	}
	
	@Override
	public int compareTo(Todo o) {
		return o!= null?(this.sort>=o.sort?1:0):1;
	}
	
	//提供的工具类
	public static Todo normal(String name, String url, int count, int sort) {
		Todo todo = new Todo();
		todo.setName(name);
		todo.setUrl(url);
		todo.setLevel(NORMAL);
		todo.setSort(sort);
		todo.setCount(count);
		return todo;
	}
	public static Todo medium(String name, String url, int count, int sort) {
		Todo todo = Todo.normal(name, url, count, sort);
		todo.setLevel(MEDIUM);
		return todo;
	}
	public static Todo important(String name, String url, int count, int sort) {
		Todo todo = Todo.normal(name, url, count, sort);
		todo.setLevel(IMPORTANT);
		return todo;
	}
	public static Todo serious(String name, String url, int count, int sort) {
		Todo todo = Todo.normal(name, url, count, sort);
		todo.setLevel(SERIOUS);
		return todo;
	}
}