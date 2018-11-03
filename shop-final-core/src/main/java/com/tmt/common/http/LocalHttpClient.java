package com.tmt.common.http;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.tmt.common.exception.BaseRuntimeException;
import com.tmt.common.utils.Maps;

public class LocalHttpClient {
	
	/**
	 * 获得HttpClient
	 * @return
	 */
	private static CloseableHttpClient getHttpClient() {  
        return HttpClients.custom().build();  
    } 
	
	/**
	 * 返回 RequestBuilder
	 * @param url
	 * @return
	 */
	public static RequestBuilder post(String url) {
		return RequestBuilder.post(url);
	}
	
	/**
	 * 返回 RequestBuilder
	 * @param url
	 * @return
	 */
	public static RequestBuilder get(String url) {
		return RequestBuilder.get(url);
	}
	
	/**
	 * 设置 header
	 * @param b
	 * @param headers
	 * @return
	 */
    public static RequestBuilder headers(RequestBuilder b, Map<String, String> headers) {
    	Iterator<String> it = headers.keySet().iterator();
    	while(it.hasNext()) {
    		String key = it.next();
    		String v = headers.get(key);
    		b.addHeader(key, v);
    	}
    	return b;
    }
    
    /**
     * 默认的代理
     */
    public static RequestBuilder proxy(RequestBuilder b, String host) {
    	Map<String, String> headers = Maps.newHashMap();
    	headers.put("Host", host);
		headers.put("Connection", "keep-alive");
		headers.put("Referer", "http://" + host);
		headers.put("Cache-Control", "max-age=0");
		headers.put("User-Agent","Mozilla/5.0 (Windows NT 5.1) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/14.0.835.186 Safari/535.1");
		headers.put("Content-Type", "application/x-www-form-urlencoded");
		headers.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		headers.put("Accept-Language", "zh-CN,zh;q=0.8");
		headers.put("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
		return headers(b, headers);
    }
	
	/**
	 * 返回格式化的内容
	 * @param request
	 * @param responseHandler
	 * @return
	 */
	public static <T> T execute(HttpUriRequest request, ResponseHandler<T> responseHandler){
		CloseableHttpClient httpClient = LocalHttpClient.getHttpClient();
		try {
			return httpClient.execute(request, responseHandler);
		} catch (Exception e) {
			throw new BaseRuntimeException(e);
		} finally {
			IOUtils.closeQuietly(httpClient);
		}
	}
	
	/**
	 * 简单的访问，不获取内容
	 * @param url
	 * @return
	 */
	public static int touch(HttpUriRequest request) {
		CloseableHttpClient httpClient = LocalHttpClient.getHttpClient();
		try {
			HttpResponse httpResponse = httpClient.execute(request);
			return httpResponse != null ? httpResponse.getStatusLine().getStatusCode() : null;
		} catch (Exception e) {
			return -1;
		} finally {
			IOUtils.closeQuietly(httpClient);
		}
	}
	
	/**
	 * 简单的get请求
	 * @param url
	 * @return
	 */
	public static String execute(HttpUriRequest request, Charset charset) {
		charset = charset == null? Consts.UTF_8: charset;
		CloseableHttpClient httpClient = LocalHttpClient.getHttpClient();
		try {
			HttpResponse httpResponse = httpClient.execute(request);
			return httpResponse != null ? EntityUtils.toString(httpResponse.getEntity(), charset): null;
		} catch (Exception e) {
			throw new BaseRuntimeException(e);
		} finally {
			IOUtils.closeQuietly(httpClient);
		}
	}
	
	/**
	 * 简单的get请求
	 * @param url
	 * @return
	 */
	public static byte[] execute(HttpUriRequest request) {
		CloseableHttpClient httpClient = LocalHttpClient.getHttpClient();
		try {
			HttpResponse httpResponse = httpClient.execute(request);
			return httpResponse != null ? EntityUtils.toByteArray(httpResponse.getEntity()): null;
		} catch (Exception e) {
			throw new BaseRuntimeException(e);
		} finally {
			IOUtils.closeQuietly(httpClient);
		}
	}
}