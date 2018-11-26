package com.tmt.common.web.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tmt.common.utils.StringUtil3;

/**
 * GET参数编码格式转换 合并了POST参数编码的转换
 * 
 * @author liFeng 2014年8月17日
 */
public class EncodingConvertFilter extends OncePerRequestFilter {

	protected static Logger logger = LoggerFactory.getLogger(EncodingConvertFilter.class);

	private String fromEncoding = "ISO-8859-1";
	private String toEncoding = "UTF-8";
	private boolean forceEncoding = true;
	private boolean forceGetEncoding = true;

	/**
	 * 处理get 参数的编码转换，Tomcat 等服务器中不需要配置编码转换
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		HttpServletRequest _request = request;
		// 默认处理POST
		if (this.toEncoding != null && (this.forceEncoding || request.getCharacterEncoding() == null)) {
			request.setCharacterEncoding(this.toEncoding);
			if (this.forceEncoding) {
				response.setCharacterEncoding(this.toEncoding);
			}
		}
		// GET参数处理
		if (this.toEncoding != null && this.forceGetEncoding && "GET".equalsIgnoreCase(request.getMethod())) {
			_request = new GetHttpServletRequest(request);
		}
		filterChain.doFilter(_request, response);
	}

	public String getFromEncoding() {
		return fromEncoding;
	}

	public void setFromEncoding(String fromEncoding) {
		this.fromEncoding = fromEncoding;
	}

	public String getToEncoding() {
		return toEncoding;
	}

	public void setToEncoding(String toEncoding) {
		this.toEncoding = toEncoding;
	}

	public boolean isForceEncoding() {
		return forceEncoding;
	}

	public void setForceEncoding(boolean forceEncoding) {
		this.forceEncoding = forceEncoding;
	}

	public boolean isForceGetEncoding() {
		return forceGetEncoding;
	}

	public void setForceGetEncoding(boolean forceGetEncoding) {
		this.forceGetEncoding = forceGetEncoding;
	}

	/**
	 * 封装
	 * 
	 * @author lifeng
	 */
	public class GetHttpServletRequest extends HttpServletRequestWrapper {

		HttpServletRequest request;

		public GetHttpServletRequest(HttpServletRequest request) {
			super(request);
			this.request = request;
		}

		@Override
		public String getParameter(String name) {
			// 默认解码ISO-8859-1
			String value = this.request.getParameter(name);
			if (value == null) {
				return null;
			}
			try {
				// 1.value.getBytes(fromEncoding) 重新编码
				// 2.new String 重新解码
				value = new String(value.getBytes(fromEncoding), toEncoding);
				value = StringUtil3.mb4Replace(value, null);
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			return value;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public Map getParameterMap() {
			Iterator<String[]> localIterator = this.request.getParameterMap().values().iterator();
			while (localIterator.hasNext()) {
				String[] arrayOfString = localIterator.next();
				for (int i = 0; i < arrayOfString.length; i++) {
					try {
						arrayOfString[i] = StringUtil3
								.mb4Replace(new String(arrayOfString[i].getBytes(fromEncoding), toEncoding), null);
					} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
						localUnsupportedEncodingException.printStackTrace();
					}
				}
			}
			return super.getParameterMap();
		}

		@Override
		public String[] getParameterValues(String name) {
			String[] values = this.request.getParameterValues(name);
			if (values == null) {
				return null;
			}
			try {
				for (int i = 0; i < values.length; i++) {
					values[i] = StringUtil3.mb4Replace(new String(values[i].getBytes(fromEncoding), toEncoding), null);
				}
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}

			return values;
		}
	}
}