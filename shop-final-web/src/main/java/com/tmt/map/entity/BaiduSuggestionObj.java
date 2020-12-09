package com.tmt.map.entity;

import java.util.List;

/**
 * 百度地图返回对象
 * 
 * @author 超级管理员
 * @date 2016-01-21
 */
public class BaiduSuggestionObj {
	private List<Poi> result;
	private String status;
	private String message;
	public List<Poi> getResult() {
		return result;
	}
	public void setResult(List<Poi> result) {
		this.result = result;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public static class Poi {
		private String name;
		private String uid;
		private String city;
		private String district;
		private LatLng location;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getUid() {
			return uid;
		}
		public void setUid(String uid) {
			this.uid = uid;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getDistrict() {
			return district;
		}
		public void setDistrict(String district) {
			this.district = district;
		}
		public LatLng getLocation() {
			return location;
		}
		public void setLocation(LatLng location) {
			this.location = location;
		}
		public String getAddress() {
			return new StringBuilder(city).append(district).toString();
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
}
