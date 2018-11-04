package com.tmt.system.utils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;

/**
 *  基于 logback 的错误处理
 * @author lifeng
 */
public class ErrorSMTPAppender extends AppenderBase<ILoggingEvent>{

	private String title = "系统错误";
	private String tos;
	protected Layout<ILoggingEvent> layout;
	
	@Override
	protected void append(ILoggingEvent event) {
		if (event.getLevel() == Level.ERROR) {
			String error = layout.doLayout(event);
			context.getScheduledExecutorService().execute(new Runnable() {
				@Override
				public void run() {
					MessageUtils.sendErrorMessage(error);
					EmailUtils.sendEmail(title, tos, error);
				}
			});
		}
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTos() {
		return tos;
	}
	public void setTos(String tos) {
		this.tos = tos;
	}
	public Layout<ILoggingEvent> getLayout() {
		return layout;
	}
	public void setLayout(Layout<ILoggingEvent> layout) {
		this.layout = layout;
	}
}