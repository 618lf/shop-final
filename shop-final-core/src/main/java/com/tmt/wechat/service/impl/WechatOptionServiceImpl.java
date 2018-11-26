package com.tmt.wechat.service.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;

import com.tmt.common.config.Globals;
import com.tmt.common.http.LocalHttpClient;
import com.tmt.common.http.handler.JsonResponseHandler;
import com.tmt.common.lock.ResourceLock;
import com.tmt.common.persistence.incrementer.UUIdGenerator;
import com.tmt.common.utils.CacheUtils;
import com.tmt.common.utils.DateUtil3;
import com.tmt.common.utils.JsonMapper;
import com.tmt.common.utils.Maps;
import com.tmt.wechat.bean.base.BaseResult;
import com.tmt.wechat.bean.base.Constants.MediaType;
import com.tmt.wechat.bean.base.SnsToken;
import com.tmt.wechat.bean.base.Ticket;
import com.tmt.wechat.bean.base.Token;
import com.tmt.wechat.bean.base.WechatConfig;
import com.tmt.wechat.bean.base.WechatConstants;
import com.tmt.wechat.bean.massmsg.MassNews;
import com.tmt.wechat.bean.material.Material;
import com.tmt.wechat.bean.material.MaterialBatchGetResult;
import com.tmt.wechat.bean.material.MaterialCountResult;
import com.tmt.wechat.bean.material.MaterialImageUploadResult;
import com.tmt.wechat.bean.material.MaterialNewsBatchGetResult;
import com.tmt.wechat.bean.material.MaterialNewsResult;
import com.tmt.wechat.bean.material.MaterialNewsUpdate;
import com.tmt.wechat.bean.material.MaterialUploadResult;
import com.tmt.wechat.bean.material.MaterialVideoResult;
import com.tmt.wechat.bean.material.Media;
import com.tmt.wechat.bean.menu.MenuButtons;
import com.tmt.wechat.bean.message.MsgHead;
import com.tmt.wechat.bean.qrcode.QrCodeTicket;
import com.tmt.wechat.bean.template.TemplateMessageResult;
import com.tmt.wechat.bean.user.FollowResult;
import com.tmt.wechat.bean.user.UserInfo;
import com.tmt.wechat.exception.WechatErrorException;
import com.tmt.wechat.service.WechatOptionService;
import com.tmt.wechat.utils.SHA1;

public class WechatOptionServiceImpl implements WechatOptionService{

	// 线程栈
	private static final ThreadLocal<WechatConfig> RESOURCES = new InheritableThreadLocal<WechatConfig>();
	  
	//==========================================================
	//               运行绑定
	//==========================================================
		
	/**
	 * 绑定到执行栈
	 */
	@Override
	public WechatOptionService bind(WechatConfig config) {
		RESOURCES.set(config);
		return this;
	}
	
	/**
	 * 释放资源
	 */
	@Override
	public void unbind() {
		RESOURCES.remove();
	}
	
	
	//==========================================================
	//                接入校验
	//==========================================================
	
	/**
	 * 校验参数
	 */
	@Override
	public boolean checkSignature(String timestamp, String nonce, String signature) {
		try {
			WechatConfig app = RESOURCES.get();
			return SHA1.gen(app.getToken(), timestamp, nonce).equals(signature);
		}finally {
			this.unbind();
		}
	}
	
	//==========================================================
	//                消息处理
	//==========================================================

	@Override
	public MsgHead onMessage(String req) {
		return null;
	}

	//==========================================================
	//                授权登录
	//==========================================================
	
	@Override
	public String getAuthorizeURL(String domain, String url) {
		try {
			WechatConfig app = RESOURCES.get();
			String redirectUri = new StringBuilder("http://").append(domain).append(Globals.frontPath).append("/oauth/wechat/oAuth.rt").toString();
			try {
				if (url != null) {
					redirectUri = new StringBuilder(redirectUri).append("?to=").append(url).toString();
				}
				redirectUri = URLEncoder.encode(redirectUri, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			//授权的公众号
			StringBuilder _content = new StringBuilder("https://open.weixin.qq.com/connect/oauth2/authorize");
			_content.append("?appid=").append(app.getAppId())
	                .append("&redirect_uri=").append(redirectUri)
	                .append("&response_type=code")
	                .append("&scope=").append("snsapi_userinfo")
	                .append("&state=").append(domain)
	                .append("#wechat_redirect");
			return _content.toString();
		} finally {
			this.unbind();
		}
	}

	@Override
	public SnsToken oauth2AccessToken(String code, String state) {
		try {
			WechatConfig app = RESOURCES.get();
			HttpUriRequest httpUriRequest = RequestBuilder.get()
					.setUri(WechatConstants.BASE_URI + "/sns/oauth2/access_token")
					.addParameter("appid", app.getAppId())
					.addParameter("secret", app.getSecret())
					.addParameter("code", code)
					.addParameter("grant_type", "authorization_code")
					.build();
			SnsToken token = LocalHttpClient.execute(httpUriRequest,JsonResponseHandler.createResponseHandler(SnsToken.class));
			token.setAppId(app.getAppId());
			return token;
		} finally {
			this.unbind();
		}
	}

	@Override
	public UserInfo authUserinfo(String accessToken, String openId) {
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setUri(WechatConstants.BASE_URI + "/sns/userinfo")
				.addParameter("access_token", accessToken)
				.addParameter("openid", openId)
				.addParameter("lang", "zh_CN")
				.build();
		return LocalHttpClient.execute(httpUriRequest,JsonResponseHandler.createResponseHandler(UserInfo.class));
	}

	//==========================================================
	//                URL 签名
	//==========================================================
	
	protected Ticket getTicket() {
		WechatConfig app = RESOURCES.get();
		final String key = new StringBuilder(WechatConstants.PUB_ACCOUNT_JS_TICKET).append(app.getAppId()).toString();
		Ticket ticket = CacheUtils.get(key);
		if (ticket == null || ticket.isExpired()) {
			ticket = CacheUtils.get(key);
			if (ticket == null || ticket.isExpired()) {
				HttpUriRequest httpUriRequest = RequestBuilder.get()
						.setUri(WechatConstants.BASE_URI+"/cgi-bin/ticket/getticket")
						.addParameter("access_token", this.getToken().getAccess_token())
						.addParameter("type","jsapi")
						.build();
				ticket = LocalHttpClient.execute(httpUriRequest,JsonResponseHandler.createResponseHandler(Ticket.class));
				if (ticket != null && ticket.getExpires_in() != 0){
					ticket.setAddTime(DateUtil3.getTimeStampNow());
					CacheUtils.putWithLiveTime(key, ticket, ticket.getExpires_in());
				}
			} 
		}
		return ticket;
	}
	
	@Override
	public Map<String, String> getUrlSign(String url) {
		try {
			WechatConfig app = RESOURCES.get();
			Ticket ticket = this.getTicket();
			String noncestr = UUIdGenerator.uuid();
			String timestamp = Long.toString(System.currentTimeMillis() / 1000);
			StringBuilder signStr = new StringBuilder();
			signStr.append("jsapi_ticket=").append(ticket.getTicket()).append("&")
			       .append("noncestr=").append(noncestr).append("&")
			       .append("timestamp=").append(timestamp).append("&")
			       .append("url=").append(url);
			String signature = SHA1.genWithAmple(signStr.toString());
			
			// 返回数据
			Map<String, String> reMap = Maps.newHashMap();
			reMap.put("noncestr", noncestr);
			reMap.put("timestamp", timestamp);
			reMap.put("signature", signature.toLowerCase());
			reMap.put("url", url);
			reMap.put("appId", app.getAppId());
			return reMap;
		} finally {
			this.unbind();
		}
	}

	//==========================================================
	//                调用 API 的token
	//==========================================================
	
	private Token getToken() {
		final WechatConfig app = RESOURCES.get();
		final String key = new StringBuilder(WechatConstants.PUB_ACCOUNT_ACCESS_TOKEN).append(app.getAppId()).toString();
		Token token = CacheUtils.get(key);
        if (token == null || token.isExpired()) {
        	token = ResourceLock.getLock(WechatConstants.LOCK_FOR_ACCESS_TOKEN).doHandler(new com.tmt.common.lock.Handler() {
        		@Override
				@SuppressWarnings("unchecked")
				public Token doHandle() {
					Token _token = CacheUtils.get(key);
	        		if (_token == null || _token.isExpired()) {
	        			HttpUriRequest httpUriRequest = RequestBuilder.get()
	        					.setUri(WechatConstants.BASE_URI + "/cgi-bin/token")
	        					.addParameter("grant_type","client_credential")
	        					.addParameter("appid", app.getAppId())
	        					.addParameter("secret", app.getSecret())
	        					.build();
	        			_token = LocalHttpClient.execute(httpUriRequest,JsonResponseHandler.createResponseHandler(Token.class));
	        			if (_token != null && _token.getExpires_in() != 0){
	        				_token.setAddTime(DateUtil3.getTimeStampNow());
	        				CacheUtils.putWithLiveTime(key, _token, _token.getExpires_in());
	        			}
	        		}
	        		return _token;
				}
        	});
		}
        return token;
	}

	//==========================================================
	//               菜单服务
	//==========================================================
	
	@Override
	public boolean menuCreate(MenuButtons menuItem) throws WechatErrorException {
		try {
			String access_token = this.getToken().getAccess_token();
			HttpUriRequest httpUriRequest = RequestBuilder.post()
					.setHeader(WechatConstants.jsonHeader)
					.setUri(new StringBuilder(WechatConstants.BASE_URI).append("/cgi-bin/menu/create").toString())
					.addParameter("access_token", access_token)
					.setEntity(new StringEntity(JsonMapper.toJson(menuItem), Charset.forName("utf-8")))
					.build();
			BaseResult result = LocalHttpClient.execute(httpUriRequest,JsonResponseHandler.createResponseHandler(BaseResult.class));
			if ("ok".equals(result.getErrmsg())) {
				return true;
			}
			return false;
		}finally {
			this.unbind();
		}
	}

	//==========================================================
	//              二维码服务
	//==========================================================
	@Override
	public QrCodeTicket qrcodeCreateFinal(String scene_str) {
		try {
			String access_token = this.getToken().getAccess_token();
			
			// 构建参数
			Integer expire_seconds = null;
			String qrcodeJson = String.format("{"+(expire_seconds==null?"%1$s":"\"expire_seconds\": %1$s, ")+"\"action_name\": \"%2$s\", \"action_info\": {\"scene\": {\"scene_str\": \"%3$s\"}}}",
				   expire_seconds == null?"": expire_seconds, "QR_LIMIT_STR_SCENE", scene_str);
			HttpUriRequest httpUriRequest = RequestBuilder.post()
					.setHeader(WechatConstants.jsonHeader)
					.setUri(WechatConstants.BASE_URI+"/cgi-bin/qrcode/create")
					.addParameter("access_token", access_token)
					.setEntity(new StringEntity(qrcodeJson, Charset.forName("utf-8")))
					.build();
			return LocalHttpClient.execute(httpUriRequest,JsonResponseHandler.createResponseHandler(QrCodeTicket.class));
		}finally {
			this.unbind();
		}
	}
	
	@Override
	public String qrcodeShowUrl(String ticket) {
		try {
			// 构建参数
			String image = RequestBuilder.get().setUri(WechatConstants.QRCODE_DOWNLOAD_URI + "/cgi-bin/showqrcode").addParameter("ticket", ticket)
						.build().getURI().toString();
			return image;
		}finally {
			this.unbind();
		}
	}

	//==========================================================
	//              素材服务
	//==========================================================

	@Override
	public Media mediaUpload(MediaType mediaType, InputStream inputStream)
			throws WechatErrorException {
		
		// 临时文件
	    File temp = FileUtils.getFile(FileUtils.getTempDirectory(), UUIdGenerator.uuid() + ".jpg");
		try {
			FileUtils.writeByteArrayToFile(temp, IOUtils.toByteArray(inputStream));
			String access_token = this.getToken().getAccess_token();
			HttpPost httpPost = new HttpPost(WechatConstants.BASE_URI + "/cgi-bin/media/upload");
			FileBody bin = new FileBody(temp);
			HttpEntity httpEntity = MultipartEntityBuilder.create()
					.addPart("media", bin)
					.addTextBody("access_token", access_token)
					.addTextBody("type", mediaType.name())
					.build();
			httpPost.setEntity(httpEntity);
			return LocalHttpClient.execute(httpPost, JsonResponseHandler.createResponseHandler(Media.class));
		} catch (IOException e) {
			return null;
		}finally {
			temp.delete();
			this.unbind();
		}
	}

	@Override
	public File mediaDownload(String media_id) throws WechatErrorException {
		return null;
	}

	@Override
	public MaterialImageUploadResult materialImgUpload(File file)
			throws WechatErrorException {
		return null;
	}

	@Override
	public MaterialUploadResult materialNewsUpload(MassNews news)
			throws WechatErrorException {
		return null;
	}
	
	//==========================================================
	//              素材服务 -- 永久素材
	//==========================================================

	/**
	 * 上传除了图文消息之外的其他消息
	 */
	@Override
	public MaterialUploadResult materialUpload(MediaType mediaType,
			Material material) throws WechatErrorException {
		// 临时文件
	    File temp = FileUtils.getFile(FileUtils.getTempDirectory(), UUIdGenerator.uuid() + ".jpg");
		try {
			FileUtils.writeByteArrayToFile(temp, IOUtils.toByteArray(material.getUpload()));
			String access_token = this.getToken().getAccess_token();
			HttpPost httpPost = new HttpPost(WechatConstants.BASE_URI + "/cgi-bin/material/add_material");
			FileBody bin = new FileBody(temp);
			HttpEntity httpEntity = MultipartEntityBuilder.create()
					.addPart("media", bin)
					.addTextBody("access_token", access_token)
					.addTextBody("type", mediaType.name())
					.addTextBody("description", JsonMapper.toJson(material))
					.build();
			httpPost.setEntity(httpEntity);
			return LocalHttpClient.execute(httpPost, JsonResponseHandler.createResponseHandler(MaterialUploadResult.class));
		} catch (IOException e) {
			return null;
		}finally {
			temp.delete();
			this.unbind();
		}
	}

	@Override
	public InputStream materialImageOrVoiceDownload(String mediaId)
			throws WechatErrorException {
		return null;
	}

	@Override
	public MaterialVideoResult materialVideoInfo(String mediaId)
			throws WechatErrorException {
		return null;
	}

	@Override
	public MaterialNewsResult materialNewsInfo(String mediaId)
			throws WechatErrorException {
		return null;
	}

	@Override
	public boolean materialNewsUpdate(MaterialNewsUpdate news)
			throws WechatErrorException {
		return false;
	}

	@Override
	public boolean materialDelete(String mediaId) throws WechatErrorException {
		return false;
	}

	@Override
	public MaterialCountResult materialCount() throws WechatErrorException {
		return null;
	}

	@Override
	public MaterialNewsBatchGetResult materialNewsBatchGet(int offset, int count)
			throws WechatErrorException {
		return null;
	}

	@Override
	public MaterialBatchGetResult materialFileBatchGet(String type, int offset,
			int count) throws WechatErrorException {
		return null;
	}

	
	//==========================================================
	//              模板消息
	//==========================================================
	@Override
	public TemplateMessageResult sendTemplateMessage(String messageJson) {
		try {
			String access_token = this.getToken().getAccess_token();
			HttpUriRequest httpUriRequest = RequestBuilder.post()
					.setHeader(WechatConstants.jsonHeader)
					.setUri(WechatConstants.BASE_URI+"/cgi-bin/message/template/send")
					.addParameter("access_token", access_token)
					.setEntity(new StringEntity(messageJson,Charset.forName("utf-8")))
					.build();
			return LocalHttpClient.execute(httpUriRequest,JsonResponseHandler.createResponseHandler(TemplateMessageResult.class));
		}finally {
			this.unbind();
		}
	}

	
	//==========================================================
	//                调用 API 的token
	//==========================================================

	@Override
	public UserInfo userinfo(String openId) {
		try {
			String access_token = this.getToken().getAccess_token();
			
			HttpUriRequest httpUriRequest = RequestBuilder.post()
					.setUri(WechatConstants.BASE_URI + "/cgi-bin/user/info")
					.addParameter("access_token", access_token)
					.addParameter("openid", openId)
					.addParameter("lang", "zh_CN")
					.build();
			return LocalHttpClient.execute(httpUriRequest,JsonResponseHandler.createResponseHandler(UserInfo.class));
		}finally {
			this.unbind();
		}
	}

	@Override
	public FollowResult userPage(String next_openid) {
		try {
			String access_token = this.getToken().getAccess_token();
			
			HttpUriRequest httpUriRequest = RequestBuilder.post()
					.setUri(WechatConstants.BASE_URI + "/cgi-bin/user/get")
					.addParameter("access_token", access_token)
					.addParameter("openid", next_openid)
					.addParameter("lang", "zh_CN")
					.build();
			return LocalHttpClient.execute(httpUriRequest,JsonResponseHandler.createResponseHandler(FollowResult.class));
		}finally {
			this.unbind();
		}
	}
}
