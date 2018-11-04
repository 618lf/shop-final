package com.tmt.wechat.bean.kefu;

import java.io.Serializable;
import java.util.List;

public class KefuOnlines implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Kefu> kf_online_list;

	public List<Kefu> getKf_online_list() {
		return kf_online_list;
	}

	public void setKf_online_list(List<Kefu> kf_online_list) {
		this.kf_online_list = kf_online_list;
	}
}