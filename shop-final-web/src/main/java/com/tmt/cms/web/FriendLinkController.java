package com.tmt.cms.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.cms.entity.FriendLink;
import com.tmt.cms.service.FriendLinkServiceFacade;
import com.tmt.core.config.Globals;
import com.tmt.core.entity.AjaxResult;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.persistence.incrementer.IdGen;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.utils.WebUtils;
import com.tmt.core.web.BaseController;

/**
 * 友情链接 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
@Controller("cmsFriendLinkController")
@RequestMapping(value = "${adminPath}/cms/friendLink")
public class FriendLinkController extends BaseController{
	
	@Autowired
	private FriendLinkServiceFacade friendLinkService;
	
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(FriendLink friendLink, Model model){
		return "/cms/FriendLinkList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param friendLink
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(FriendLink friendLink, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(friendLink, c);
		return friendLinkService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param friendLink
	 * @param model
	 */
	@RequestMapping("form")
	public String form(FriendLink friendLink, Model model) {
	    if(friendLink != null && !IdGen.isInvalidId(friendLink.getId())) {
		   friendLink = this.friendLinkService.get(friendLink.getId());
		} else {
		   if(friendLink == null) {
			  friendLink = new FriendLink();
		   }
		   friendLink.setId(IdGen.INVALID_ID);
		}
		model.addAttribute("friendLink", friendLink);
		return "/cms/FriendLinkForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(FriendLink friendLink, Model model, RedirectAttributes redirectAttributes) {
		this.friendLinkService.save(friendLink);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改友情链接", friendLink.getName(), "成功"));
		redirectAttributes.addAttribute("id", friendLink.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/cms/friendLink/form");
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
		List<FriendLink> friendLinks = Lists.newArrayList();
		for(Long id: idList) {
			FriendLink friendLink = new FriendLink();
			friendLink.setId(id);
			friendLinks.add(friendLink);
		}
		this.friendLinkService.delete(friendLinks);
		return AjaxResult.success();
	}
	


	//查询条件
	private void initQc(FriendLink friendLink, Criteria c) {
        if(StringUtils.isNotBlank(friendLink.getName())) {
           c.andEqualTo("NAME", friendLink.getName());
        }
        if(StringUtils.isNotBlank(friendLink.getLogo())) {
           c.andEqualTo("LOGO", friendLink.getLogo());
        }
        if(friendLink.getOrders() != null) {
           c.andEqualTo("ORDERS", friendLink.getOrders());
        }
        if(StringUtils.isNotBlank(friendLink.getUrl())) {
           c.andEqualTo("URL", friendLink.getUrl());
        }
        if(friendLink.getCreateDate() != null) {
           c.andDateEqualTo("CREATE_DATE", friendLink.getCreateDate());
        }
	}
	
}
