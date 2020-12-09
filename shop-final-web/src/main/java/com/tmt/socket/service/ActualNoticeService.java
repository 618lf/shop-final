package com.tmt.socket.service;

import java.util.concurrent.CopyOnWriteArraySet;

import org.springframework.stereotype.Component;

import com.tmt.common.utils.SpringContextHolder;
import com.tmt.notice.ActualNotice;

/**
 * Spring 的组件
 * @author lifeng
 */
@Component
public class ActualNoticeService implements ActualNotice {

	// 所有链接上来的服务器
    private CopyOnWriteArraySet<ActualNotice> ADMINS = new CopyOnWriteArraySet<ActualNotice>(); 
	
    /**
     * 发送消息
     */
	@Override
	public void sendMessage(String message) {
        for(ActualNotice item: ADMINS){               
        	item.sendMessage(message);
        }
	}
	
	/**
	 * 添加客户端
	 * @param client
	 */
	public void addClient(ActualNotice client) {
		ADMINS.add(client);
	}
	
	/**
	 * 添加客户端
	 * @param client
	 */
	public void removeClient(ActualNotice client) {
		ADMINS.remove(client);
	}
	
	/**
	 * 得到服务器
	 * @return
	 */
	public static ActualNoticeService getServer() {
		return SpringContextHolder.getBean(ActualNoticeService.class);
	}
}