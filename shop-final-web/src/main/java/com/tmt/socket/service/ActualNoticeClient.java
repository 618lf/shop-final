package com.tmt.socket.service;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.tmt.notice.ActualNotice;

/**
 * 实例的监控
 * @author lifeng
 */
@ServerEndpoint(value="/actual/notice")  
public class ActualNoticeClient implements ActualNotice {
	
	// 对应的链接
	private Session session;
	
	/**
	 * 打开链接
	 * @param session
	 */
	@OnOpen  
    public void onOpen(Session session){  
		this.session = session;
		ActualNoticeService.getServer().addClient(this);
	}
	
	/**
	 * 连接关闭调用的方法 
	 */
    @OnClose  
    public void onClose(){  
    	ActualNoticeService.getServer().removeClient(this);
    } 
    
    /** 
     * 收到客户端消息后调用的方法 
     * @param message 客户端发送过来的消息 
     * @param session 可选的参数 
     */  
    @OnMessage  
    public void onMessage(String message, Session session) {}
    
    /**
     * 发生错误时调用 
     * @param session
     * @param error
     */
    @OnError  
    public void onError(Session session, Throwable error){}

    /**
     * 发送消息
     */
	@Override
	public void sendMessage(String message) {
		if(this.session != null
				&& this.session.isOpen()) {
			try {
				this.session.getBasicRemote().sendText(message);
			} catch (IOException e) {}  
		}
	} 
}