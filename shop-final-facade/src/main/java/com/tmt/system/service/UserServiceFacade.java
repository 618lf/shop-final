package com.tmt.system.service;

import java.util.List;
import java.util.Map;

import com.tmt.common.service.BaseServiceFacade;
import com.tmt.system.entity.User;
import com.tmt.system.entity.UserAccount;
import com.tmt.system.entity.UserSession;
import com.tmt.system.entity.UserUnion;

/**
 * 用户管理
 * @author root
 */
public interface UserServiceFacade extends BaseServiceFacade<User, Long> {  
	
	/**
	 * 请不要随意这个方法，这个是后台管理员修改的方法
	 * @param user
	 * @return
	 */
	public Long save(User user);
	
	/**
	 * 只会修改用户的基本信息,不会修改会员的密码、权限等信息（用户自己触发的修改）
	 * @param user
	 */
	public void userUpdate(User user);
	
	/**
	 * 给用户分配权限组
	 * @param user
	 */
	public void grantGroups(User user);
	
	/**
	 * 批量删除
	 * @param users
	 */
	public void delete(List<User> users);
	
	/**
	 * 用户session
	 * @param users
	 * @return
	 */
	public List<UserSession> userSessions(List<User> users);
	
	/**
	 * 锁住用户
	 * @param users
	 */
	public void lockUser(List<User> users);
	
	/**
	 * 用户解锁
	 * @param users
	 */
	public void unLockUser(List<User> users);
	
	/**
	 * 修改头像
	 * @param user
	 */
	public void updateHeadimg(User user);
	
	/**
	 * 修改密码
	 * @param user
	 */
	public void updatePassWord(User user);
	
	/**
	 * 修改状态
	 * @param user
	 */
	public void updateStatus(User user);
	
	/**
	 * 忘记密码,修改密码
	 * @param user
	 */
	public void memberUpdatePassword(User user);
	
	/**
	 * 忘记密码
	 * @param user
	 */
	public void memberForgetPassword(User user);
	
    /**
     * 第一次进入修改密码
     * @param user
     */
    public void updatePassWordAndStatus(User user);
	
	/**
	 * 用户注册校验
	 * @param user
	 * @return
	 */
	public int checkAccountByRegister(String account);
	
	/**
	 * 校验用户编码
	 * @param user
	 * @return
	 */
	public int checkUserNo(User user);
	
	/**
	 * 校验用户邮箱
	 * @param user
	 * @return
	 */
	public int checkUserAccount(UserAccount account);
	
	/**
	 * 根据账户查询用户
	 * @param account
	 * @return
	 */
	public User findUserByAccount(String account);
	
	/**
	 * 根据编码查询用户
	 * @param no
	 * @return
	 */
	public User findUserByNo(String no);
	
	/**
	 * 根据邮箱查询忘记密码用户
	 * @param email
	 * @return
	 */
	public User findForgetPasswordMember(String email);
	
	/**
	 * 根据ID查询用户头像
	 * @param userId
	 * @return
	 */
	public String findUserHeadimgById(Long userId);
	
	/**
	 * 查询用户组下的所有的用户
	 * @param groupId
	 * @return
	 */
	public List<User> findUsersByGroupId(Long groupId);
	
	/**
	 * 树形选择用户
	 * @return
	 */
	public List<Map<String, Object>> userTreeSelect(Long officeId);
	
	/**
	 * 得到用户帐号
	 * @param account
	 * @return
	 */
    public UserAccount findByAccount(String account);
    
    /**
     * 得到用户统一帐号
     * @param union
     * @return
     */
    public UserUnion findByUnion(String union);
    
    /**
     * 注册
     * @param account
     */
    public void registerByAccount(UserAccount account);
    
    /**
     * 绑定此账户
     * @param account
     */
    public void bindUserAccount(UserAccount account);
	
    /**
     * 用户资源锁
     * @param user
     */
    public void userResourceLock(User user);
    
}