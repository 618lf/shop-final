package com.tmt.core.security.principal.support;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tmt.core.cache.redis.RedisUtils;
import com.tmt.core.codec.Digests;
import com.tmt.core.security.principal.Principal;
import com.tmt.core.security.principal.PrincipalStrategy;
import com.tmt.core.security.subject.Subject;
import com.tmt.core.security.utils.TokenUtils;
import com.tmt.core.utils.StringUtils;

/**
 * 基于 token 的身份解决方案
 * 对于key 的管理可以更加简单， 看情况而定
 * @author lifeng
 */
public class TokenPrincipalStrategy implements PrincipalStrategy {

	private String tokenName = "token";
	private Integer timeOut = 24 * 60 * 60 * 30; // 一个月
	
	public Integer getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(Integer timeOut) {
		this.timeOut = timeOut;
	}

	public String getTokenName() {
		return tokenName;
	}

	public void setTokenName(String tokenName) {
		this.tokenName = tokenName;
	}

	/**
	 * 创建身份信息 基于 Jwts
	 */
	@Override
	public void createPrincipal(Subject subject, HttpServletRequest request,
			HttpServletResponse response) {
        String key = UUID.randomUUID().toString();
		String token = TokenUtils.getToken(subject.getPrincipal(), key);
		response.setHeader(this.getTokenName(), token);
		
		// session id
		String sessionId = this.getKey(token);
		RedisUtils.set(sessionId, key, this.getTimeOut());
		subject.setSessionId(sessionId);
	}

	/**
	 * 将身份信息无效
	 */
	@Override
	public void invalidatePrincipal(Subject subject,
			HttpServletRequest request, HttpServletResponse response) {
		response.setHeader(this.getTokenName(), "");
	}

	/**
	 * 获取身份信息
	 */
	@Override
	public void resolvePrincipal(Subject subject, HttpServletRequest request,
			HttpServletResponse response) {
		
		// 获取token
		String token = request.getHeader(this.getTokenName());
		if (!StringUtils.hasText(token)) {
			return;
		}
		
		// 获取加密的key 根据token 获取
		String sessionId = this.getKey(token);
		String key = RedisUtils.getObject(sessionId);
		if (!StringUtils.hasText(key)) {
			RedisUtils.expire(sessionId, this.getTimeOut());
		}
		subject.setSessionId(sessionId);
		
		if (!StringUtils.hasText(key)) {
			this.invalidatePrincipal(subject, request, response);
			return;
		}
		
		// 获取身份
		Principal principal = TokenUtils.getSubject(token, key);
		if (principal != null) {
			subject.setPrincipal(principal);
		}
	}
	
	/**
	 * 将此sessionId 无效
	 */
	@Override
	public void invalidatePrincipal(String sessionId) {
		RedisUtils.delete(sessionId);
	}

	/**
	 * 获得缓存的key值
	 * @param token
	 * @return
	 */
	protected String getKey(String token) {
		return Digests.md5(token);
	}
}