package com.tmt.bbs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.bbs.entity.ReplyHit;
import com.tmt.bbs.service.ReplyHitServiceFacade;
import com.tmt.core.persistence.Page;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;
import com.tmt.core.persistence.QueryCondition.Criteria;
import com.tmt.core.utils.StringUtils;
import com.tmt.core.web.BaseController;

/**
 * 回复点赞 管理
 * @author 超级管理员
 * @date 2017-04-12
 */
@Controller("bbsReplyHitController")
@RequestMapping(value = "${adminPath}/bbs/reply/hit")
public class ReplyHitController extends BaseController{
	
	@Autowired
	private ReplyHitServiceFacade replyHitService;
	
	/**
	 * 列表初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(ReplyHit replyHit, Model model){
		model.addAttribute("replyId", replyHit.getReplyId());
		return "/bbs/ReplyHitList";
	}
	
	/**
	 * 列表页面的数据 
	 * @param replyHit
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(ReplyHit replyHit, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(replyHit, c);
		qc.setOrderByClause("CREATE_DATE DESC");
		return replyHitService.queryForPage(qc, param);
	}
	
	//查询条件
	private void initQc(ReplyHit replyHit, Criteria c) {
		c.andEqualTo("REPLY_ID", replyHit.getReplyId());
        if(StringUtils.isNotBlank(replyHit.getCreateName())) {
           c.andEqualTo("CREATE_NAME", replyHit.getCreateName());
        }
	}
}
