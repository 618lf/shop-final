package com.tmt.map.entity;

import java.io.Serializable;

/**
 * 位置
 * @author root
 */
public class LocationPoi implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String name; // 市
	private String address; // 详细地址
	private String lat; // 经度
	private String lng; // 纬度
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLng() {
		return lng;
	}
	public void setLng(String lng) {
		this.lng = lng;
	}
	
	/**
	 * 创建一个地址
	 * @param name
	 * @param address
	 * @param lat
	 * @param lng
	 * @return
	 */
	public static LocationPoi newPoi(String name, String address, String lat, String lng) {
		LocationPoi poi = new LocationPoi();
		poi.name = name;
		poi.address = address;
		poi.lat = lat;
		poi.lng = lng;
		return poi;
	}
}