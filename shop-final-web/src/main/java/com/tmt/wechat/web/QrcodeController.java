package com.tmt.wechat.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.common.config.Globals;
import com.tmt.common.entity.AjaxResult;
import com.tmt.common.persistence.Page;
import com.tmt.common.persistence.PageParameters;
import com.tmt.common.persistence.QueryCondition;
import com.tmt.common.persistence.QueryCondition.Criteria;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.StringUtil3;
import com.tmt.common.utils.WebUtils;
import com.tmt.common.web.BaseController;
import com.tmt.system.utils.UserUtils;
import com.tmt.wechat.bean.qrcode.QrCodeTicket;
import com.tmt.wechat.entity.App;
import com.tmt.wechat.entity.Qrcode;
import com.tmt.wechat.service.AppServiceFacade;
import com.tmt.wechat.service.QrcodeServiceFacade;
import com.tmt.wechat.service.WechatOptionService;

/**
 * 二维码 管理
 * @author 超级管理员
 * @date 2016-10-01
 */
@Controller("wechatQrcodeController")
@RequestMapping(value = "${adminPath}/wechat/qrcode")
public class QrcodeController extends BaseController{
	
	@Autowired
	private QrcodeServiceFacade qrcodeService;
	@Autowired
	private AppServiceFacade appService;
	@Autowired
	private WechatOptionService wechatService;
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Qrcode qrcode, Model model){
		return "/wechat/QrcodeList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param qrcode
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Qrcode qrcode, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(qrcode, c);
		qc.setOrderByClause(param.orderBy("UPDATE_DATE DESC"));
		return qrcodeService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param qrcode
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Qrcode qrcode, Model model) {
	    if(qrcode != null && !IdGen.isInvalidId(qrcode.getId())) {
		   qrcode = this.qrcodeService.get(qrcode.getId());
		   qrcode.setAppName(appService.get(qrcode.getAppId()).getName());
		} else {
		   if(qrcode == null) {
			  qrcode = new Qrcode();
		   }
		   qrcode.setId(IdGen.INVALID_ID);
		   qrcode.setType(Qrcode.NO);
		   qrcode.setActionName("QR_LIMIT_STR_SCENE"); // 只需要支持这种形式
		}
		model.addAttribute("qrcode", qrcode);
		return "/wechat/QrcodeForm";
	}
	
	/**
	 * 校验编号
	 * @param user
	 * @return
	 */
	@ResponseBody
	@RequestMapping("check/sceneStr")
	public Boolean checkSceneStr(Qrcode qrcode){
		int iCount = this.qrcodeService.checkSceneStr(qrcode);
		if (iCount > 0) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Qrcode qrcode, Model model, RedirectAttributes redirectAttributes) {
		qrcode.userOptions(UserUtils.getUser());
		// 第一次才创建
		if (IdGen.isInvalidId(qrcode.getId())) {
			String msg = sendQrcode(qrcode);
			if (msg != null) {
				addMessage(redirectAttributes, StringUtil3.format("%s'%s'%s", "创建二维码", qrcode.getAppId(), "失败"));
				return WebUtils.redirectTo(Globals.getAdminPath(), "/wechat/qrcode/form");
			}
		} 
		this.qrcodeService.save(qrcode);
		addMessage(redirectAttributes, StringUtil3.format("%s'%s'%s", "创建二维码", qrcode.getAppId(), "成功"));
		redirectAttributes.addAttribute("id", qrcode.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/wechat/qrcode/form");
	}
	
	/**
	 * 发送并创建二维码
	 */
	private String sendQrcode(Qrcode qrcode) {
		App app = appService.get(qrcode.getAppId());
		QrCodeTicket ticket = wechatService.bind(app).qrcodeCreateFinal(qrcode.getSceneStr());
		if (ticket != null && StringUtil3.isNotBlank(ticket.getTicket())) {
			qrcode.setTicket(ticket.getTicket());
			qrcode.setImage(wechatService.bind(app).qrcodeShowUrl(ticket.getTicket()));
			return null;
		}
		return ticket.getErrmsg();
	}
	
	/**
	 * 删除
	 * @param idList
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete")
	public AjaxResult delete(Long[] idList , Model model, HttpServletResponse response) {
		List<Qrcode> qrcodes = Lists.newArrayList();
		for(Long id: idList) {
			Qrcode qrcode = new Qrcode();
			qrcode.setId(id);
			qrcodes.add(qrcode);
		}
		this.qrcodeService.delete(qrcodes);
		return AjaxResult.success();
	}

	//查询条件
	private void initQc(Qrcode qrcode, Criteria c) {
        if(StringUtil3.isNotBlank(qrcode.getAppId())) {
           c.andEqualTo("APP_ID", qrcode.getAppId());
        }
	}
}