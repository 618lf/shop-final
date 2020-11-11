package com.tmt.system.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;

import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;
import com.tmt.system.entity.User;
import com.tmt.system.entity.UserAccount;
import com.tmt.system.entity.UserUnion;

/**
 * 授权登录
 * @author root
 */
public abstract class OAuthLoginController extends OAuthController{

	/**
	 * 重定向到授权页面
	 * @return
	 */
	@RequestMapping("/to.rt")
	public String to(String returnUrl, HttpServletRequest request, HttpServletResponse response){
		return WebUtils.redirectTo(this.getAuthorizeURL(request, returnUrl));
	}
	
	/**
	 * 授权回调
	 * 需要将token存储起来，方便使用
	 * @param code --- Authorization Code
	 * @param state --- 原始的state 值
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping("/oAuth.rt")
	public String oAuth(String code, String state, String to, HttpServletRequest request, HttpServletResponse response) {
		
		// 判断授权的参数
		if (!(StringUtils.isNotBlank(code) && StringUtils.isNotBlank(state))) {
			return this.redirectToSavedRequest(to, request, response);
		}
		
		// 获得token, 重回之前的页面（相当于重试一次）
		Map<String, Object> token = this.getAccessToken(request, code, state);
		if (token.isEmpty()) {
			return this.redirectToSavedRequest(to, request, response);
		}
		
		// 搜集授权信息
		String openId = (String)(token.get("openId"));
		String unionId = (String)(token.get("unionId"));
		UserAccount account = userService.findByAccount(openId);
		if (account != null) {
			// bug 这里是少了一个分支，如果之前没unionID 之后有了，没更新上去
			// 先登录(貌似需要保存其他的信息 -- 如果不保存则效率高)
			this.login(request, response, account);
		    // 返回之前的路径
		    return this.redirectToSavedRequest(to, request, response);
		} 
		
		// 此账户信息未授权过
		account = this.newUserAccount(token);
		account.setId(openId);
		
		// 如果有统一用户信息
		if (StringUtils.isNotBlank(unionId)) {
			UserUnion union = userService.findByUnion(unionId);
			if (union != null) {
				account.setUserId(union.getUserId());
			}  else {
				union = new UserUnion();
				union.setId(unionId);
				account.setUnion(union);
			}
		}
		
		// 如果没有用户信息 -- 走注册路线（或绑定到其他账户）
		if (account.getUserId() == null) {
			
			// 收集用户信息
			String accessToken = String.valueOf(token.get("accessToken"));
			User member = this.fetchUserInfo(accessToken, openId);
			account.setUser(member);
			
			// 保存定位信息，如果设置了
			if (StringUtils.isNotBlank(to)) {
			    WebUtils.savedRequestUrl(response, to);
		    }
			
			// 直接创建新的账户
			this.loginAndRegister(request, response, account);
			return this.redirectToSavedRequest(to, request, response);
	    }
		
		// 如果已经注册过 -- 走新增当前账户路线
	    this.loginAndBind(request, response, account);
	    
	    // 返回之前的路径
	    return this.redirectToSavedRequest(to, request, response);
	}
	
	/**
	 * 返回重定向的页面
	 * @return
	 */
	protected abstract String getAuthorizeURL(HttpServletRequest request, String returnUrl);
	
	/**
	 * 根据授权的参数返回用户账户对象
	 * @param code
	 * @param state
	 * @return
	 */
	protected abstract Map<String, Object> getAccessToken(HttpServletRequest request, String code, String state);
	
	/**
	 * 创建一个新的账户
	 * @return
	 */
	protected abstract UserAccount newUserAccount(Map<String, Object> token);
	
	/**
	 * 收集用户信息
	 * @return
	 */
	protected abstract User fetchUserInfo(String accessToken, String openId);
	
}
