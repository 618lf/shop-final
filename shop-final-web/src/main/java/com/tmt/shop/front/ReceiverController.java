package com.tmt.shop.front;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.Lists;
import com.tmt.shop.entity.Receiver;
import com.tmt.shop.service.ReceiverServiceFacade;
import com.tmt.system.entity.User;
import com.tmt.system.utils.UserUtils;

/**
 * 收货地址
 * 
 * @author root
 */
@Controller("frontReceiverController")
@RequestMapping(value = "${frontPath}/member/receiver")
public class ReceiverController {

	@Autowired
	private ReceiverServiceFacade receiverService;

	/**
	 * 表单
	 * 
	 * @param receiver
	 * @param model
	 */
	@RequestMapping("add.html")
	public String add(String to, Model model) {
		Receiver receiver = new Receiver();
		receiver.setSex(Receiver.NO);
		receiver.setId(IdGen.INVALID_ID);
		model.addAttribute("to", to);
		model.addAttribute("receiver", receiver);
		return "/front/member/ReceiverView";
	}

	/**
	 * 表单
	 * 
	 * @param receiver
	 * @param model
	 */
	@RequestMapping("view/{id}.html")
	public String view(@PathVariable Long id, String to, Model model) {
		Receiver receiver = this.receiverService.get(id);
		if (receiver == null) {
			receiver = new Receiver();
			receiver.setId(IdGen.INVALID_ID);
			receiver.setSex(Receiver.NO);
		}
		model.addAttribute("to", to);
		model.addAttribute("receiver", receiver);
		return "/front/member/ReceiverView";
	}

	/**
	 * 保存
	 * 
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("save.json")
	public AjaxResult save(Receiver receiver, Model model, RedirectAttributes redirectAttributes) {
		User user = UserUtils.getUser();
		receiver.userOptions(user);
		this.receiverService.save(receiver);
		return AjaxResult.success(receiver);
	}

	/**
	 * 收货地址列表
	 * 
	 * @param receiver
	 * @param model
	 */
	@RequestMapping("list.html")
	public String list() {
		return "/front/member/ReceiverList";
	}

	/**
	 * 选择
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("select.html")
	public String select(String to, Model model) {
		model.addAttribute("to", to);
		return "/front/member/ReceiverSelect";
	}

	/**
	 * 加载数据
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("load.json")
	public AjaxResult load() {
		User user = UserUtils.getUser();
		List<Receiver> receivers = receiverService.queryUserReceivers(user);
		return AjaxResult.success(receivers);
	}

	/**
	 * 删除地址
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete/{id}.json")
	public AjaxResult delete(@PathVariable Long id) {
		List<Receiver> receiverLst = Lists.newArrayList();
		Receiver receiver = new Receiver();
		receiver.setId(id);
		receiverLst.add(receiver);
		this.receiverService.delete(receiverLst);
		return AjaxResult.success(receiver);
	}

	/**
	 * 设置默认地址
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("def/{id}.json")
	public AjaxResult def(@PathVariable Long id) {
		User user = UserUtils.getUser();
		Receiver receiver = new Receiver();
		receiver.setId(id);
		receiver.setCreateId(user.getId());
		this.receiverService.def(receiver);
		return AjaxResult.success(receiver);
	}

	/**
	 * 得到默认的地址
	 * 
	 * @return
	 */
	@ResponseBody
	@RequestMapping("get.json")
	public AjaxResult get() {
		User user = UserUtils.getUser();
		return AjaxResult.success(this.receiverService.queryUserDefaultReceiver(user));
	}

	/**
	 * 发送验证码
	 * 
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("code/send.json")
	public AjaxResult sendVerification(String phone) {
		// User user = UserUtils.getUser();
		int res = 4001;// SmsUtils.sendVerification(user, phone);
		if (res == 1) {
			return AjaxResult.success();
		} else if (res == 4002) {
			return AjaxResult.error("10分钟之内，只能发送3个验证码！");
		} else if (res == 4001) {
			return AjaxResult.error("手机号码有误！");
		} else {
			return AjaxResult.error("其他错误，请重新获取！");
		}
	}

	/**
	 * 验证验证码
	 * 
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("code/verify.json")
	public AjaxResult verifyCode(String phone, Integer code) {
		// User user = UserUtils.getUser();
		int res = 4002;// SmsUtils.verificationCode(user, phone, code);
		if (res == 1) {
			return AjaxResult.success();
		} else if (res == 4002) {
			return AjaxResult.error("手机号无效");
		} else if (res == 4001) {
			return AjaxResult.error("验证码已失效");
		} else {
			return AjaxResult.error("验证码无效");
		}
	}
}