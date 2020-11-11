package com.tmt.wechat.update;

import java.util.List;

import com.tmt.core.entity.BaseEntity;
import com.tmt.core.utils.JsonMapper;
import com.tmt.core.utils.SpringContextHolder;
import com.tmt.core.utils.StringUtils;
import com.tmt.system.entity.UpdateData;
import com.tmt.system.entity.User;
import com.tmt.system.entity.UserAccount;
import com.tmt.system.entity.UserWechat;
import com.tmt.system.service.UserServiceFacade;
import com.tmt.update.impl.AbstractUpdateHandler;
import com.tmt.wechat.bean.message.MsgHead;
import com.tmt.wechat.bean.user.UserInfo;
import com.tmt.wechat.service.WechatUserService;

/**
 * 消息处理
 * @author lifeng
 */
public class WechatOpsHandler extends AbstractUpdateHandler {

	WechatUserService wechatUserService;
	UserServiceFacade userService;
	public WechatOpsHandler() {
		wechatUserService = SpringContextHolder.getBean(WechatUserService.class);
	}
	
	@Override
	public Boolean doHandler(List<UpdateData> datas) {
		return null;
	}

	@Override
	protected Boolean doInnerHandler(UpdateData data) {
		if (WechatModule.USER_OPS == data.getModule()) {
			MsgHead request = JsonMapper.fromJson(data.getMsg(), MsgHead.class);
			
			// 获取用户信息
			if (request != null) {
				UserAccount account = userService.findByAccount(request.getFromUserName());
				if (account == null) {
					account = newUserAccount(request.getToUserName(), request.getFromUserName());
					UserInfo user = wechatUserService.userinfo(request.getFromUserName());
					if (user != null) {
						User member = new User();
						member.setName(StringUtils.mb4Replace(user.getNickname(), null));
						member.setSex((user.getSex() != null && "1".equals(user.getSex().toString()))?BaseEntity.YES:BaseEntity.NO);
				        member.setProvince(user.getProvince());
						member.setCity(user.getCity());
				        member.setHeadimg(user.getHeadimgurl());
				        account.setUser(member);
				        this.userService.registerByAccount(account);
					}
				}
			}
			return Boolean.TRUE;
		}
		return null;
	}
	
	private UserAccount newUserAccount(String appId, String openId) {
		UserAccount account = new UserAccount();
		account.setType((byte)4);
		UserWechat wechat = new UserWechat();
		wechat.setOpenId(openId);
		wechat.setAppId(appId);
		account.setWechat(wechat);
		return account;
	}
}