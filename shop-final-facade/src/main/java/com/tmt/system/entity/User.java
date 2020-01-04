package com.tmt.system.entity;

import java.io.Serializable;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.tmt.Constants;
import com.tmt.core.entity.BaseEntity;
import com.tmt.core.utils.StringUtils;

/**
 * 系统用户
 * 
 * @author lifeng
 *
 */
public class User extends BaseEntity<Long> implements Serializable {

	private static final long serialVersionUID = 1L;
	private String loginName;// 用户账户
	private String password;// 密码
	private String no;// 用户编号
	private String name;// 昵称(如果只用到一个姓名，则这个字段就是真实姓名，如果用到昵称 和真实姓名则这个字段是昵称)
	private String email;// 电子邮件
	private String mobile;// 移动电话
	private String province;// 省
	private String city;// 市
	private Byte sex;// 性别
	private String loginIp;// 最后一次登录IP
	private Date loginDate;// 最后一次登录日期
	private Integer status = 1;// 用户状态(1247 模式)：1 待修改密码 ,2 锁定 ,4 正常 （默认 1）
	private Long officeId;// 所属组织（系统内部维护的组织,与真实用户的组织无关）
	private String officeCode;//
	private String officeName;
	// 这个字段以前没用到，现在用来区分不同的注册类型：网站注册、微信用户：方便后台统计
	private UserType userType;
	// 新加的字段 -- 注册时加上
	private String headimg;// 头像地址
	private String roleIds;
	private String roleNames;
	private String groupIds;
	private String groupNames;
	// 找回密码相关:安全码，过期时间
	private String secretKey;
	private Date expiresDate;

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Byte getSex() {
		return sex;
	}

	public void setSex(Byte sex) {
		this.sex = sex;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getHeadimg() {
		return headimg;
	}

	public void setHeadimg(String headimg) {
		this.headimg = headimg;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public Date getExpiresDate() {
		return expiresDate;
	}

	public void setExpiresDate(Date expiresDate) {
		this.expiresDate = expiresDate;
	}

	public String getGroupIds() {
		return groupIds;
	}

	public void setGroupIds(String groupIds) {
		this.groupIds = groupIds;
	}

	public String getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(String groupNames) {
		this.groupNames = groupNames;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public String getRoleNames() {
		return roleNames;
	}

	public void setRoleNames(String roleNames) {
		this.roleNames = roleNames;
	}

	public Long getOfficeId() {
		return officeId;
	}

	public void setOfficeId(Long officeId) {
		this.officeId = officeId;
	}

	public String getOfficeCode() {
		return officeCode;
	}

	public void setOfficeCode(String officeCode) {
		this.officeCode = officeCode;
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserType getUserType() {
		return userType;
	}

	public String getUserTypeName() {
		if (userType != null) {
			return userType.name;
		}
		return "";
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getUserStatus() {
		if (status == null) {
			return "未知状态";
		}
		return UserStatus.valueOf(status);
	}

	/**
	 * 账户是否锁定,除了正常的之前其他 修改待修改密码状态为不锁定状态 add by syd 2018-11-30
	 * 
	 * @return
	 */
	public Boolean isLocked() {
		return (status != null && (User.YES == this.getDelFlag() || (this.getStatus() != UserStatus.NARMAL.getValue()
				&& this.getStatus() != UserStatus.MD_P.getValue())));
	}

	/**
	 * 设置用户的状态
	 * 
	 * @param status
	 */
	public void setUserStatus(UserStatus status) {
		if (status == UserStatus.MD_P) {
			if (this.status == UserStatus.LOCK_U.getValue() || this.status == UserStatus.MD_P_A_LOCK.getValue()) {
				this.status = UserStatus.MD_P_A_LOCK.getValue();
			} else if (this.status == UserStatus.MD_P.getValue() || this.status == UserStatus.NARMAL.getValue()) {
				this.status = UserStatus.MD_P.getValue();
			}
		} else if (status == UserStatus.LOCK_U) {
			if (this.status == UserStatus.LOCK_U.getValue() || this.status == UserStatus.NARMAL.getValue()) {
				this.status = UserStatus.LOCK_U.getValue();
			} else if (this.status == UserStatus.MD_P.getValue() || this.status == UserStatus.MD_P_A_LOCK.getValue()) {
				this.status = UserStatus.MD_P_A_LOCK.getValue();
			}
		} else if (status == UserStatus.NARMAL) {
			if (this.status == UserStatus.MD_P.getValue() || this.status == UserStatus.MD_P_A_LOCK.getValue()) {
				this.status = UserStatus.MD_P.getValue();
			} else if (this.status == UserStatus.LOCK_U.getValue() || this.status == UserStatus.NARMAL.getValue()) {
				this.status = UserStatus.NARMAL.getValue();
			}
		}
		if (this.status != UserStatus.MD_P.getValue() && this.status != UserStatus.LOCK_U.getValue()
				&& this.status != UserStatus.MD_P_A_LOCK.getValue() && this.status != UserStatus.NARMAL.getValue()) {
			this.status = status.getValue();
		}
	}

	/**
	 * 用户注册的类型
	 * 
	 * @author lifeng
	 */
	public enum UserType {
		QQ("QQ"), SINA("微博"), SITE("网站"), WX("微信网站"), WE_CHAT("微信公众号"), EMAIL("邮箱"), SMS("手机号");
		private String name;

		private UserType(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
	}

	/**
	 * 用户状态
	 * 
	 * @author lifeng
	 */
	public enum UserStatus {
		MD_P("待修改密码", 1), LOCK_U("锁定", 2), MD_P_A_LOCK("待修改密码-锁定", 3), NARMAL("正常", 4), OTHER("其他", -1);
		private String name;
		private int value;

		private UserStatus(String name, int value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public static String valueOf(int value) {
			for (UserStatus status : UserStatus.values()) {
				if (status.getValue() == value) {
					return status.getName();
				}
			}
			return UserStatus.OTHER.getName();
		}
	}

	/**
	 * 是否系统用户 返回值改为小写，前端就可以使用${user.root}
	 * 
	 * @return
	 */
	public boolean isRoot() {
		return Long.valueOf(Constants.ROOT).equals(this.getId());
	}

	/**
	 * 1. SuperAdmin（内置用户）不能修改用户名 将ID设置为0则，用户名可以修改
	 * 
	 * 2. 数据库唯一索引中可以出现多个未null 的 单，保存数据时，如果页面没填值则将数据库值 设置为“”（和唯一索引有冲突） 所以保存时，将"" 转为
	 * null
	 */
	@Override
	public void preUpdate() {
		super.preUpdate();
		if (StringUtils.isBlank(this.getEmail())) {
			this.setEmail(null);
		}
		if (StringUtils.isBlank(this.getLoginName())) {
			this.setLoginName(null);
		}
		if (StringUtils.isBlank(this.getMobile())) {
			this.setMobile(null);
		}
	}
}