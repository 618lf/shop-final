package com.tmt.core.security.logback;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

import com.tmt.Constants;
import com.tmt.core.eventbus.EventBus;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;

/**
 * 支持通过 logback 将消息推送到 Mq 中
 * 
 * @see 优先将消息放入队列中
 * @author lifeng
 */
public class LogbackAppender extends AppenderBase<ILoggingEvent> {

	private Layout<ILoggingEvent> layout;
	private BlockingQueue<Event> events = null;
	private int maxExecutoSeconds = 30;
	private volatile AtomicBoolean sending = new AtomicBoolean(false);

	public int getMaxExecutoSeconds() {
		return maxExecutoSeconds;
	}
	
	public void setMaxExecutoSeconds(int maxExecutoSeconds) {
		this.maxExecutoSeconds = maxExecutoSeconds;
	}

	public Layout<ILoggingEvent> getLayout() {
		return layout;
	}

	public void setLayout(Layout<ILoggingEvent> layout) {
		this.layout = layout;
	}

	@Override
	public void start() {
		events = new LinkedBlockingDeque<>();
		super.start();
	}

	/**
	 * 添加到队列中
	 */
	@Override
	protected void append(ILoggingEvent event) {
		event.getThreadName();
		this.events.add(new Event(event));
		this.prepareTask();
	}

	/**
	 * 添加一次执行任务
	 */
	private void prepareTask() {
		if (sending.compareAndSet(false, true)) {
			new EventSender().run();
		}
	}

	/**
	 * Helper class to actually send LoggingEvents asynchronously.
	 */
	protected class EventSender implements Runnable {

		@Override
		public void run() {
			try {
				long now = System.currentTimeMillis();
				while (true) {

					// 会去需要发送的数据
					final Event event = events.poll();
					if (event == null) {
						break;
					}

					// 发送
					this.doSend(event);

					// 超过最大的执行时间，等待下一次的執行（需要下一次的触发）
					long dur = System.currentTimeMillis();
					if ((dur - now) / 1000 > maxExecutoSeconds) {
						break;
					}
				}
			} finally {
				this.prepareNextTask();
			}
		}

		/**
		 * 特殊情况下使用阻塞式的发送
		 * 
		 * @param event
		 */
		private void doSend(final Event event) {
			try {
				String msgBody = layout.doLayout(event.event);
				EventBus.me().post(new com.tmt.core.eventbus.Event(Constants.EVENT_LOG, msgBody));
			} catch (Exception e) {
			}
		}

		/**
		 * 准备下一次的执行
		 */
		private void prepareNextTask() {
			if (sending.compareAndSet(true, false) && events.peek() != null) {
				prepareTask();
			}
		}
	}

	/**
	 * Small helper class to encapsulate a LoggingEvent, its MDC properties, and the
	 * number of retries.
	 */
	protected static class Event {

		private final ILoggingEvent event;

		private final Map<String, String> properties;

		public Event(ILoggingEvent event) {
			this.event = event;
			this.properties = this.event.getMDCPropertyMap();
		}

		public ILoggingEvent getEvent() {
			return this.event;
		}

		public Map<String, String> getProperties() {
			return this.properties;
		}
	}
}