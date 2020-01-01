package com.tmt.system.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.common.persistence.BaseDaoImpl;
import com.tmt.common.utils.StringUtils;
import com.tmt.system.entity.User;
import com.tmt.system.entity.UserAccount;

/**
 * 登录帐号 管理
 * @author 超级管理员
 * @date 2016-09-01
 */
@Repository("systemUserAccountDao")
public class UserAccountDao extends BaseDaoImpl<UserAccount,String> {
	
	/**
	 * 删除账户信息
	 * @param user
	 */
	public void delete(User user) {
		
		// 用户名
		if (StringUtils.isNotBlank(user.getLoginName())) {
			UserAccount account = new UserAccount();
			account.setId(user.getLoginName());
			this.delete(account);
		}
		
		// 邮箱
		if (StringUtils.isNotBlank(user.getEmail())) {
			UserAccount account = new UserAccount();
			account.setId(user.getEmail());
			this.delete(account);
		}
		
		// 手机
		if (StringUtils.isNotBlank(user.getMobile())) {
			UserAccount account = new UserAccount();
			account.setId(user.getMobile());
			this.delete(account);
		}
	}
	
	/**
	 * 保存用户帐号
	 * @param user
	 */
	public void save(User user) {
		
		// 用户名
		if (StringUtils.isNotBlank(user.getLoginName())) {
			UserAccount account = new UserAccount();
			account.setId(user.getLoginName());
			account.setUserId(user.getId());
			account.setType((byte)1);
			this.insert(account);
		}
		
		// 邮箱
		if (StringUtils.isNotBlank(user.getEmail())) {
			UserAccount account = new UserAccount();
			account.setId(user.getEmail());
			account.setUserId(user.getId());
			account.setType((byte)2);
			this.insert(account);
		}
		
		// 手机
		if (StringUtils.isNotBlank(user.getMobile())) {
			UserAccount account = new UserAccount();
			account.setId(user.getMobile());
			account.setUserId(user.getId());
			account.setType((byte)3);
			this.insert(account);
		}
	}
}
