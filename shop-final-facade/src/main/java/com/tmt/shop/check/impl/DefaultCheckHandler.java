package com.tmt.shop.check.impl;

import com.tmt.core.utils.SpringContextHolder;
import com.tmt.shop.check.CheckHandler;
import com.tmt.shop.check.CheckWrap;
import com.tmt.shop.check.OrderCheckResult;
import com.tmt.shop.service.ProductServiceFacade;

/**
 * 默认的处理器
 * @author lifeng
 */
public abstract class DefaultCheckHandler implements CheckHandler{

	CheckHandler handler;
	protected ProductServiceFacade productService;
	public DefaultCheckHandler() {
		productService = SpringContextHolder.getBean(ProductServiceFacade.class);
	}
	
	@Override
	public OrderCheckResult doHandler(CheckWrap check) {
		OrderCheckResult result = this.doInnerHandler(check);
		if (result != null) {
			return result;
		}
		
		// 有下一个处理器，则处理
		if (this.handler != null) {
		    return this.handler.doHandler(check);
		}
		return null;
	}

	@Override
	public void setNextHandler(CheckHandler handler) {
		this.handler = handler;
	}
	
	/**
	 * 子类需要实现的处理器
	 * @param event
	 * @return
	 */
	protected abstract OrderCheckResult doInnerHandler(CheckWrap check);
}