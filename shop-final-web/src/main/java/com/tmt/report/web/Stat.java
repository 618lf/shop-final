package com.tmt.report.web;

/**
 * 统计
 * @author lifeng
 */
public class Stat implements Comparable<Stat>{

	private String x;
	private Object y;
	
	public String getX() {
		return x;
	}
	public void setX(String x) {
		this.x = x;
	}
	public Object getY() {
		return y;
	}
	public void setY(Object y) {
		this.y = y;
	}
	@Override
	public int compareTo(Stat o) {
		return this.getX().compareTo(o.getX());
	}
}