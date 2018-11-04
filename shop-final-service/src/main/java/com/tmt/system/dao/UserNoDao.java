package com.tmt.system.dao; 

import org.springframework.stereotype.Repository;

import com.tmt.common.persistence.BaseDaoImpl;
import com.tmt.system.entity.User;
import com.tmt.system.entity.UserNo;

/**
 * 登录帐号 管理
 * @author 超级管理员
 * @date 2016-09-01
 */
@Repository("systemUserNoDao")
public class UserNoDao extends BaseDaoImpl<UserNo,String> {
	
	/**
	 * 保存用户编码
	 * @param user
	 */
	public void save(User user) {
		UserNo no = new UserNo();
		no.setId(user.getNo());
		no.setUserId(user.getId());
		this.insert(no);
	}
	
	/**
	 * 保存用户编码
	 * @param user
	 */
	public void delete(User user) {
		UserNo no = new UserNo();
		no.setId(user.getNo());
		this.delete(no);
	}
}
