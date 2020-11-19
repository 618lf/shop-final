package com.tmt.system.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.core.codec.Digests;
import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.system.entity.User;
import com.tmt.system.entity.UserWechat;

/**
 * 微信会员 管理
 * @author 超级管理员
 * @date 2016-09-01
 */
@Repository("systemUserWechatDao")
public class UserWechatDao extends BaseDaoImpl<UserWechat,String> {
	
	/**
	 * 保存
	 * @param wechat
	 */
	public void save(UserWechat wechat) {
		String id = Digests.md5(new StringBuilder().append(wechat.getUserId()).append("-").append(wechat.getAppId()).toString());
		wechat.setId(id);
		this.insert(wechat);
	}
	
	/**
	 * 获取用户微信账户ID
	 * @param user
	 * @param appId
	 * @return
	 */
	public UserWechat get(User user, String appId) {
		String id = Digests.md5(new StringBuilder().append(user.getId()).append("-").append(appId).toString());
		return this.get(id);
	}
}