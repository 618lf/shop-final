package com.tmt.core.searcher;

/**
 * 上一个下一个
 * @author root
 * @param <T>
 */
public class ValueWrapper {
	private String prev;
	private String next;
	public String getPrev() {
		return prev;
	}
	public void setPrev(String prev) {
		this.prev = prev;
	}
	public String getNext() {
		return next;
	}
	public void setNext(String next) {
		this.next = next;
	}
}