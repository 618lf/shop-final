package com.tmt.map.entity;

import java.util.List;

import com.tmt.map.entity.tencent.Location;

/**
 * 查询结果
 * @author root
 */
public class TencentSuggestionObj {

	private Integer status;
	private String message;
	private Integer count;
	private List<Poi>  data;
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public List<Poi> getData() {
		return data;
	}
	public void setData(List<Poi> data) {
		this.data = data;
	}

	// 位置
	public static class Poi {
		private String id;
		private String title;
		private String address;
		private String province;
		private String city;
		private String adcode;
		private Integer type;
		private Location location;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public String getProvince() {
			return province;
		}
		public void setProvince(String province) {
			this.province = province;
		}
		public String getCity() {
			return city;
		}
		public void setCity(String city) {
			this.city = city;
		}
		public String getAdcode() {
			return adcode;
		}
		public void setAdcode(String adcode) {
			this.adcode = adcode;
		}
		public Integer getType() {
			return type;
		}
		public void setType(Integer type) {
			this.type = type;
		}
		public Location getLocation() {
			return location;
		}
		public void setLocation(Location location) {
			this.location = location;
		}
	}
}