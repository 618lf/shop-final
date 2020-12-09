package com.tmt.map.entity;

import java.util.List;

import com.tmt.map.entity.tencent.AdInfo;
import com.tmt.map.entity.tencent.Location;

/**
 * 腾讯位置
 * @author root
 */
public class TencentResultObj {
	
	private Integer status;
	private String message;
	private Integer count;
	private List<Poi> data;
	
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
		private String tel;
		private String category;
		private Integer type;
		private Location location;
		private AdInfo ad_info;
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
		public String getTel() {
			return tel;
		}
		public void setTel(String tel) {
			this.tel = tel;
		}
		public String getCategory() {
			return category;
		}
		public void setCategory(String category) {
			this.category = category;
		}
		public AdInfo getAd_info() {
			return ad_info;
		}
		public void setAd_info(AdInfo ad_info) {
			this.ad_info = ad_info;
		}
	}
}