package com.tmt.common.http.handler;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import com.tmt.common.utils.JaxbMapper;
import com.tmt.common.utils.StringUtil3;

/**
 * XML 处理
 * @author lifeng
 *
 */
public class XmlResponseHandler{

	/**
	 * 转为默认的字符集 utf-8
	 * @param clazz
	 * @param encoding
	 * @return
	 */
	public static <T> ResponseHandler<T> createResponseHandler(final Class<T> clazz){
		return XmlResponseHandler.createResponseHandler(clazz, "utf-8");
	}
	
	/**
	 * 转为指定的字符集
	 * @param clazz
	 * @param encoding
	 * @return
	 */
	public static <T> ResponseHandler<T> createResponseHandler(final Class<T> clazz, final String encoding){
		return new ResponseHandler<T>() {
			@Override
			public T handleResponse(HttpResponse response)
					throws ClientProtocolException, IOException {
				int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    String str = EntityUtils.toString(entity);
            		// encoding == null 则不编码
                    String xml = encoding == null?str:(new String(str.getBytes("iso-8859-1"), encoding));
                    return StringUtil3.isNotBlank(str)?JaxbMapper.fromXml(xml, clazz):null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }

			}
		};
	}
}