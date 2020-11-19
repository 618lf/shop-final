package com.tmt.notice;

/**
 * 实时的消息发送
 * @author lifeng
 */
public interface ActualNotice {

	/**
	 * 发送消息
	 * @param message
	 */
	void sendMessage(String message);
}