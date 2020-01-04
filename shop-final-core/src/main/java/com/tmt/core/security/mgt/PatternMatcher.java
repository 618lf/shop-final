package com.tmt.core.security.mgt;

public interface PatternMatcher {

	boolean matches(String pattern, String source);
}