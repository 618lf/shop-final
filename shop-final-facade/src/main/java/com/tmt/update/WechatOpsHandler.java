package com.tmt.update;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.tmt.Constants;
import com.tmt.common.entity.BaseEntity;
import com.tmt.common.utils.JsonMapper;
import com.tmt.common.utils.StringUtil3;
import com.tmt.system.entity.UpdateData;
import com.tmt.system.entity.User;
import com.tmt.system.entity.UserAccount;
import com.tmt.system.entity.UserWechat;
import com.tmt.system.service.UserServiceFacade;
import com.tmt.wechat.bean.message.MsgHead;
import com.tmt.wechat.bean.user.UserInfo;
import com.tmt.wechat.service.WechatUserService;

/**
 * 消息处理
 * 
 * @author lifeng
 */
public class WechatOpsHandler implements UpdateHandler {

	@Autowired
	private WechatUserService wechatUserService;
	@Autowired
	private UserServiceFacade userService;

	@Override
	public boolean handleFor(Byte moudle) {
		return moudle != null && moudle == Constants.USER_OPS;
	}

	@Override
	public Boolean doHandler(List<UpdateData> datas) {
		return null;
	}

	@Override
	public Boolean doHandler(UpdateData data) {
		if (Constants.USER_OPS == data.getModule()) {
			MsgHead request = JsonMapper.fromJson(data.getMsg(), MsgHead.class);

			// 获取用户信息
			if (request != null) {
				UserAccount account = userService.findByAccount(request.getFromUserName());
				if (account == null) {
					account = newUserAccount(request.getToUserName(), request.getFromUserName());
					UserInfo user = wechatUserService.userinfo(request.getFromUserName());
					if (user != null) {
						User member = new User();
						member.setName(StringUtil3.mb4Replace(user.getNickname(), null));
						member.setSex((user.getSex() != null && "1".equals(user.getSex().toString())) ? BaseEntity.YES
								: BaseEntity.NO);
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
		account.setType((byte) 4);
		UserWechat wechat = new UserWechat();
		wechat.setOpenId(openId);
		wechat.setAppId(appId);
		account.setWechat(wechat);
		return account;
	}
}