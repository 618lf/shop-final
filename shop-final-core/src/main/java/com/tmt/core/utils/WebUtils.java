package com.tmt.core.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.tmt.Constants;
import com.tmt.core.entity.BaseEntity;
import com.tmt.core.exception.BaseRuntimeException;
import com.tmt.core.security.utils.RedirectView;
import com.tmt.core.web.security.cookie.CookieProvider;
import com.tmt.core.web.useragent.DeviceType;
import com.tmt.core.web.useragent.UserAgent;

/**
 * web 资源操作
 * 
 * @author lifeng
 */
public class WebUtils extends org.springframework.web.util.WebUtils {

	private static String SAVED_REQUEST_KEY = "_srk";

	/**
	 * Constant representing the HTTP 'GET' request method, equal to
	 * <code>GET</code>.
	 */
	public static final String GET_METHOD = "GET";

	/**
	 * Constant representing the HTTP 'POST' request method, equal to
	 * <code>POST</code>.
	 */
	public static final String POST_METHOD = "POST";

	/**
	 * Standard Servlet 2.3+ spec request attributes for include URI and paths.
	 * <p>
	 * If included via a RequestDispatcher, the current resource will see the
	 * originating request. Its own URI and paths are exposed as request attributes.
	 */
	public static final String INCLUDE_REQUEST_URI_ATTRIBUTE = "javax.servlet.include.request_uri";
	public static final String INCLUDE_CONTEXT_PATH_ATTRIBUTE = "javax.servlet.include.context_path";
	public static final String INCLUDE_SERVLET_PATH_ATTRIBUTE = "javax.servlet.include.servlet_path";
	public static final String INCLUDE_PATH_INFO_ATTRIBUTE = "javax.servlet.include.path_info";
	public static final String INCLUDE_QUERY_STRING_ATTRIBUTE = "javax.servlet.include.query_string";

	/**
	 * 简单的获取表格中的数据,约定大于配置，表格的参数名称已items.作为前缀
	 */
	public static final String DEFAULT_ITEMS_PARAM = "items.";

	/**
	 * 导出文件时提交的参数前缀
	 */
	public static final String DEFAULT_EXPORT_PARAM = "export.";

	/**
	 * 重定向
	 */
	public static final String REDIRECT_URL_PREFIX = "redirect:";

	/**
	 * 转发
	 */
	public static final String FORWARD_URL_PREFIX = "forward:";

	/**
	 * 重定向到一个URL
	 * 
	 * @param url
	 * @return
	 */
	public static String redirectTo(String url) {
		return new StringBuilder(REDIRECT_URL_PREFIX).append(url).toString();
	}

	/**
	 * 重定向到多个部分组成的URL
	 * 
	 * @param url
	 * @return
	 */
	public static String redirectTo(String... parts) {
		StringBuilder url = new StringBuilder(REDIRECT_URL_PREFIX);
		for (String part : parts) {
			url.append(part);
		}
		return url.toString();
	}

	/**
	 * 转发到一个地址
	 * 
	 * @param url
	 * @return
	 */
	public static String forwardTo(String url) {
		return new StringBuilder(FORWARD_URL_PREFIX).append(url).toString();
	}

	/**
	 * 是否重定向
	 * 
	 * @param url
	 * @return
	 */
	public static Boolean isRedirect(String url) {
		return StringUtils.startsWith(url, REDIRECT_URL_PREFIX);
	}

	/**
	 * 是否转发
	 * 
	 * @param url
	 * @return
	 */
	public static Boolean isforward(String url) {
		return StringUtils.startsWith(url, FORWARD_URL_PREFIX);
	}

	/**
	 * 是否是ajax 提交
	 * 
	 * @param request
	 * @return
	 */
	public static Boolean isAjax(HttpServletRequest request) {
		String str2 = request.getHeader("X-Requested-With");// Ajax 处理
		if ((str2 != null) && (str2.equalsIgnoreCase("XMLHttpRequest"))) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 是否post提交
	 * 
	 * @param request
	 * @return
	 */
	public static Boolean isPost(HttpServletRequest request) {
		return request.getMethod().equalsIgnoreCase(POST_METHOD);
	}

	/**
	 * 是否是手机访问
	 * 
	 * @param request
	 * @return
	 */
	public static Boolean isMobile(HttpServletRequest request) {
		if (request == null) {
			request = ContextHolderUtils.getRequest();
		}
		UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));
		return DeviceType.MOBILE.equals(userAgent.getOperatingSystem().getDeviceType());
	}

	/**
	 * 是否是微信浏览器的请求 内置浏览器 (微信PC客户端)
	 * 
	 * @param request
	 * @return
	 */
	public static Boolean isWeixinPc(HttpServletRequest request) {
		if (request == null) {
			request = ContextHolderUtils.getRequest();
		}
		String userAgent = request.getHeader("User-Agent");
		if (StringUtils.isNotBlank(userAgent) && userAgent.indexOf("MicroMessenger") != -1
				&& userAgent.indexOf("WindowsWechat") != -1) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 是否是微信浏览器的请求 内置浏览器
	 * 
	 * @param request
	 * @return
	 */
	public static Boolean isWeixin(HttpServletRequest request) {
		if (request == null) {
			request = ContextHolderUtils.getRequest();
		}
		String userAgent = request.getHeader("User-Agent");
		return isWeixinByAgent(userAgent);
	}

	/**
	 * 是否是微信的浏览器
	 * 
	 * @param userAgent
	 * @return
	 */
	public static Boolean isWeixinByAgent(String userAgent) {
		if (StringUtils.isNotBlank(userAgent) && userAgent.indexOf("MicroMessenger") != -1) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 是否QQ内置的浏览器的请求
	 * 
	 * @param request
	 * @return
	 */
	public static Boolean isQQ(HttpServletRequest request) {
		if (request == null) {
			request = ContextHolderUtils.getRequest();
		}
		String userAgent = request.getHeader("User-Agent");
		return isQQ(userAgent);
	}

	/**
	 * 是否是微信的浏览器
	 * 
	 * @param userAgent
	 * @return
	 */
	public static Boolean isQQ(String userAgent) {
		if (StringUtils.isNotBlank(userAgent) && (userAgent.indexOf("QQ/") != -1 || userAgent.indexOf("QQ-") != -1)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 输出json 数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public static void sendXml(HttpServletResponse response, String xml) {
		response.setContentType("text/xml;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.write(xml);
			out.flush();
		} catch (IOException e) {
			throw new BaseRuntimeException("发送xml数据异常", e);
		}
	}

	/**
	 * 输出json 数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public static void sendJson(HttpServletResponse response, String json) {
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.println(json);
			out.flush();
		} catch (IOException e) {
			throw new BaseRuntimeException("发送json数据异常", e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * 将状态码设置为 500 让异常信息能打印出来
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public static void sendJson(HttpServletResponse response, int status, String json) {
		response.setContentType("application/json; charset=UTF-8");
		response.setStatus(status);
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.println(json);
			out.flush();
		} catch (IOException e) {
			throw new BaseRuntimeException("发送json数据异常", e);
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * sendError -- 会查找 web.xml 中错误页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public static void sendError(HttpServletResponse response, String msg) {
		try {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, msg);
		} catch (IOException e) {
			throw new BaseRuntimeException("返回错误页面出现异常", e);
		}
	}

	/**
	 * 获得用户远程地址
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		if (request == null) {
			request = ContextHolderUtils.getRequest();
		}
		String remoteAddr = request.getHeader("X-Real-IP");
		if (StringUtils.isBlank(remoteAddr)) {
			remoteAddr = request.getHeader("X-Forwarded-For");
		} else if (StringUtils.isBlank(remoteAddr)) {
			remoteAddr = request.getHeader("Proxy-Client-IP");
		} else if (StringUtils.isBlank(remoteAddr)) {
			remoteAddr = request.getHeader("WL-Proxy-Client-IP");
		}
		return remoteAddr != null ? remoteAddr : request.getRemoteAddr();
	}

	/**
	 * 上传文件 -- 工具类
	 * 
	 * @param request
	 * @return
	 */
	public static MultipartFile[] uploadFile(HttpServletRequest request) {
		List<MultipartFile> files = Lists.newArrayList();
		// 自己解析上传文件，
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
				XSpringContextHolder.getServletContext());
		if (multipartResolver.isMultipart(request)) { // 判断request是否有文件上传
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
			Iterator<String> ite = multiRequest.getFileNames();
			while (ite.hasNext()) {
				MultipartFile file = multiRequest.getFile(ite.next());
				files.add(file);
			}
		}
		return files.toArray(new MultipartFile[files.size()]);
	}

	/**
	 * 上传文件 -- 工具类
	 * 
	 * @param request
	 * @return
	 */
	public static MultipartFile uploadSingleFile(HttpServletRequest request) {
		MultipartFile[] files = uploadFile(request);
		return files != null && files.length != 0 ? files[0] : null;
	}

	/**
	 * 得到request 中的数据，并清除两端的空格
	 * 
	 * @param request
	 * @param paramName
	 * @return
	 */
	public static String getCleanParam(ServletRequest request, String paramName) {
		request = request == null ? WebUtils.getRequest() : request;
		return StringUtils.trimToNull(request.getParameter(paramName));
	}

	/**
	 * 得到request 中的数据，并清除两端的空格
	 * 
	 * @param paramName
	 * @return
	 */
	public static String getCleanParam(String paramName) {
		return WebUtils.getCleanParam(null, paramName);
	}

	/**
	 * 得到请求的地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getRequestUri(HttpServletRequest request) {
		String uri = (String) request.getAttribute(INCLUDE_REQUEST_URI_ATTRIBUTE);
		if (uri == null) {
			uri = request.getRequestURI();
		}
		return normalize(decodeAndCleanUriString(request, uri));
	}

	/**
	 * 获取请求
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		return ContextHolderUtils.getRequest();
	}

	/**
	 * 获取表格数据
	 * 
	 * @param request
	 * @param clazz
	 *            表格对应的实体类
	 * @param paramPrefix
	 *            --参数前缀 例如:items.id,items.name
	 * @return
	 */
	public static <T> List<T> fetchItemsFromRequest(HttpServletRequest request, Class<T> clazz, String paramPrefix) {
		List<Map<String, String>> relas = fetchItemsFromRequest(request, paramPrefix);
		// 转换
		List<T> items = JsonMapper.fromJsonToList(JsonMapper.toJson(relas), clazz);
		return items;
	}

	/**
	 * 获取参数
	 * 
	 * @param request
	 * @param paramPrefix
	 * @return
	 */
	public static List<Map<String, String>> fetchItemsFromRequest(HttpServletRequest request, String paramPrefix) {
		request = request == null ? ContextHolderUtils.getRequest() : request;
		String itemsParam = (paramPrefix == null ? DEFAULT_ITEMS_PARAM : paramPrefix);
		Map<String, String[]> params = Maps.newOrderMap();
		for (Object param : request.getParameterMap().keySet()) {
			String key = String.valueOf(param);
			if (key != null && StringUtils.startsWith(key, itemsParam)) {
				params.put(StringUtils.substringAfter(key, itemsParam), request.getParameterValues(key));
			}
		}
		List<Map<String, String>> relas = Lists.newArrayList();
		if (params != null && !params.isEmpty()) {
			String[] ids = params.entrySet().iterator().next().getValue();// 取第一个值
			for (int i = 0, j = ids.length; i < j; i++) {
				Map<String, String> rela = Maps.newHashMap();
				for (String key : params.keySet()) {
					String[] values = params.get(key);
					if (values == null || values.length <= i) {
						continue;
					}
					rela.put(key, values[i]);
				}
				relas.add(rela);
			}
		}
		return relas;
	}

	/**
	 * Decode the given source string with a URLDecoder. The encoding will be taken
	 * from the request, falling back to the default "ISO-8859-1".
	 * <p>
	 * The default implementation uses <code>URLDecoder.decode(input, enc)</code>.
	 *
	 * @param request
	 *            current HTTP request
	 * @param source
	 *            the String to decode
	 * @return the decoded String
	 * @see #DEFAULT_CHARACTER_ENCODING
	 * @see javax.servlet.ServletRequest#getCharacterEncoding
	 * @see java.net.URLDecoder#decode(String, String)
	 * @see java.net.URLDecoder#decode(String)
	 */
	@SuppressWarnings({ "deprecation" })
	public static String decodeRequestString(HttpServletRequest request, String source) {
		String enc = determineEncoding(request);
		try {
			return URLDecoder.decode(source, enc);
		} catch (UnsupportedEncodingException ex) {
			return URLDecoder.decode(source);
		}
	}

	/**
	 * Decode the supplied URI string and strips any extraneous portion after a ';'.
	 *
	 * @param request
	 *            the incoming HttpServletRequest
	 * @param uri
	 *            the application's URI string
	 * @return the supplied URI string stripped of any extraneous portion after a
	 *         ';'.
	 */
	private static String decodeAndCleanUriString(HttpServletRequest request, String uri) {
		uri = decodeRequestString(request, uri);
		int semicolonIndex = uri.indexOf(';');
		return (semicolonIndex != -1 ? uri.substring(0, semicolonIndex) : uri);
	}

	/**
	 * Determine the encoding for the given request. Can be overridden in
	 * subclasses.
	 * <p>
	 * The default implementation checks the request's
	 * {@link ServletRequest#getCharacterEncoding() character encoding}, and if that
	 * <code>null</code>, falls back to the {@link #DEFAULT_CHARACTER_ENCODING}.
	 *
	 * @param request
	 *            current HTTP request
	 * @return the encoding for the request (never <code>null</code>)
	 * @see javax.servlet.ServletRequest#getCharacterEncoding()
	 */
	protected static String determineEncoding(HttpServletRequest request) {
		String enc = request.getCharacterEncoding();
		if (enc == null) {
			enc = DEFAULT_CHARACTER_ENCODING;
		}
		return enc;
	}

	/**
	 * Normalize a relative URI path that may have relative values ("/./", "/../",
	 * and so on ) it it. <strong>WARNING</strong> - This method is useful only for
	 * normalizing application-generated paths. It does not try to perform security
	 * checks for malicious input. Normalize operations were was happily taken from
	 * org.apache.catalina.util.RequestUtil in Tomcat trunk, r939305
	 *
	 * @param path
	 *            Relative path to be normalized
	 * @return normalized path
	 */
	public static String normalize(String path) {
		return normalize(path, true);
	}

	/**
	 * Normalize a relative URI path that may have relative values ("/./", "/../",
	 * and so on ) it it. <strong>WARNING</strong> - This method is useful only for
	 * normalizing application-generated paths. It does not try to perform security
	 * checks for malicious input. Normalize operations were was happily taken from
	 * org.apache.catalina.util.RequestUtil in Tomcat trunk, r939305
	 *
	 * @param path
	 *            Relative path to be normalized
	 * @param replaceBackSlash
	 *            Should '\\' be replaced with '/'
	 * @return normalized path
	 */
	private static String normalize(String path, boolean replaceBackSlash) {

		if (path == null)
			return null;

		// Create a place for the normalized path
		String normalized = path;

		if (replaceBackSlash && normalized.indexOf('\\') >= 0)
			normalized = normalized.replace('\\', '/');

		if (normalized.equals("/."))
			return "/";

		// Add a leading "/" if necessary
		if (!normalized.startsWith("/"))
			normalized = "/" + normalized;

		// Resolve occurrences of "//" in the normalized path
		while (true) {
			int index = normalized.indexOf("//");
			if (index < 0)
				break;
			normalized = normalized.substring(0, index) + normalized.substring(index + 1);
		}

		// Resolve occurrences of "/./" in the normalized path
		while (true) {
			int index = normalized.indexOf("/./");
			if (index < 0)
				break;
			normalized = normalized.substring(0, index) + normalized.substring(index + 2);
		}

		// Resolve occurrences of "/../" in the normalized path
		while (true) {
			int index = normalized.indexOf("/../");
			if (index < 0)
				break;
			if (index == 0)
				return (null); // Trying to go outside our context
			int index2 = normalized.lastIndexOf('/', index - 1);
			normalized = normalized.substring(0, index2) + normalized.substring(index + 3);
		}

		// Return the normalized path that we have completed
		return (normalized);
	}

	// 通过request.getParameter() 获取的参数没有经过 Xss 过滤，特制作一组获取 request 参数
	/**
	 * 获取单个参数
	 * 
	 * @param name
	 * @return
	 */
	public static String getSafeParameter(String name) {
		String value = WebUtils.getCleanParam(name);
		if (StringUtils.isNotBlank(value)) {
			return StringUtils.removeScript(value);
		}
		return value;
	}

	/**
	 * 获取整个Map
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public static Map getSafeParameterMap() {
		HttpServletRequest request = WebUtils.getRequest();
		Iterator<String[]> localIterator = request.getParameterMap().values().iterator();
		while (localIterator.hasNext()) {
			String[] arrayOfString = localIterator.next();
			for (int i = 0; i < arrayOfString.length; i++) {
				arrayOfString[i] = StringUtils.removeScript(arrayOfString[i]);
			}
		}
		return request.getParameterMap();
	}

	/**
	 * 获得编码后的参数集合
	 * 
	 * @param name
	 * @return
	 */
	public static String[] getSafeParameterValues(String name) {
		HttpServletRequest request = WebUtils.getRequest();
		String[] values = request.getParameterValues(name);
		if (values == null) {
			return null;
		}
		for (int i = 0; i < values.length; i++) {
			values[i] = StringUtils.removeScript(values[i]);
		}
		return values;
	}

	/**
	 * 重定向到保存的路径上
	 * 
	 * @param request
	 * @param response
	 * @param fallbackUrl
	 */
	public static void redirectToSavedRequest(HttpServletRequest request, HttpServletResponse response,
			String fallbackUrl) throws IOException {
		String successUrl = null;
		boolean contextRelative = true;
		SavedRequestEx savedRequest = CookieProvider.getAndClearAttribute(request, response, SAVED_REQUEST_KEY);
		if (savedRequest != null && savedRequest.getMethod().equalsIgnoreCase(GET_METHOD)) {
			successUrl = savedRequest.getRequestUrl();
			contextRelative = false;
		}

		if (successUrl == null) {
			successUrl = fallbackUrl;
		}

		if (successUrl == null) {
			throw new IllegalStateException("Success URL not available via saved request or via the "
					+ "successUrlFallback method parameter. One of these must be non-null for "
					+ "issueSuccessRedirect() to work.");
		}

		WebUtils.issueRedirect(request, response, successUrl, null, contextRelative);
	}

	/**
	 * 得到跳转的Url
	 * 
	 * @param request
	 * @param fallbackUrl
	 * @return
	 */
	public static String getAndClearSavedRequest(HttpServletRequest request, HttpServletResponse response,
			String fallbackUrl) {
		String successUrl = null;
		if (request == null) {
			request = ContextHolderUtils.getRequest();
		}
		SavedRequestEx savedRequest = CookieProvider.getAndClearAttribute(request, response, SAVED_REQUEST_KEY);
		if (savedRequest != null && savedRequest.getMethod().equalsIgnoreCase(GET_METHOD)) {
			successUrl = savedRequest.getRequestUrl();
		}
		if (successUrl == null) {
			successUrl = fallbackUrl;
		}
		return successUrl;
	}

	/**
	 * 存储一个地址（以get方式存储）
	 * 
	 * @param requestUrl
	 */
	public static void savedRequestUrl(HttpServletResponse response, String requestUrl) {
		if (StringUtils.isNotBlank(requestUrl)) {
			SavedRequestEx savedRequest = new SavedRequestEx(requestUrl);
			CookieProvider.setAttribute(null, response, SAVED_REQUEST_KEY, savedRequest);
		}
	}

	/**
	 * 存储请求
	 * 
	 * @param request
	 */
	public static void saveRequest(HttpServletRequest request, HttpServletResponse response) {
		SavedRequestEx savedRequest = new SavedRequestEx(request);
		CookieProvider.setAttribute(request, response, SAVED_REQUEST_KEY, savedRequest);
	}

	/**
	 * 重定向到指定的地址
	 * 
	 * @param request
	 * @param response
	 * @param url
	 * @throws IOException
	 */
	public static void issueRedirect(HttpServletRequest request, HttpServletResponse response, String url)
			throws IOException {
		issueRedirect(request, response, url, null, true);
	}

	/**
	 * 重定向到指定的地址
	 * 
	 * @param request
	 * @param response
	 * @param url
	 * @throws IOException
	 */
	public static void issueRedirect(HttpServletRequest request, HttpServletResponse response, String url,
			Map<String, ?> queryParams, boolean contextRelative) throws IOException {
		RedirectView view = new RedirectView(url, contextRelative, true);
		view.renderMergedOutputModel(queryParams, request, response);
	}

	/**
	 * 添加协议
	 * 
	 * @param url
	 * @return
	 */
	public static String preAppendScheme(String scheme, String url) {
		if (StringUtils.startsWith(url, "http") || StringUtils.startsWith(url, "https")) {
			return url;
		}
		return new StringBuilder(scheme).append(url).toString();
	}

	/**
	 * 不包括 contextPath
	 * 
	 * @param request
	 * @return
	 */
	public static String getPathWithinApplication(HttpServletRequest request) {
		String contextPath = getContextPath(request);
		String requestUri = getRequestUri(request);
		if (StringUtils.startsWithIgnoreCase(requestUri, contextPath)) {
			String path = requestUri.substring(contextPath.length());
			return (StringUtils.isNotBlank(path) ? path : "/");
		} else {
			return requestUri;
		}
	}

	/**
	 * 返回 contextPath
	 * 
	 * @param request
	 * @return
	 */
	public static String getContextPath(HttpServletRequest request) {
		String contextPath = (String) request.getAttribute(INCLUDE_CONTEXT_PATH_ATTRIBUTE);
		if (contextPath == null) {
			contextPath = request.getContextPath();
		}
		if ("/".equals(contextPath)) {
			contextPath = "";
		}
		return contextPath;
	}

	/**
	 * 标记为新用户
	 */
	public static void makerNewUser(HttpServletResponse response) {
		CookieUtils.setCookie(response, Constants.NEW_USER, String.valueOf(BaseEntity.YES), -1);
	}

	/**
	 * 删除新用户标识
	 */
	public static void removeNewUser(HttpServletRequest request, HttpServletResponse response) {
		CookieUtils.remove(request, response, Constants.NEW_USER);
	}

	/**
	 * 是否新用户
	 * 
	 * @return
	 */
	public static Boolean isNewUser(HttpServletRequest request) {
		String nu = CookieUtils.getCookie(request, Constants.NEW_USER);
		if (nu != null && String.valueOf(BaseEntity.YES).equals(nu)) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
}