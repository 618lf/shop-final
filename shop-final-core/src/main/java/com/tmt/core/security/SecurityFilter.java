package com.tmt.core.security;

import java.io.IOException;
import java.util.concurrent.Callable;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;

import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.security.filter.OncePerRequestFilter;
import com.tmt.core.security.mgt.FilterChainResolver;
import com.tmt.core.security.mgt.SecurityManager;
import com.tmt.core.security.principal.Principal;
import com.tmt.core.security.subject.Subject;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;
import com.tmt.core.utils.time.DateUtils;

/**
 * 安全过滤器
 * 
 * @author lifeng
 */
public class SecurityFilter extends OncePerRequestFilter implements Ordered {

	// 访问日志记录
	private Logger ERROR_LOG = LoggerFactory.getLogger(SecurityFilter.class);
	private Logger ACCESS_LOG = LoggerFactory.getLogger("sys-access");
	private SecurityManager securityManager;
	private FilterChainResolver filterChainResolver;

	public SecurityFilter(SecurityManager securityManager, FilterChainResolver filterChainResolver) {
		this.securityManager = securityManager;
		this.filterChainResolver = filterChainResolver;
	}

	@Override
	public int getOrder() {
		return 0;
	}

	/**
	 * 具体的执行逻辑
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException {

		Throwable t = null;

		Subject subejct = null;

		long t1 = System.currentTimeMillis();
		try {

			subejct = securityManager.createSubject(request, response);
			subejct.execute(new Callable() {
				@Override
				public Object call() throws Exception {
					executeChain(request, response, filterChain);
					return null;
				}
			});

		} catch (Throwable throwable) {
			t = throwable;
		}

		// 记录访问日志
		try {
			this.accessLog(subejct, request, t1, t);
		} finally {

			// 提交 Session
			if (subejct != null && subejct.getSession() != null) {
				subejct.getSession().onCommit();
			}

			// 销毁临时数据
			if (subejct != null) {
				subejct.destory();
				subejct = null;
			}
		}

		if (t != null) {
			if (t instanceof ServletException) {
				throw (ServletException) t;
			}
			if (t instanceof IOException) {
				throw (IOException) t;
			}
			throw new ServletException("Filtered request failed.", t);
		}
	}

	/**
	 * 记录访问日志
	 * 
	 * @param request
	 * @param t1
	 */
	protected void accessLog(Subject subejct, HttpServletRequest request, long t1, Throwable ex) {

		// 监控和待办的不纳入记录
		String _url = request.getRequestURI();

		// 来源
		String referer = request.getHeader("referer");

		// springMvc 出现的错误会放入 request
		Throwable _ex = ex == null ? (Throwable) request.getAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE) : ex;

		// 用户
		Long createId = null;
		String createName = "";
		Principal principal = null;
		if (subejct != null && (principal = subejct.getPrincipal()) != null) {
			createId = principal.getId();
			createName = principal.getAccount();
		}

		// 记录下来
		ACCESS_LOG.info("{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}\t{}", IdGen.key(), (byte) (_ex == null ? 1 : 2),
				createId == null ? "\\N" : createId, StringUtils.defaultIfBlank(createName, "\\N"),
				DateUtils.getTimeStampNow(), WebUtils.getRemoteAddr(request),
				StringUtils.abbreviate(request.getHeader("user-agent"), 255), _url, request.getMethod(),
				(int) (System.currentTimeMillis() - t1), StringUtils.defaultIfBlank("", "\\N"),
				StringUtils.defaultIfBlank(
						StringUtils.isNoneBlank(referer) ? StringUtils.abbreviate(referer, 255) : null, "\\N"));

		// 记录错误
		if (_ex != null) {
			ERROR_LOG.error("服务器错误：{} - ", _url, _ex);
		}
	}

	/**
	 * 调用执行链
	 * 
	 * @param request
	 * @param response
	 * @param origChain
	 * @throws IOException
	 * @throws ServletException
	 */
	protected void executeChain(HttpServletRequest request, HttpServletResponse response, FilterChain origChain)
			throws IOException, ServletException {
		FilterChain chain = getExecutionChain(request, response, origChain);
		chain.doFilter(request, response);
	}

	/**
	 * 得到执行链
	 * 
	 * @param request
	 * @param response
	 * @param origChain
	 * @return
	 */
	protected FilterChain getExecutionChain(HttpServletRequest request, HttpServletResponse response,
			FilterChain origChain) {
		FilterChain chain = origChain;

		FilterChainResolver resolver = getFilterChainResolver();
		if (resolver == null) {
			return origChain;
		}

		FilterChain resolved = resolver.getChain(request, response, origChain);

		if (resolved != null) {
			chain = resolved;
		}

		return chain;
	}

	public FilterChainResolver getFilterChainResolver() {
		return filterChainResolver;
	}

	public void setFilterChainResolver(FilterChainResolver filterChainResolver) {
		this.filterChainResolver = filterChainResolver;
	}
}