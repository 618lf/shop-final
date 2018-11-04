package com.tmt.common.security.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class OncePerRequestFilter implements Filter, Nameable {

	public static final String ALREADY_FILTERED_SUFFIX = ".FILTERED";
	
	private boolean enabled = true;
	private String name;
	protected FilterConfig filterConfig;
	public FilterConfig getFilterConfig() {
        return filterConfig;
    }
	public void setFilterConfig(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
	}
	public boolean isEnabled() {
        return enabled;
    }
	public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
	protected String getName() {
        if (this.name == null) {
            FilterConfig config = getFilterConfig();
            if (config != null) {
                this.name = config.getFilterName();
            }
        }

        return this.name;
    }
	public void setName(String name) {
		this.name = name;
	}
	
	public void init(FilterConfig filterConfig) throws ServletException {
        setFilterConfig(filterConfig);
    }
	
	/**
	 * 基本的执行逻辑
	 */
	public final void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
        String alreadyFilteredAttributeName = getAlreadyFilteredAttributeName();
        if (request.getAttribute(alreadyFilteredAttributeName) != null ) {
            filterChain.doFilter(request, response);
        } else if (!isEnabled(httpRequest, httpResponse)) {
            filterChain.doFilter(request, response);
        } else {
            request.setAttribute(alreadyFilteredAttributeName, Boolean.TRUE);
            try {
                doFilterInternal(httpRequest, httpResponse, filterChain);
            } finally {
                request.removeAttribute(alreadyFilteredAttributeName);
            }
        }
    }
	
	protected boolean isEnabled(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return isEnabled();
    }
	
	protected String getAlreadyFilteredAttributeName() {
        String name = getName();
        if (name == null) {
            name = getClass().getName();
        }
        return name + ALREADY_FILTERED_SUFFIX;
    }
	
	public void destroy() {}
	
	/**
	 * 最终时执行的这里
	 * @param request
	 * @param response
	 * @param chain
	 * @throws ServletException
	 * @throws IOException
	 */
	protected abstract void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)  throws ServletException, IOException;
}
