package com.tmt.system.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tmt.common.config.Globals;
import com.tmt.common.entity.TreeVO;
import com.tmt.common.persistence.BaseDao;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.service.BaseService;
import com.tmt.common.utils.DateUtil3;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.Maps;
import com.tmt.common.utils.StringUtil3;
import com.tmt.system.dao.GroupUserDao;
import com.tmt.system.dao.RoleUserDao;
import com.tmt.system.dao.UserAccountDao;
import com.tmt.system.dao.UserDao;
import com.tmt.system.dao.UserNoDao;
import com.tmt.system.dao.UserSessionDao;
import com.tmt.system.dao.UserUnionDao;
import com.tmt.system.dao.UserWechatDao;
import com.tmt.system.entity.GroupUser;
import com.tmt.system.entity.RoleUser;
import com.tmt.system.entity.SystemConstant;
import com.tmt.system.entity.UpdateData;
import com.tmt.system.entity.User;
import com.tmt.system.entity.User.UserStatus;
import com.tmt.system.entity.User.UserType;
import com.tmt.system.entity.UserAccount;
import com.tmt.system.entity.UserNo;
import com.tmt.system.entity.UserSession;
import com.tmt.system.entity.UserUnion;
import com.tmt.system.entity.UserWechat;
import com.tmt.update.UpdateServiceFacade;

/**
 * 用户管理
 * @author root
 */
@Service
public class UserService extends BaseService<User,Long> implements UserServiceFacade{
	
	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleUserDao roleUserDao;
	@Autowired
	private GroupUserDao groupUserDao; 
	@Autowired
	private OfficeService officeService;
	@Autowired
	private NumberGeneratorFacade numberGenerator; 
	@Autowired
	private UserAccountDao accountDao;
	@Autowired
	private UserUnionDao unionDao;
	@Autowired
	private UserNoDao userNoDao;
	@Autowired
	private UserWechatDao userWechatDao;
	@Autowired
	private UserSessionDao userSessionDao;
	@Autowired
	private UpdateServiceFacade updateService;
	
	@Override
	protected BaseDao<User, Long> getBaseDao() {
		return this.userDao;
	}
	/**
	 * 请不要随意这个方法，这个是后台管理员修改的方法
	 * @param user
	 * @return
	 */
	@Override
	@Transactional
	public Long save(User user) {
		if (IdGen.isInvalidId(user.getId())) {
		    user.setPassword(Globals.entryptPassword("hello"));//默认密码
		    if (StringUtil3.isBlank(user.getNo())) {
		    	user.setNo(this.createUserCode());
		    }
		    user.preUpdate();
		    this.insert(user);
		    this.userNoDao.save(user);
		    this.accountDao.save(user);
		} else {
			User _old = this.get(user.getId());
			List<RoleUser> userTemps = this.roleUserDao.findByUserId(user.getId());
			List<GroupUser> userGroups = this.groupUserDao.findByUserId(user.getId());
			this.roleUserDao.batchDelete(userTemps);
			this.groupUserDao.batchDelete(userGroups);
			this.update(user);
			this.accountDao.delete(_old);
		    this.accountDao.save(user);
		}
		//用户权限
		String roleIdStrs = user.getRoleIds();
		if (StringUtil3.isNotBlank(roleIdStrs) ) {
			List<RoleUser> roleUsers = Lists.newArrayList();
			String[] roleIds = roleIdStrs.split(",");
			RoleUser roleUser = null;
			for( String roleId :roleIds ) {
				if(StringUtil3.isNotEmpty(roleId)) {
					roleUser = new RoleUser();
					roleUser.setRoleId(Long.parseLong(roleId));
					roleUser.setUserId(user.getId());
					roleUsers.add(roleUser);
				}
			}
			this.roleUserDao.batchInsert(roleUsers);
		}
		//用户组
		String groupIdStrs = user.getGroupIds();
		if (StringUtil3.isNotBlank(groupIdStrs)) {
			List<GroupUser> userGroups = Lists.newArrayList();
			String[] groupIds = groupIdStrs.split(",");
			GroupUser userGroup = null;
			for(String groupId :groupIds ) {
				if(StringUtil3.isNotEmpty(groupId) ) {
					userGroup = new GroupUser();
					userGroup.setGroupId(Long.parseLong(groupId));
					userGroup.setUserId(user.getId());
					userGroups.add(userGroup);
				}
			}
			this.groupUserDao.batchInsert(userGroups);
		}
		return user.getId();
	}
	
	/**
	 * 只会修改用户的基本信息,不会修改会员的密码、权限等信息（用户自己触发的修改）
	 * @param user
	 */
	@Override
	@Transactional
	public void userUpdate(User user) {
		User old = this.get(user.getId());
		this.update("updateSelfInfo", user);
		this.accountDao.delete(old);
		this.accountDao.save(user);
	}
	
	/**
	 * 给用户分配权限组
	 * @param user
	 */
	@Override
	@Transactional
	public void grantGroups(User user) {
		List<GroupUser> userGroups = this.groupUserDao.findByUserId(user.getId());
		this.groupUserDao.batchDelete(userGroups);
		//用户组
		String groupIdStrs = user.getGroupIds();
		if (StringUtil3.isNotBlank(groupIdStrs)) {
			userGroups = Lists.newArrayList();
			String[] groupIds = groupIdStrs.split(",");
			GroupUser userGroup = null;
			for(String groupId :groupIds) {
				if(StringUtil3.isNotEmpty(groupId) ) {
					userGroup = new GroupUser();
					userGroup.setGroupId(Long.parseLong(groupId));
					userGroup.setUserId(user.getId());
					userGroups.add(userGroup);
				}
			}
			this.groupUserDao.batchInsert(userGroups);
		}
	}
	
	@Override
	@Transactional
	public void delete(List<User> users){
		
		//删除用户 -- 应该改为删除状态（不应改直接删除）
		super.batchDelete(users);
		
		//删除权限
		List<RoleUser> roleUsers = Lists.newArrayList();
		for(User user:users) {
			List<RoleUser> userTemps = this.roleUserDao.findByUserId(user.getId());
			if(userTemps != null) {
				roleUsers.addAll(userTemps);
			}
		}
		this.roleUserDao.batchDelete(roleUsers);
		List<GroupUser> groupUsers = Lists.newArrayList();
		for(User user: users) {
			List<GroupUser> groupTemps = this.groupUserDao.findByUserId(user.getId());
			if(groupTemps != null) {
				groupUsers.addAll(groupTemps);
			}
		}
		this.groupUserDao.batchDelete(groupUsers);
		//删除编号，账户
		for(User user:users) {
			this.userNoDao.delete(user);
			this.accountDao.delete(user);
		}
	}
	
	/**
	 * 用户session
	 * @param users
	 * @return
	 */
	@Override
	public List<UserSession> userSessions(List<User> users) {
		List<UserSession> sessions = Lists.newArrayList();
		for(User user: users) {
			UserSession session = this.userSessionDao.get(user.getId());
			if (session != null) {
				sessions.add(session);
			}
		}
		this.userSessionDao.batchDelete(sessions);
		return sessions;
	}
	
	@Override
	@Transactional
	public void lockUser(List<User> users) {
		this.batchUpdate("updateStatus", users);
	}
	
	@Override
	@Transactional
	public void unLockUser(List<User> users) {
		this.batchUpdate("updateStatus", users);
		
	}
	
	@Override
	@Transactional
	public void updatePassWord(User user) {
		if (StringUtil3.isNotBlank(user.getPassword())) {
		    user.setPassword(Globals.entryptPassword(user.getPassword()));
		} 
		this.userDao.update("updatePassWord",user);
	}
	
	@Override
	@Transactional
	public void updateStatus(User user) {
		this.userDao.update("updateStatus",user);
	}
	
	@Transactional
	public void updatePassWordAndStatus(User user ) {
		if(StringUtil3.isNotBlank(user.getPassword())) {
		   user.setPassword(Globals.entryptPassword(user.getPassword()));
		} 
		this.userDao.update("updatePassWordAndStatus",user);
	}
	
	/**
	 * 会员修改密码
	 * @param member
	 */
	@Override
	@Transactional
	public void memberUpdatePassword(User member){
		this.updatePassWord(member);
		if (StringUtil3.isNotBlank(member.getSecretKey())) {
			member.setSecretKey(null);
			member.setExpiresDate(null);
			this.memberForgetPassword(member);
		}
	}
	
	@Override
	@Transactional
	public void memberForgetPassword(User user){
		this.userDao.update("updateByForgetPassword", user);
	}
	
	/**
	 * 校验用户编码
	 */
	@Override
	public int checkUserNo(User user){
		return this.userNoDao.countByCondition("checkUserNo",user);
	}
	
	/**
	 * 校验用户邮箱
	 */
	@Override
	public int checkUserAccount(UserAccount accont){
		return this.accountDao.countByCondition("checkUserAccount", accont);
	}
	
	/**
	 * 根据用户账户获取用户
	 */
	@Override
	public User findUserByAccount(String account){
		UserAccount _account = this.accountDao.get(account);
		if (_account != null) {
		    return this.get(_account.getUserId());
		}
		return null;
	}
	
	/**
	 * 根据编号获取用户
	 */
	@Override
	public User findUserByNo(String no){
		UserNo _no = this.userNoDao.get(no);
		if (_no != null) {
			return this.get(_no.getUserId());
		}
		return null;
	}
	
	/**
	 * 查找需要找回密码的用户,只能读取一次
	 * @param email
	 * @return
	 */
	public User findForgetPasswordMember(String email) {
		UserAccount _account = this.accountDao.get(email);
		if (_account != null) {
			User user = this.queryForObject("findForgetPasswordUser", _account.getUserId());
			if (user != null && StringUtil3.isNotBlank(user.getSecretKey())) {
			    return user;
			}
		}
		return new User();
	}
	
	/**
	 * 用户头像
	 */
	public String findUserHeadimgById(Long userId){
		return this.queryForAttr("findUserHeadimgById", userId);
	}

	/**
	 * 获得组下的用户
	 */
	public List<User> findUsersByGroupId(Long groupId) {
        return this.queryForList("findUsersByGroupId", groupId);
	}
	
	/**
	 * 获得组织下的用户
	 * @param officeId
	 * @return
	 */
	public List<User> findOfficeUsers(Long officeId) {
		return this.queryForList("findOfficeUsers", officeId);
	}
	
	/**
	 * 获得部门下的用户
	 * @param officeId
	 * @return
	 */
	public List<User> findDepartUsers(Long officeId) {
		return this.queryForList("findDepartUsers", officeId);
	}
	
	/**
	 * 用户树形选择
	 */
	public List<Map<String, Object>> userTreeSelect(Long officeId){
		List<Map<String, Object>> mapList = Lists.newArrayList();
		Map<String,Object> param = Maps.newHashMap();
		if(officeId != null) {
		   param.put("CASCADE_WITH_PARENT_ID", officeId);
		   param.put("CASCADE_WITH_OFFICE_ID", officeId);
		}
		List<TreeVO> orgTrees = this.officeService.findTreeList(param);
		List<TreeVO> userTrees = this.queryForGenericsList("findTreeList", param);
		for(int i=0; i<orgTrees.size(); i++){
			TreeVO e = orgTrees.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", "O_"+e.getId());
			map.put("pId", "O_"+e.getParent());
			map.put("name", StringUtil3.isBlank(e.getTreeName())?"匿名":e.getTreeName());
			map.put("chkDisabled", Boolean.TRUE);
			map.put("selectAbled", Boolean.FALSE);
			int iCount = this.countByCondition("officeSelectCheck",e.getId());
			if(iCount > 0) {
				map.put("chkDisabled", Boolean.FALSE);
			}
			mapList.add(map);
		}
		for(int i=0; i<userTrees.size(); i++){
			TreeVO e = userTrees.get(i);
			Map<String, Object> map = Maps.newHashMap();
			map.put("id", e.getId());
			map.put("pId", "O_"+e.getParent());
			map.put("name", e.getTreeName());
			map.put("type", 1);
			map.put("selectAbled", Boolean.TRUE);
			mapList.add(map);
		}
		return mapList;
	}
	
	/**
	 * 校验用户邮箱
	 */
	@Override
	public int checkAccountByRegister(String account) {
		return this.accountDao.countByCondition("checkAccountByRegister", account);
	}
	
	/**
	 * 创建全局的会员号
	 * @return
	 */
	private String createUserCode() {
		String nextSeq = String.valueOf(numberGenerator.generateNumber(SystemConstant.GLOBAL_MEMBER_CODE));
		return StringUtil3.leftPad(nextSeq, 8, "0");
	}
	
	/**
	 * 获得用户对应公众号的OPENID
	 * @param appId
	 * @return
	 */
	public String getUserWechatOpenId(User user, String appId) {
		UserWechat wechat = this.userWechatDao.get(user, appId);
		return wechat != null?wechat.getOpenId():null;
	}
	
	/**
	 * 得到用户帐号
	 * @param account
	 * @return
	 */
	@Override
	public UserAccount findByAccount(String account) {
		return accountDao.get(account);
	}

	/**
	 * 得到用户统一帐号
	 * @param account
	 * @return
	 */
	@Override
	public UserUnion findByUnion(String union) {
		return unionDao.get(union);
	}
	
	/**
	 * 用户注册
	 */
	@Override
	@Transactional
	public void registerByAccount(UserAccount account) {
		UserAccount _account = accountDao.get(account.getId());
		if (_account != null) {
			account.setUserId(_account.getUserId());
			return;
		}
		
		// 注册新账户
		User member = account.getUser();
		if (account.getType() == 4) {
			member.setUserType(UserType.WE_CHAT);
		} else if(account.getType() == 5) {
			member.setUserType(UserType.WX);
		} else if(account.getType() == 6)  {
			member.setUserType(UserType.QQ);
		} else if(account.getType() == 7)  {
			member.setUserType(UserType.SINA);
		} else if(account.getType() == 8)  {
			member.setUserType(UserType.SMS);
		} else if(account.getType() == 9)  {
			member.setUserType(UserType.EMAIL);
		} else {
			member.setUserType(UserType.SITE);
		}
		
		// 用户基本信息
		if (StringUtil3.isNotBlank(member.getPassword())) {
			member.setPassword(Globals.entryptPassword(member.getPassword()));
		}
		member.setStatus(UserStatus.NARMAL.getValue());
		member.setNo(this.createUserCode());
		this.insert(member); this.userNoDao.save(member);
		
		// 添加账户信息
		account.setUserId(member.getId());
		
		// 保存帐号信息
		this.saveAccount(account);
		
		// 发送信号量
		this._update(member, SystemConstant.USER_UP);
	}
	
	/**
	 * 绑定此帐号
	 */
	@Override
	@Transactional
	public void bindUserAccount(UserAccount account) {
		UserAccount _account = accountDao.get(account.getId());
		if (_account != null) {
			return;
		}
		
		// 保存帐号信息
		this.saveAccount(account);
	}
	
	// 保存帐号信息
	private void saveAccount(UserAccount account) {
		// 添加账户信息
		this.accountDao.insert(account);
		
		// 如果有统一用户信息
		UserUnion union = account.getUnion();
		if (union != null) {
			union.setUserId(account.getUserId());
			unionDao.insert(union);
		}
		
		// 微信用户，添加
		UserWechat wechat = account.getWechat();
		if (wechat != null) {
			wechat.setUserId(account.getUserId());
			this.userWechatDao.save(wechat);
		}
	}
	
	/**
	 * 锁住用户(资源锁)
	 * @param user
	 */
	@Override
	@Transactional
	public void userResourceLock(User user) {
		this.userDao.lock(user);
	}
	
	/**
	 * 修改头像
	 */
	@Override
	public void updateHeadimg(User user) {
		this.update("updateHeadimg", user);
	}
	
	/**
	 * 用户登录
	 */
	@Transactional
	public String userLogin(User user, String sessionId, String loginIp) {
		
		// 之前
		String oldSessionId = null;
		
		Date now = DateUtil3.getTimeStampNow();
		
		// 失效之前的
		UserSession _old = this.userSessionDao.get(user.getId());
		if (_old != null && StringUtil3.isNotBlank(_old.getSessionId())) {
			oldSessionId = _old.getSessionId();
		}
		
		// 更新session信息
		if (StringUtil3.isNotBlank(sessionId)) {
			UserSession session = new UserSession();
			session.setUserId(user.getId()); session.setSessionId(sessionId); session.setUpdateTime(now);
			this.userSessionDao.insert(session);
		}
		
		// 更新用户登录信息
		user.setLoginDate(now); user.setLoginIp(loginIp);
		this.update("updateUserLoginAction", user);
		
		// 发送信号量
		this._update(user, SystemConstant.USER_IN);
		return oldSessionId;
	}
	
	// 用户更新
	private void _update(User user, byte module) {
		UpdateData updateData = new UpdateData();
		updateData.setId(user.getId());
		updateData.setMsg(StringUtil3.abbr(user.getName(), 30));
		updateData.setModule(module);
		updateData.setOpt((byte)0);
		updateService.save(updateData);
	}
}