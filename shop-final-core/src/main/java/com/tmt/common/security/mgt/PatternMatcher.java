package com.tmt.common.security.mgt;

public interface PatternMatcher {

	boolean matches(String pattern, String source);
}