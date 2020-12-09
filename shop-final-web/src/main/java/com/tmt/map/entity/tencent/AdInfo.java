package com.tmt.map.entity.tencent;

/**
 * 具体的地址
 * @author root
 */
public class AdInfo {
	
	private String nation;
	private String province;
	private String city;
	private Integer adcode;
	
	public String getNation() {
		return nation;
	}
	public void setNation(String nation) {
		this.nation = nation;
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
	public Integer getAdcode() {
		return adcode;
	}
	public void setAdcode(Integer adcode) {
		this.adcode = adcode;
	}
}
