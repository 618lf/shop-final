package com.tmt.wechat.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.core.entity.Result;
import com.tmt.core.utils.JaxbMapper;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;
import com.tmt.core.web.BaseController;
import com.tmt.wechat.bean.message.MsgHead;
import com.tmt.wechat.entity.App;
import com.tmt.wechat.service.WechatService;
import com.tmt.wechat.utils.WechatUtils;

/**
 * 微信公众号服务
 * 需要根据绑定的域名返回不同的公众号
 * @author root
 */
@Controller("frontWechatController")
@RequestMapping(value = "${frontPath}/wechat")
public class WechatController extends BaseController {

	/**
	 * 微信服务
	 */
	@Autowired
	private WechatService wechatService;
	
	/**
	 * 用于测试是否可以和微信接入
	 * 适合，多微信接入
	 * 根据请求的域名判断适配的微信APP
	 */
	@ResponseBody
	@RequestMapping(value = "/access.json", method = RequestMethod.GET)
	public String wechatGet(String signature, String timestamp, String nonce, String echostr, HttpServletRequest request) {
		
		// 基本的验证
		if (StringUtils.isBlank(signature)
				|| StringUtils.isBlank(timestamp) || StringUtils.isBlank(nonce)) {
			return "ERROR";
		}
		
		// 请求的域名
		String domain = request.getServerName();
		
	    // 根据 domain 找到具体的app
		App app = WechatUtils.getDomainApp(domain);
		
		/**
		 * 验证正确性
		 */
		if (wechatService.bind(app).checkSignature(timestamp, nonce, signature)) {
			return echostr;
		}
		return "ERROR";
	}
	
	/**
	 * 微信端调用的本地接口
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/access.json", method = RequestMethod.POST)
	public String wechatPost(@RequestBody String requestBody, String signature, String timestamp, String nonce,
			HttpServletRequest request, HttpServletResponse response) {
		
		// 基本的验证
		if (StringUtils.isBlank(signature)
				|| StringUtils.isBlank(timestamp) || StringUtils.isBlank(nonce)) {
			return "ERROR";
		}
		
		// 请求的域名
		String domain = request.getServerName();
		
	    // 根据 domain 找到具体的app
		App app = WechatUtils.getDomainApp(domain);
				
		/**
		 * 可能被伪造的请求
		 */
		if (!wechatService.bind(app).checkSignature(timestamp, nonce, signature)) {
			return "ERROR";
		}
		
		// 现在只支持明文
		MsgHead resp = this.wechatService.bind(app).onMessage(requestBody);
		if (resp != null) {
			return JaxbMapper.toXml(resp);
		}
		return "SUCCESS";
	}
	
	/**
	 * 请求签名
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/url/sign.json", method = RequestMethod.POST)
	public Result urlSign(String url, HttpServletRequest request) {
		if (WebUtils.isWeixin(request) && WebUtils.isAjax(request)) {
			String _url = WebUtils.decodeRequestString(request, url);
			
			// 请求的域名
			String domain = request.getServerName();
			
		    // 根据 domain 找到具体的app
			App app = WechatUtils.getDomainApp(domain);
			
			// 获得签名
			return Result.success(wechatService.bind(app).getUrlSign(_url));
		}
		return Result.error("不需要签名");
	}
}