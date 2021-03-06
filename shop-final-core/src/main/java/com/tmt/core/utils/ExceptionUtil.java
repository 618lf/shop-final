package com.tmt.core.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.commons.io.IOUtils;

/**
 * 返回异常错误信息
 * 
 * @author liFeng 2014年6月23日
 */
public class ExceptionUtil {

	/**
	 * 返回错误信息字符串
	 * 
	 * @param ex Exception
	 * @return 错误信息字符串
	 */
	public static String getMessage(Throwable ex) {
		StringWriter sw = new StringWriter();
		try {
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			return sw.toString();
		} finally {
			IOUtils.closeQuietly(sw);
		}
	}

	/**
	 * 将CheckedException转换为UncheckedException.
	 */
	public static RuntimeException unchecked(Exception e) {
		if (e instanceof RuntimeException) {
			return (RuntimeException) e;
		} else {
			return new RuntimeException(e);
		}
	}

	/**
	 * 判断异常是否由某些底层的异常引起.
	 */
	@SuppressWarnings("unchecked")
	public static boolean isCausedBy(Exception ex, Class<? extends Exception>... causeExceptionClasses) {
		Throwable cause = ex.getCause();
		while (cause != null) {
			for (Class<? extends Exception> causeClass : causeExceptionClasses) {
				if (causeClass.isInstance(cause)) {
					return true;
				}
			}
			cause = cause.getCause();
		}
		return false;
	}

	/**
	 * 返回最底层的错误信息
	 * 
	 * @param ex
	 * @return
	 */
	public static String getCauseMessage(Throwable ex) {
		return getCauseMessage(ex, null);
	}

	/**
	 * 返回最底层的错误信息
	 * 
	 * @param ex           异常
	 * @param messageCount 异常的数据量
	 * @return
	 */
	public static String getCauseMessage(Throwable ex, Integer messageLimit) {
		Throwable cause = ex;
		while (cause.getCause() != null) {
			cause = cause.getCause();
		}
		String message = getMessage(cause);
		return messageLimit != null ? StringUtils.abbreviate(message, messageLimit) : message;
	}
}
