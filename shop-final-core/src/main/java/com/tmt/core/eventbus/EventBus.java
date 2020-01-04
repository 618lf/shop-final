package com.tmt.core.eventbus;

import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import com.google.common.eventbus.AsyncEventBus;

/**
 * 事件驱动
 * 
 * @author lifeng
 */
public class EventBus {
	private final com.google.common.eventbus.EventBus eventBus;
	private static EventBus me = null;
	private volatile boolean inited = false;

	public EventBus(Integer coreThreads) {
		eventBus = new AsyncEventBus("EventBus",
				Executors.newFixedThreadPool(coreThreads, new EventLoopFactory(true, "EventBus", new AtomicLong())));
		me = this;
	}

	/**
	 * 初始化,返回当前是否已初始化
	 */
	public synchronized void init(Consumer<Boolean> register) {
		if (!inited) {
			this.delayConsumer(register);
		}
		inited = true;
	}

	/**
	 * 延迟10s 注册成为消费者
	 * 
	 * @param register
	 * @return
	 */
	private Consumer<Boolean> delayConsumer(Consumer<Boolean> register) {
		return (t) -> {
			new Thread(() -> {
				try {
					Thread.sleep(10 * 1000);
				} catch (Exception e) {
				}
				register.accept(t);
			}).start();
		};
	}

	/**
	 * 注册成为事件消费者
	 * 
	 * @param object
	 */
	public void register(Object object) {
		eventBus.register(object);
	}

	/**
	 * 发送事件
	 * 
	 * @param event
	 */
	public void post(Event event) {
		eventBus.post(event);
	}

	/**
	 * 当前对象
	 * 
	 * @return
	 */
	public static EventBus me() {
		return me;
	}
}
