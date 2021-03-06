package com.tmt.wechat.bean.pay;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.tmt.core.utils.CDataAdapter;

/**
 * 支付通知返回结果
 * @author root
 *
 */
@XmlRootElement(name="xml")
@XmlAccessorType(XmlAccessType.FIELD)
public class PayNotifyReply {

	@XmlElement
	@XmlJavaTypeAdapter(value = CDataAdapter.class)
	private String return_code;
	@XmlElement
	@XmlJavaTypeAdapter(value = CDataAdapter.class)
	private String return_msg;
	
	public String getReturn_code() {
		return return_code;
	}
	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}
	public String getReturn_msg() {
		return return_msg;
	}
	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}
	
	/**
	 * 支付成功
	 * @return
	 */
	public static PayNotifyReply success() {
		PayNotifyReply reply = new PayNotifyReply();
		reply.setReturn_code("SUCCESS");
		reply.setReturn_msg("OK");
		return reply;
	}
	
	/**
	 * 支付失败
	 * @return
	 */
	public static PayNotifyReply fail() {
		PayNotifyReply reply = new PayNotifyReply();
		reply.setReturn_code("FAIL");
		reply.setReturn_msg("FAIL");
		return reply;
	}
	
}
