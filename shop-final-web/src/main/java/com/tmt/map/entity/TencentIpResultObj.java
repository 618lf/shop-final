package com.tmt.map.entity;

import com.tmt.map.entity.tencent.AdInfo;
import com.tmt.map.entity.tencent.Location;

/**
 * 腾讯(IP定位)
 * @author root
 */
public class TencentIpResultObj {
    
	private Integer status;
	private String message;
	private Result result;
	
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

	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}

	public static class Result {
		private Location location;
		private AdInfo ad_info;
		public Location getLocation() {
			return location;
		}
		public void setLocation(Location location) {
			this.location = location;
		}
		public AdInfo getAd_info() {
			return ad_info;
		}
		public void setAd_info(AdInfo ad_info) {
			this.ad_info = ad_info;
		}
	}
}