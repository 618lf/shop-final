package com.tmt.wechat.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tmt.core.entity.BaseEntity;
import com.tmt.core.utils.Maps;
import com.tmt.core.utils.StringUtils;
import com.tmt.system.entity.User;
import com.tmt.system.entity.UserAccount;
import com.tmt.system.entity.UserWechat;
import com.tmt.system.web.OAuthLoginController;
import com.tmt.wechat.bean.base.SnsToken;
import com.tmt.wechat.bean.user.UserInfo;
import com.tmt.wechat.entity.App;
import com.tmt.wechat.service.WechatService;
import com.tmt.wechat.utils.WechatUtils;

/**
 * 微信公众号授权
 * 需要根据绑定的域名返回不同的公众号
 * @author root
 */
@Controller("frontWechatOAuthController")
@RequestMapping(value = "${frontPath}/oauth/wechat")
public class WechatOAuthController extends OAuthLoginController {

	/**
	 * 微信服务
	 */
	@Autowired
	private WechatService wechatService;
	
	/**
	 * 定位到授权页面
	 */
	@Override
	protected String getAuthorizeURL(HttpServletRequest request, String returnUrl) {
		
		// 请求的域名
		String domain = request.getServerName();
		
	    // 根据 domain 找到具体的app
		App app = WechatUtils.getDomainApp(domain);
		
		// 可以不设置重定向的地址
		return wechatService.bind(app).getAuthorizeURL(domain, returnUrl);
	}

	/**
	 * 获取授权信息
	 */
	@Override
	protected Map<String, Object> getAccessToken(HttpServletRequest request, String code, String state) {
		
		// 请求的域名
		String domain = request.getServerName();
		
	    // 根据 domain 找到具体的app
		App app = WechatUtils.getDomainApp(domain);
		
		Map<String, Object> params = Maps.newHashMap();
		SnsToken token = wechatService.bind(app).oauth2AccessToken(code, state);
		if (token != null && StringUtils.isNotBlank(token.getAccess_token())) {
			String openId = token.getOpenid();
			String unionId = token.getUnionid();
			params.put("appId", token.getAppId());
			params.put("openId", openId);
			params.put("unionId", unionId);
			params.put("accessToken", token.getAccess_token());
			
			// 需要存储到 Session 中的数据
			params.put("_token", token);
		}
		return params;
	}

	/**
	 * 新建一个账户
	 */
	@Override
	protected UserAccount newUserAccount(Map<String, Object> token) {
		UserAccount account = new UserAccount();
		account.setType((byte)4);
		
		// 设置微信公众号信息
		String appId = String.valueOf(token.get("appId"));
		String openId = String.valueOf(token.get("openId"));
		if (StringUtils.isNotBlank(appId)) {
			UserWechat wechat = new UserWechat();
			wechat.setOpenId(openId);
			wechat.setAppId(appId);
			account.setWechat(wechat);
		}
		return account;
	}

	/**
	 * 获取用户信息
	 */
	@Override
	protected User fetchUserInfo(String accessToken, String openId) {
		UserInfo user = wechatService.authUserinfo(accessToken, openId);
		if (user != null) {
			User member = new User();
			member.setName(StringUtils.mb4Replace(user.getNickname(), null));
			member.setSex((user.getSex() != null && "1".equals(user.getSex().toString()))?BaseEntity.YES:BaseEntity.NO);
	        member.setProvince(user.getProvince());
			member.setCity(user.getCity());
	        member.setHeadimg(user.getHeadimgurl());
	        return member;
		}
		return null;
	}
}