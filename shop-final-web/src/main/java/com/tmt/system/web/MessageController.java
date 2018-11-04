package com.tmt.system.web;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.common.config.Globals;
import com.tmt.common.entity.AjaxResult;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.utils.DateUtil3;
import com.tmt.common.utils.FreemarkerUtils;
import com.tmt.common.utils.StringUtil3;
import com.tmt.common.utils.WebUtils;
import com.tmt.system.entity.ExcelTemplate;
import com.tmt.system.entity.Message;
import com.tmt.system.entity.Message.MessageBox;
import com.tmt.system.entity.User;
import com.tmt.system.utils.ExcelImpUtil;
import com.tmt.system.utils.UserUtils;


/**
 * 站内信管理
 * @author lifeng
 */
@Controller
@RequestMapping(value = "${adminPath}/system/message")
public class MessageController extends BaseMessageController{
	
	/**
	 * 收件箱
	 * @param member
	 * @param model
	 * @return
	 */
	@RequestMapping("inBox")
	public String inBox(){
		return "/system/MessageInList";
	}
	
	/**
	 * 发件箱
	 * @param member
	 * @param model
	 * @return
	 */
	@RequestMapping("outBox")
	public String outBox(){
		return "/system/MessageOutList";
	}
	
	/**
	 * 垃圾箱
	 * @param member
	 * @param model
	 * @return
	 */
	@RequestMapping("trashBox")
	public String trashBox(){
		return "/system/MessageTrashList";
	}
	
	/**
	 * 草稿箱
	 * @param member
	 * @param model
	 * @return
	 */
	@RequestMapping("draftBox")
	public String draftBox(){
		return "/system/MessageDraftList";
	}
	
	/**
	 * 一般是get 来显示表单
	 * @param notice
	 * @param model
	 * @return
	 */
	@RequestMapping("form")
	public String form(Message msg, Model model){
		if( msg != null && !IdGen.isInvalidId(msg.getId())) {
			msg = this.messageService.get(msg.getId());
		} else {
			if(msg == null) {
				msg = new Message();
			}
			msg.setId(IdGen.INVALID_ID);
	    }
		model.addAttribute("msg", msg);
		model.addAttribute("message", null);
		return "/system/MessageSend";
	}
	
	/**
	 * 保存到草稿箱
	 * @param log
	 * @param model
	 * @return
	 */
	@RequestMapping("save")
	public String save(Message message, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes){
		User user = UserUtils.getUser(message.getReceiverUserId());
		message.setMsgBox(MessageBox.DRAFT);
		message.setReceiverUserId(user.getId());
		message.setReceiverUserName(user.getName());
		message.setSendTime(DateUtil3.getTimeStampNow());
		this.messageService.save(message);
		redirectAttributes.addAttribute("id", message.getId());
		return WebUtils.redirectTo(new StringBuilder(Globals.getAdminPath()).append("/system/message/form").toString());
	}
	
	/**
	 * post 提交数据,并重定向到发件箱
	 * @param log
	 * @param model
	 * @return
	 */
	@RequestMapping("replay")
	public String replay(Message message, Model model, RedirectAttributes redirectAttributes){
		Message _message = this.messageService.get(message.getId());
		Message msg = new Message();
		msg.setReceiverUserId(_message.getSendUserId());
		msg.setReceiverUserName(_message.getSendUserName()); 
		model.addAttribute("msg", msg);
		model.addAttribute("message", null);
		return "/system/MessageSend";
	}
	 
	/**
	 * 一般是get 来显示表单
	 * @param notice
	 * @param model
	 * @return
	 */
	@RequestMapping("view")
	public String view(Message msg, Model model){
		msg = this.messageService.get(msg.getId());
		model.addAttribute("msg", msg);
		model.addAttribute("message", null);
		return "/system/MessageView";
	} 
	
	/**
	 * post 提交数据,并重定向到发件箱
	 * @param log
	 * @param model
	 * @return
	 */
	@RequestMapping("send")
	public String send(Message message, HttpServletRequest request,  Model model, RedirectAttributes redirectAttributes){
		User from = UserUtils.getUser();
		message.setSendUserId(from.getId());
		message.setSendUserName(from.getName());
		this.messageService.send(message);
		addMessage(redirectAttributes, StringUtil3.format("%s'%s'%s", "发送站内信", message.getTitle(), "成功"));
		return WebUtils.redirectTo(new StringBuilder(Globals.getAdminPath()).append("/system/message/outBox").toString());
	}
	
	/**
	 * 易发
	 * @return
	 */
	@ResponseBody
	@RequestMapping("easySend")
	public AjaxResult send(Message message){
		User from = UserUtils.getUser();
		message.setSendUserId(from.getId());
		message.setSendUserName(from.getName());
		this.messageService.send(message);
		return AjaxResult.success();
	}

	
	/**
	 * 批量发送邮件
	 * @param idList
	 * @return
	 */
	@ResponseBody
	@RequestMapping("batchSend")
	public AjaxResult batchSend(Long[] idList, Model model, HttpServletResponse respons) {
		if(idList != null && idList.length != 0) {
		   for(Long id: idList) {
			   Message message = this.messageService.get(id);
			   this.messageService.send(message);
		   }
		}
		return AjaxResult.success();
	}
	
	/**
	 * build
	 * @param log
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "build", method = RequestMethod.GET)
	public String build(Model model){
		List<ExcelTemplate> templates = ExcelImpUtil.queryByType("messagex.templates");
		model.addAttribute("templates", templates);
		return "/system/MessageBuilder";
	}
	
	/**
	 * 异步提交
	 * @param templateId
	 * @param message
	 * @param request
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "build", method = RequestMethod.POST)
	public AjaxResult builder(Long templateId, Message message, HttpServletRequest request,  Model model, RedirectAttributes redirectAttributes){
		MultipartFile file = WebUtils.uploadSingleFile(request);
		AjaxResult result = ExcelImpUtil.fetchObjectFromTemplate(templateId, file);
		if( result != null && result.getSuccess()) {
			List<Map<String,Object>> maps = result.getObj();
			if( maps != null && maps.size() != 0 ) {
				for(Map<String,Object> map : maps ) {
					String code = String.valueOf(map.get("memberNo"));
					User user = null;
					if (StringUtil3.isNotBlank(code) && (user = UserUtils.getUserByNo(code)) != null) {
						map.put("user", user);
						String html = FreemarkerUtils.processNoTemplate(message.getContent(), map);
						Message ma = new Message();
						ma.setTitle(message.getTitle());
						ma.setReceiverUserId(user.getId());
						ma.setReceiverUserName(user.getName());
						ma.setContent(html);
						ma.setMsgBox(MessageBox.DRAFT);
						ma.setSendUserId(UserUtils.getUser().getId());
						ma.setSendUserName(UserUtils.getUser().getName());
						ma.setSendTime(DateUtil3.getTimeStampNow());
						this.messageService.save(ma);
					}
				}
			}
			return AjaxResult.success();
		}
		return result;
	}
}