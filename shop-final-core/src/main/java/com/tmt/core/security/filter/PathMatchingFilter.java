package com.tmt.core.security.filter;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmt.core.security.mgt.PatternMatcher;
import com.tmt.core.security.mgt.support.AntPathMatcher;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;

/**
 * 根据路径选择执行的filter
 * @author lifeng
 */
public class PathMatchingFilter extends AdviceFilter implements PathConfigProcessor{

	/**
	 * 配置信息
	 */
	protected Map<String, Object> appliedPaths = new LinkedHashMap<String, Object>();
	
	/**
	 * url 匹配器
	 */
	private PatternMatcher pathMatcher = new AntPathMatcher();
	
	/**
	 * 记录 请求设置中每个请求对应的filter
	 */
	@Override
	public Filter processPathConfig(String path, String config) {
		String[] values = null;
        if (config != null) {
            values = StringUtils.split(config);
        }
        this.appliedPaths.put(path, values);
        return this;
	}
	
	/**
	 * 将contextpath 加入到请求地址中
	 * @param request
	 * @return
	 */
	protected String getPathWithinApplication(HttpServletRequest request) {
        return WebUtils.getPathWithinApplication(request);
    }
	
	/**
	 * 验证请求地址是否能在配置中找到， 一定会有一个被找到
	 * @param path
	 * @param request
	 * @return
	 */
	protected boolean pathsMatch(String path, HttpServletRequest request) {
        String requestURI = getPathWithinApplication(request);
        return pathMatcher.matches(path, requestURI);
    }
	
	/**
	 * 找到需要执行的配置
	 */
	protected boolean preHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (this.appliedPaths == null || this.appliedPaths.isEmpty()) {
            return true;
        }
        for (String path : this.appliedPaths.keySet()) {
            if (pathsMatch(path, request)) {
                Object config = this.appliedPaths.get(path);
                return isFilterChainContinued(request, response, path, config);
            }
        }
        return true;
    }
	
	/**
	 * 找到配置之后继续执行
	 * @param request
	 * @param response
	 * @param path
	 * @param pathConfig
	 * @return
	 * @throws Exception
	 */
	private boolean isFilterChainContinued(HttpServletRequest request, HttpServletResponse response, String path, Object pathConfig) throws Exception {
		if (isEnabled(request, response)) {
			return onPreHandle(request, response, pathConfig);
		}
		return true;
	}
	
	/**
	 * 将访问控制的合并到这里
	 * @param request
	 * @param response
	 * @param mappedValue
	 * @return
	 * @throws Exception
	 */
	protected boolean onPreHandle(HttpServletRequest request, HttpServletResponse response, Object mappedValue) throws Exception {
		return true;
    }
	
}