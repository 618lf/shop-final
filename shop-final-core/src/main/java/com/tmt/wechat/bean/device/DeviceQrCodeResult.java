package com.tmt.wechat.bean.device;

import java.io.Serializable;

import com.tmt.wechat.bean.base.BaseResult;

public class DeviceQrCodeResult implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String deviceid;
	private String qrticket;
	private String devicelicence;
	private BaseResult base_resp;
	public String getDeviceid() {
		return deviceid;
	}
	public void setDeviceid(String deviceid) {
		this.deviceid = deviceid;
	}
	public String getQrticket() {
		return qrticket;
	}
	public void setQrticket(String qrticket) {
		this.qrticket = qrticket;
	}
	public String getDevicelicence() {
		return devicelicence;
	}
	public void setDevicelicence(String devicelicence) {
		this.devicelicence = devicelicence;
	}
	public BaseResult getBase_resp() {
		return base_resp;
	}
	public void setBase_resp(BaseResult base_resp) {
		this.base_resp = base_resp;
	}
}
