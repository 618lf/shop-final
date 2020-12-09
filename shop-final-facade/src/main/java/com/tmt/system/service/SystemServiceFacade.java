package com.tmt.system.service;

import java.util.List;

import com.tmt.system.entity.Menu;
import com.tmt.system.entity.Role;
import com.tmt.system.entity.User;

/**
 * 用户管理
 * @author root
 */
public interface SystemServiceFacade{

	/**
	 * 用户ID获取用户
	 * @param account
	 * @return
	 */
	public User getUserById(Long userId);
	
	
	/**
	 * 用户唯一编号获取用户
	 * @param account
	 * @return
	 */
	public User getUserByNo(String no);
	
	/**
	 * 根据用户唯一信息获取用户：邮箱/登录名/手机
	 * @param account
	 * @return
	 */
	public User getUserByAccount(String account);
	
	/**
	 * 获得用户对应公众号的OPENID
	 * @param appId
	 * @return
	 */
	public String getUserWechatOpenId(User user, String appId);
	
	/**
	 * 用户头像
	 * @param account
	 * @return
	 */
	public String getUserHeadimgById(Long userId);
	
	/**
	 * 得到当前用户的权限
	 * @return
	 */
	public List<Menu> getMenus(User user);
	
	/**
	 * 得到当前用户的角色
	 * @return
	 */
	public List<Role> getRoles(User user);
	
	/**
	 * 查看用户的权限
	 * @param user
	 * @param permissions
	 * @return
	 */
	public List<Role> findByMenuPermission(User user, String permissions);
	
	/**
	 * 本小组成员
	 * @param user
	 * @param permissions
	 * @return
	 */
	public List<User> findOfficeUsers(Long officeId);
	
	/**
	 * 本部门成员
	 * @param officeId
	 * @return
	 */
	public List<User> findDepartUsers(Long officeId);
	
    /**
     * 用户登录
     */
    public String userLogin(User user, String sessionId, String loginIp);
}