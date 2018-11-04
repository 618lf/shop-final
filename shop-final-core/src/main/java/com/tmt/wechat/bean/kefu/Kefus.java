package com.tmt.wechat.bean.kefu;

import java.io.Serializable;
import java.util.List;

public class Kefus implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Kefu> kf_list;

	public List<Kefu> getKf_list() {
		return kf_list;
	}

	public void setKf_list(List<Kefu> kf_list) {
		this.kf_list = kf_list;
	}
}
