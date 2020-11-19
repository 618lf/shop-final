package com.tmt.shop.state.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tmt.common.utils.SpringContextHolder;
import com.tmt.shop.entity.OrderState;
import com.tmt.shop.event.EventHandler;
import com.tmt.shop.service.OrderServiceFacade;
import com.tmt.shop.service.OrderStateServiceFacade;
import com.tmt.shop.state.StateHandler;

/**
 * 默认的处理器
 * @author root
 *
 */
public abstract class DefaultStateHandler implements StateHandler{

	// 相关服务
	protected OrderServiceFacade orderService;
	protected OrderStateServiceFacade orderStateService;
	
	protected Logger logger = LoggerFactory.getLogger(EventHandler.class);
	
	protected StateHandler handler;
	
	/**
	 * 初始化服务
	 */
	public DefaultStateHandler() {
		orderService = SpringContextHolder.getBean(OrderServiceFacade.class);
		orderStateService = SpringContextHolder.getBean(OrderStateServiceFacade.class);
	}
	
	@Override
	public Byte doHandler(OrderState state) {
		Byte flag = this.doInnerHandler(state);
		
		// 不为 -1 则不执行下面的处理
		if (flag != -1) {
			return flag;
		}
		
		// 有下一个处理器，则处理
		if (this.handler != null) {
			return this.handler.doHandler(state);
		}
		
		// 没有找到处理器
		return -1;
	}
	
	@Override
	public void setNextHandler(StateHandler handler) {
		this.handler = handler;
	}
	
	/**
	 * 子类需要实现的处理器
	 * @param event
	 * @return
	 */
	protected abstract Byte doInnerHandler(OrderState state);
}
