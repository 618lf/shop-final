package com.tmt.wechat.bean.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.tmt.wechat.utils.MapUtil;
import com.tmt.wechat.utils.SignUtils;

/**
 * 支付配置
 * 
 * @author lifeng
 */
public class WechatPayConfig {

	private String appId;
	private String mchId;
	private String mchKey;
	private String notifyUrl;
	private String keyPath;
	private boolean useSandbox;
	private SSLContext sslContext;

	public boolean isUseSandbox() {
		return useSandbox;
	}

	public void setUseSandbox(boolean useSandbox) {
		this.useSandbox = useSandbox;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public void setKeyPath(String keyPath) {
		this.keyPath = keyPath;
	}

	public String getKeyPath() {
		return keyPath;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public void setMchKey(String mchKey) {
		this.mchKey = mchKey;
	}

	public String getMchId() {
		return this.mchId;
	}

	public String getMchKey() {
		return this.mchKey;
	}

	public String getAppId() {
		return this.appId;
	}

	public String getNotifyUrl() {
		return this.notifyUrl;
	}

	public SSLContext getSslContext() {
		return sslContext;
	}

	public void setSslContext(SSLContext sslContext) {
		this.sslContext = sslContext;
	}
	
	/**
     * HTTP(S) 连接超时时间，单位毫秒
     *
     * @return
     */
    public int getHttpConnectTimeoutMs() {
        return 6*1000;
    }

    /**
     * HTTP(S) 读数据超时时间，单位毫秒
     *
     * @return
     */
    public int getHttpReadTimeoutMs() {
        return 8*1000;
    }

	public SSLContext initSSLContext() {
		if (null == mchId) {
			throw new IllegalArgumentException("请确保商户号mch_id已设置");
		}

		File file = new File(this.keyPath);
		if (!file.exists()) {
			throw new RuntimeException("证书文件：【" + file.getPath() + "】不存在！");
		}

		try {
			FileInputStream inputStream = new FileInputStream(file);
			KeyStore keystore = KeyStore.getInstance("PKCS12");
			char[] partnerId2charArray = mchId.toCharArray();
			keystore.load(inputStream, partnerId2charArray);
			this.sslContext = SSLContexts.custom().loadKeyMaterial(keystore, partnerId2charArray).build();
			return this.sslContext;
		} catch (Exception e) {
			throw new RuntimeException("证书文件有问题，请核实！", e);
		}
	}
	
	/**
	 * 发起请求的方法。与实际的配置绑定在一起
	 * @param domain
	 * @param url
	 * @param data
	 * @param connectTimeoutMs
	 * @param readTimeoutMs
	 * @param useCert
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public String doRequest(final String domain, String urlSuffix, String data, int connectTimeoutMs, int readTimeoutMs, boolean useCert) throws ClientProtocolException, IOException {
		CloseableHttpClient httpClient = null;
		try {
			if (useCert) {
				if (this.sslContext == null) {
					this.initSSLContext();
				}
				SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
	                    sslContext, new String[]{"TLSv1"},  null,  new DefaultHostnameVerifier());
	
				BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager(
	                    RegistryBuilder.<ConnectionSocketFactory>create()
	                            .register("http", PlainConnectionSocketFactory.getSocketFactory())
	                            .register("https", sslConnectionSocketFactory)
	                            .build(), null,  null, null);
				httpClient = HttpClientBuilder.create().setConnectionManager(connManager)
		                .build();
			} else {
				httpClient = HttpClientBuilder.create().build();
			}
			
			String url = new StringBuilder("https://").append(domain).append(urlSuffix).toString();
	        HttpPost httpPost = new HttpPost(url);
	        
	        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeoutMs).setConnectTimeout(connectTimeoutMs).build();
	        httpPost.setConfig(requestConfig);
	        
	        StringEntity postEntity = new StringEntity(data, "UTF-8");
	        httpPost.addHeader("Content-Type", "text/xml");
	        httpPost.addHeader("User-Agent", "wxpay sdk java v1.0 " + this.getMchId());
	        httpPost.setEntity(postEntity);
			
	        HttpResponse httpResponse = httpClient.execute(httpPost);
	        HttpEntity httpEntity = httpResponse.getEntity();
	        return EntityUtils.toString(httpEntity, "UTF-8");
		} finally {
			IOUtils.closeQuietly(httpClient);
		}
	}
	
	/**
	 * 处理请求
	 * @param xml
	 * @return
	 * @throws Exception 
	 */
	public Map<String, String> processResponse(String xml, String signType, String key) throws Exception {
		Map<String, String> respData = MapUtil.xmlToMap(xml);
		if (!WechatConstants.SUCCESS.equals(respData.get("return_code"))) {
			return respData;
		}
		String sign = respData.get(WechatConstants.FIELD_SIGN);
		if (SignUtils.generateSign(respData, signType, key).equals(sign)) {
			return respData;
		}
		throw new Exception(String.format("Invalid sign value in XML: %s", xml));
	}
}