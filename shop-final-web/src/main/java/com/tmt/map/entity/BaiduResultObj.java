package com.tmt.map.entity;

import java.util.List;

/**
 * 百度地图返回对象
 * 
 * @author 超级管理员
 * @date 2016-01-21
 */
public class BaiduResultObj {

	private Content content;
	private Result result;
	public Content getContent() {
		return content;
	}
	public void setContent(Content content) {
		this.content = content;
	}
	public Result getResult() {
		return result;
	}
	public void setResult(Result result) {
		this.result = result;
	}

	public static class Content {
		private LatLng location;
		private String locid;
		private int radius;
		private double confidence;
		private String location_description;
		private List<Poi> pois;
		public int getRadius() {
			return radius;
		}
		public void setRadius(int radius) {
			this.radius = radius;
		}
		public double getConfidence() {
			return confidence;
		}
		public void setConfidence(double confidence) {
			this.confidence = confidence;
		}
		public LatLng getLocation() {
			return location;
		}
		public void setLocation(LatLng location) {
			this.location = location;
		}
		public String getLocid() {
			return locid;
		}
		public void setLocid(String locid) {
			this.locid = locid;
		}
		public String getLocation_description() {
			return location_description;
		}
		public void setLocation_description(String location_description) {
			this.location_description = location_description;
		}
		public List<Poi> getPois() {
			return pois;
		}
		public void setPois(List<Poi> pois) {
			this.pois = pois;
		}
	}

	public static class Poi {
		private String name;
		private String address;
		private String tag;
		private String uid;
		private LatLng location;
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
		public String getTag() {
			return tag;
		}
		public void setTag(String tag) {
			this.tag = tag;
		}
		public String getUid() {
			return uid;
		}
		public void setUid(String uid) {
			this.uid = uid;
		}
		public LatLng getLocation() {
			return location;
		}
		public void setLocation(LatLng location) {
			this.location = location;
		}
		// 得到经纬度
		public String getLocations() {
			if (this.location != null) {
				return new StringBuilder(this.location.getLat()).append("-").append(this.location.getLng()).toString();
			}
			return "";
		}
	}

	public static class LatLng {
		private String lat;
		private String lng;
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
	}

	public static class Result {
		private int error;
		private String loc_time;
		public int getError() {
			return error;
		}
		public void setError(int error) {
			this.error = error;
		}
		public String getLoc_time() {
			return loc_time;
		}
		public void setLoc_time(String loc_time) {
			this.loc_time = loc_time;
		}
	}
}