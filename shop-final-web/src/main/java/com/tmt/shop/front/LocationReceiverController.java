package com.tmt.shop.front;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.entity.AjaxResult;
import com.tmt.core.utils.StringUtils;
import com.tmt.shop.entity.Receiver;
import com.tmt.shop.service.ReceiverServiceFacade;
import com.tmt.system.entity.User;
import com.tmt.system.utils.UserUtils;

/**
 * 定位服务
 * @author root
 */
@Controller("frontLocationReceiverController")
@RequestMapping(value = "${frontPath}/location")
public class LocationReceiverController {

	@Autowired
	private ReceiverServiceFacade receiverService;
	
	/**
	 * 默认地址
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/def.json")
	public AjaxResult def() {
		if (UserUtils.isUser() && UserUtils.getUser() != null) {
			User user = UserUtils.getUser();
			// 用户默认的收货地址
			Receiver receiver = receiverService.queryUserDefaultReceiver(user);
			if (receiver != null && StringUtils.isNoneBlank(receiver.getAddress())) {
				return AjaxResult.success(StringUtils.substring(receiver.getAddress(), 0, 10));
			}
			return AjaxResult.error("不支持自动定位");
		}
		return AjaxResult.error("不支持自动定位");
	}
}