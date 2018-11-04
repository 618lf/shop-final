package com.tmt.common.security.mgt.support;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmt.common.security.mgt.FilterChainManager;
import com.tmt.common.security.mgt.FilterChainResolver;
import com.tmt.common.security.mgt.PatternMatcher;
import com.tmt.common.utils.WebUtils;

public class PathMatchingFilterChainResolver implements FilterChainResolver {

	private FilterChainManager filterChainManager;
	private PatternMatcher patternMatcher;
	
	public PathMatchingFilterChainResolver() {
		patternMatcher = new AntPathMatcher();
	}
	
	@Override
	public FilterChain getChain(HttpServletRequest request, HttpServletResponse response, FilterChain originalChain) {
		
		PatternMatcher pathMatcher = getPatternMatcher();
		FilterChainManager filterChainManager = getFilterChainManager();
        if (!filterChainManager.hasChains()) {
            return null;
        }
        
        String requestURI = getPathWithinApplication(request);
        
        for (String pathPattern : filterChainManager.getChainNames()) {
        	if (pathMatcher.matches(pathPattern, requestURI)) {
                return filterChainManager.proxy(originalChain, pathPattern);
            }
        }
		return null;
	}
	
	protected String getPathWithinApplication(HttpServletRequest request) {
        return WebUtils.getPathWithinApplication(request);
    }

	public FilterChainManager getFilterChainManager() {
		return filterChainManager;
	}

	public void setFilterChainManager(FilterChainManager filterChainManager) {
		this.filterChainManager = filterChainManager;
	}

	public PatternMatcher getPatternMatcher() {
		return patternMatcher;
	}

	public void setPatternMatcher(PatternMatcher patternMatcher) {
		this.patternMatcher = patternMatcher;
	}
}
