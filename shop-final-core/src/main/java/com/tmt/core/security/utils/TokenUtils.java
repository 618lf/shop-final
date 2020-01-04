package com.tmt.core.security.utils;

import com.tmt.core.jwt.JWT;
import com.tmt.core.jwt.JWTVerifier;
import com.tmt.core.jwt.algorithms.Algorithm;
import com.tmt.core.security.principal.Principal;
import com.tmt.core.utils.JsonMapper;
import com.tmt.core.utils.StringUtils;

/**
 * 创建token
 * Jwts 依赖 fastxml 需要研究是否有替代的方案
 * @author lifeng
 */
public class TokenUtils {

	/**
	 * 得到 token
	 * 
	 * @param sub
	 * @param key
	 * @return
	 */
	public static String getToken(Principal principal, String key) {
		try {
			String subject = JsonMapper.toJson(principal);
			return JWT.create().withIssuer("tmt").withSubject(subject).sign(Algorithm.HMAC256(key));
		} catch (Exception e) {}
		return null;
	}

	/**
	 * 得到用户实体
	 * @param token
	 * @param key
	 * @return
	 */
	public static Principal getSubject(String token, String key) {
		try {
			JWTVerifier verifier = JWT.require(Algorithm.HMAC256(key)).withIssuer("tmt").build();
			String _principal = verifier.verify(token).getSubject();
			if (StringUtils.hasText(_principal)) {
				return JsonMapper.fromJson(_principal, Principal.class);
			}
		} catch (Exception e) {e.printStackTrace();} 
		return null;
	}
	
	public static void main(String[] args) {
		Principal principal = new Principal(1L, "lifeng");
		String token = TokenUtils.getToken(principal, "secret");
		principal = TokenUtils.getSubject(token, "secret");
		System.out.println(JsonMapper.toJson(principal));
	}
}