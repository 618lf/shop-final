package com.tmt.core.security.mgt;

import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;

public interface FilterChainManager {

	 boolean hasChains();
	 
	 Set<String> getChainNames();
	 
	 FilterChain proxy(FilterChain original, String chainName);
	 
	 Map<String, Filter> getFilters();
	 
	 void addFilter(String name, Filter filter);
	 
	 void addFilter(String name, Filter filter, boolean init);
	 
	 void createChain(String chainName, String chainDefinition);
}
