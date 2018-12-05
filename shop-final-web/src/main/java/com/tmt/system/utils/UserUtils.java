package com.tmt.system.utils;

import java.util.List;

import com.tmt.Constants;
import com.tmt.common.exception.CaptchaException;
import com.tmt.common.security.context.ThreadContext;
import com.tmt.common.security.principal.Principal;
import com.tmt.common.security.realm.Realm;
import com.tmt.common.security.utils.SecurityUtils;
import com.tmt.common.utils.CacheUtils;
import com.tmt.common.utils.Ints;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.SpringContextHolder;
import com.tmt.common.utils.StringUtil3;
import com.tmt.common.web.ValidateCodeService;
import com.tmt.system.entity.Menu;
import com.tmt.system.entity.Role;
import com.tmt.system.entity.Site;
import com.tmt.system.entity.User;
import com.tmt.system.service.SystemServiceFacade;

/**
 * 用户共用信息 1. 用户的权限，菜单等，同一用户多次登录，也使用同一份数据 2. 用户工具类 --是与用户相关的信息才存入此缓存中，如果信息比较大则存储到
 * ehcache 这些个信息都是存放在session中的 如果把session 存放在 mmcache 中，则这些信息就是序列化到缓存中了。 直接存储到
 * ehcache 中 CACHE_USER 是前缀: 放入redis中 最后能每次访问都绑定到当前的线程,不然每次用一次都都取一次比较麻烦
 * 
 * @author lifeng
 */
public class UserUtils {

	private static SystemServiceFacade systemService = SpringContextHolder.getBean(SystemServiceFacade.class);
	private final static User NONE = new User();

	// -------- 得到指定用户的信息 ---------------
	/**
	 * 通过ID得到用户信息
	 * 
	 * @param userId
	 * @return
	 */
	public static User getUser(Long userId) {
		return systemService.getUserById(userId);
	}

	/**
	 * 通过ID得到用户信息
	 * 
	 * @param userId
	 * @return
	 */
	public static User getUserByAccount(String account) {
		return systemService.getUserByAccount(account);
	}

	/**
	 * 通过会员号获取用户信息
	 * 
	 * @param no
	 * @return
	 */
	public static User getUserByNo(String no) {
		return systemService.getUserByNo(no);
	}

	/**
	 * 获得当前用户对应的APP的 UserWechat信息
	 * 
	 * @param appId
	 * @return
	 */
	public static String getUserWechatOpenId(String appId) {
		User user = UserUtils.getUser();
		String openId = UserUtils.getAttribute(appId);
		if (openId == null) {
			openId = systemService.getUserWechatOpenId(user, appId);
			if (openId == null) {
				openId = "";
			}
			UserUtils.setAttribute(appId, openId);
		}
		return openId;
	}

	/**
	 * 得到当前用户的权限
	 * 
	 * @return
	 */
	public static List<String> getPermissions(User user) {
		List<String> permissions = Lists.newArrayList();
		List<Menu> menus = systemService.getMenus(user);
		for (Menu menu : menus) {
			if (StringUtil3.isNotBlank(menu.getPermission())) {
				permissions.add(menu.getPermission());
			}
		}
		return permissions;
	}

	/**
	 * 得到当前用户的角色
	 * 
	 * @return
	 */
	public static List<String> getRoles(User user) {
		List<String> permissions = Lists.newArrayList();
		List<Role> roles = systemService.getRoles(user);
		if (roles != null && roles.size() != 0) {
			for (Role role : roles) {
				if (StringUtil3.isNotBlank(role.getPermission())) {
					permissions.add(role.getPermission());
				}
			}
		}
		return permissions;
	}

	/**
	 * 得到用户的菜单
	 * 
	 * @param user
	 * @return
	 */
	public static List<Menu> getMenus(User user) {
		return systemService.getMenus(user);
	}

	// -------- 线程中的用户相关信息 ----------------------
	/**
	 * 同一用户共享（多个地方登录） Principal 是从 Session 中获取的
	 * 
	 * @return
	 */
	public static User getUser() {

		// 优先从线程栈中获取用户信息
		User user = ThreadContext.get(Constants.USER_KEY);
		Principal principal = null;

		// 没有数据，获取数据路径：缓存 --> 数据库获取
		if (user == null && (principal = (Principal) SecurityUtils.getSubject().getPrincipal()) != null
				&& principal.getId() != null) {
			String _key = new StringBuilder(Constants.CACHE_USER).append(principal.getId()).toString();
			user = CacheUtils.getSessCache().get(_key);
			if (user == null) {
				user = systemService.getUserById(principal.getId());
				CacheUtils.getSessCache().put(_key, user);
			}

			// 有用户信息 -- 不存储空用户
			if (user != null && user.getId() != null) {
				ThreadContext.put(Constants.USER_KEY, user);
			}
		}
		// 保存返回的数据是有值的
		return user != null ? user : NONE;
	}

	/**
	 * 是否拥有XXX权限
	 * 
	 * @param permission
	 * @return
	 */
	public static Boolean isPermitted(String permission) {
		return SecurityUtils.getSubject() != null && SecurityUtils.getSubject().isPermitted(permission);
	}

	/**
	 * 是否拥有XXX角色
	 * 
	 * @param role
	 * @return
	 */
	public static Boolean hasRole(String role) {
		return SecurityUtils.getSubject() != null && SecurityUtils.getSubject().hasRole(role);
	}

	/**
	 * 是否认证通过（登录）
	 * 
	 * @return
	 */
	public static Boolean isAuthenticated() {
		return SecurityUtils.getSubject() != null && SecurityUtils.getSubject().isAuthenticated();
	}

	/**
	 * 是否记住我
	 * 
	 * @return
	 */
	public static Boolean isRemembered() {
		return SecurityUtils.getSubject() != null && SecurityUtils.getSubject().getPrincipal() != null
				&& SecurityUtils.getSubject().isRemembered();
	}

	/**
	 * 是否游客
	 * 
	 * @return
	 */
	public static Boolean isGuest() {
		return SecurityUtils.getSubject() == null || SecurityUtils.getSubject().getPrincipal() == null;
	}

	/**
	 * 是否用户(无区分用户登录和记住我)
	 * 
	 * @return
	 */
	public static Boolean isUser() {
		return SecurityUtils.getSubject() != null && SecurityUtils.getSubject().getPrincipal() != null;
	}

	/**
	 * 是否是runas运行的用户
	 * 
	 * @return
	 */
	public static Boolean isRunAs() {
		return SecurityUtils.getSubject().isRunAs();
	}

	/**
	 * 切换用户
	 * 
	 * @param user
	 * @return
	 */
	public static Boolean runas(User user) {
		User curr = UserUtils.getUser();
		if (isUser() && !curr.getId().equals(user.getId())) {
			Principal principal = new Principal(user.getId(), user.getLoginName());
			SecurityUtils.getSubject().runAs(principal);
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * 回退到上一个身份
	 */
	public static void releaseRunAs() {
		SecurityUtils.getSubject().releaseRunAs();
	}

	/**
	 * 得到用户菜单
	 * 
	 * @return
	 */
	public static String getUserMenus() {
		User user = getUser();
		String _key = new StringBuilder(Constants.CACHE_MENU_LIST).append(user.getId()).toString();
		return CacheUtils.getSessCache().get(_key);
	}

	/**
	 * 缓存用户菜单
	 * 
	 * @param menus
	 */
	public static void cacheUserMenus(String menus) {
		User user = getUser();
		String _key = new StringBuilder(Constants.CACHE_MENU_LIST).append(user.getId()).toString();
		CacheUtils.getSessCache().put(_key, menus);
	}

	// ----------系统模块相关的缓存-------------------------------------
	public static void removeUser(long userId) {
		CacheUtils.getSessCache().evict(new StringBuilder(Constants.CACHE_USER).append(userId).toString());
	}

	public static void removeUserCache(long userId) {
		CacheUtils.getSessCache().evict(new StringBuilder(Constants.CACHE_USER).append(userId).toString());
		CacheUtils.getSessCache().evict(new StringBuilder(Constants.CACHE_MENU_LIST).append(userId).toString());
		Principal primary = new Principal(userId, null);
		Realm realm = SpringContextHolder.getBean(Realm.class);
		realm.clearCachedAuthorizationInfo(primary);
	}

	public static void removeAllCache() {
		CacheUtils.getSessCache()
				.evict(new StringBuilder(Constants.CACHE_MENU_LIST).append(Constants.CACHE_ALL).toString());
		Realm realm = SpringContextHolder.getBean(Realm.class);
		realm.clearAllCachedAuthorizationInfo();
	}

	// -------------用户相关的缓存(用户可以共享)系统不能随意清空--------------------------------------
	/**
	 * 存储格式 key: SESS#U#0#XXXX ，其中0为用户id,XXXX为实际的缓存key
	 * 
	 * @param key
	 * @param value
	 */
	public static void setAttribute(String key, Object value) {
		String _key = new StringBuilder().append(Constants.CACHE_USER).append(UserUtils.getUser().getId())
				.append(Constants.CACHE_DIV).append(key).toString();
		CacheUtils.getSessCache().put(_key, value);
	}

	/**
	 * 获取用户缓存
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getAttribute(String key) {
		String _key = new StringBuilder().append(Constants.CACHE_USER).append(UserUtils.getUser().getId())
				.append(Constants.CACHE_DIV).append(key).toString();
		Object obj = CacheUtils.getSessCache().get(_key);
		return (T) (obj);
	}

	/**
	 * 获取用户缓存
	 * 
	 * @param key
	 * @return
	 */
	public static void removeAttribute(String key) {
		String _key = new StringBuilder().append(Constants.CACHE_USER).append(UserUtils.getUser().getId())
				.append(Constants.CACHE_DIV).append(key).toString();
		CacheUtils.getSessCache().evict(_key);
	}

	// -------------用户登录--------------------------------------
	public static String userLogin(User user, String sessionId, String loginIp) {
		return systemService.userLogin(user, sessionId, loginIp);
	}

	// -------------用户登录校验--------------------------------------
	/**
	 * 验证成功失败计数,放入缓存 是否是验证码登录
	 * 
	 * @param useruame
	 *            用户名
	 * @param isFail
	 *            计数加1
	 * @return
	 */
	public static boolean isValidateLoginCode(String useruame, boolean isFail) {
		Site site = SiteUtils.getSite();

		// 默认设置的次数
		int dCount = Ints.defaultInteger(site.getAccountLockCount(), Constants.DEFAULT_FAILE_COUNT);

		// 如果设置的次数小于 0
		if (dCount <= 0) {
			return true;
		}

		// 无用户则不用判断
		if (useruame == null) {
			return false;
		}

		// 判断当前用户的错误次数
		String key = new StringBuilder(Constants.LOGIN_VALIDATE_COUNT).append(useruame).toString();
		Integer count = null;
		try {
			count = CacheUtils.getSysCache().get(key);
			if (count == null) {
				count = 0;
			}
			if (isFail) {
				count++;
			}
			return count >= dCount;
		} finally {
			int times = Ints.defaultInteger(site.getAccountLockTime(), Constants.DEFAULT_LOCK_TIME) * 60;
			CacheUtils.getSysCache().put(key, count, times);
		}
	}

	/**
	 * 清除当前用户的登录信息(登录成功后需要删除)
	 * 
	 * @param useruame
	 */
	public static void clearValidateCode(String useruame) {
		String key = new StringBuilder(Constants.LOGIN_VALIDATE_COUNT).append(useruame).toString();
		CacheUtils.getSysCache().delete(key);
	}

	/**
	 * 验证 不管是否成功,清除验证码，会重新生成验证码
	 * 
	 * @param useruame
	 * @param isFail
	 * @return
	 */
	public static void validateLoginCode(String captchaKey, String captcha) {
		try {
			if (!ValidateCodeService.validateCode(captchaKey, captcha)) {
				throw new CaptchaException("验证码错误.");
			}
		} finally {
			ValidateCodeService.clearValidateCode(captchaKey);
		}
	}
}