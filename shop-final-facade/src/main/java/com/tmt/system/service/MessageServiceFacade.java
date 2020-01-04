package com.tmt.system.service;

import java.util.List;

import com.tmt.core.service.BaseServiceFacade;
import com.tmt.system.entity.Message;
import com.tmt.system.entity.User;

public interface MessageServiceFacade extends BaseServiceFacade<Message, Long>{

	/**
	 * 保存
	 * @param message
	 */
	public void save(Message message);
	
	/**
	 * 批量发送站内信
	 * @param message
	 */
	public void send(Message template);
	
	/**
	 * 批量移到垃圾箱
	 * @param messages
	 */
	public void toTrash(List<Message> messages);
	
	/**
	 * 批量移到垃圾箱
	 * @param messages
	 */
	public void read(Message message);
	
	/**
	 * 垃圾箱批量删除
	 * @param messages
	 */
	public void delete(List<Message> messages);
	
	/**
	 * 当前用户未读站内信
	 * @return
	 */
	public Integer getUnreadCount(User user);
	
	/**
	 * 读取前几条
	 * @return
	 */
	public List<Message> queryUnReadTops(User user, int size);
}