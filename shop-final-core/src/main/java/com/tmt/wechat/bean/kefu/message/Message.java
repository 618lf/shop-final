package com.tmt.wechat.bean.kefu.message;

import java.io.Serializable;

/**
 * 客服消息
 * @author lifeng
 */
public abstract class Message implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String touser;
	private String msgtype;
    private KefuAccount customservice;
    
	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getMsgtype() {
		return msgtype;
	}
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
	public KefuAccount getCustomservice() {
		return customservice;
	}
	public void setCustomservice(String kf_account) {
		this.customservice = new KefuAccount();
		this.customservice.setKf_account(kf_account);
	}

	/**
	 * 是否以客服的名义发送客服消息
	 * @author lifeng
	 *
	 */
	public class KefuAccount implements Serializable{
		private static final long serialVersionUID = 1L;
		private String kf_account;
		public String getKf_account() {
			return kf_account;
		}
		public void setKf_account(String kf_account) {
			this.kf_account = kf_account;
		}
	}
}