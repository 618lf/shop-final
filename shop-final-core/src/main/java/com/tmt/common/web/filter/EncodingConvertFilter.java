package com.tmt.common.web.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import com.tmt.Constants;

/**
 * GET参数编码格式转换 合并了POST参数编码的转换
 * 
 * @author liFeng 2014年8月17日
 */
public class EncodingConvertFilter extends OncePerRequestFilter {

	protected static Logger logger = LoggerFactory.getLogger(EncodingConvertFilter.class);

	private String toEncoding = Constants.DEFAULT_ENCODING.toString();

	/**
	 * 处理get 参数的编码转换，Tomcat 等服务器中不需要配置编码转换
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		HttpServletRequest _request = request;
		if (this.toEncoding != null) {
			request.setCharacterEncoding(this.toEncoding);
			response.setCharacterEncoding(this.toEncoding);
		}
		filterChain.doFilter(_request, response);
	}

	public String getToEncoding() {
		return toEncoding;
	}

	public void setToEncoding(String toEncoding) {
		this.toEncoding = toEncoding;
	}
}