package com.tmt.common.security.mgt;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface FilterChainResolver {

	FilterChain getChain(HttpServletRequest request, HttpServletResponse response,
			FilterChain originalChain);

	void setFilterChainManager(FilterChainManager filterChainManager);

	void setPatternMatcher(PatternMatcher patternMatcher);
}
