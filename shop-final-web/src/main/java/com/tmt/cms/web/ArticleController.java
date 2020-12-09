package com.tmt.cms.web;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.cms.entity.Article;
import com.tmt.cms.service.ArticleServiceFacade;
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
 * 文章管理 管理
 * @author 超级管理员
 * @date 2016-07-20
 */
@Controller("cmsArticleController")
@RequestMapping(value = "${adminPath}/cms/article")
public class ArticleController extends BaseController{
	
	@Autowired
	private ArticleServiceFacade articleService;
	
	
	/**
	 * 进入初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(Article article, Model model){
		return "/cms/ArticleList";
	}
	
	/**
	 * 初始化页面的数据 
	 * @param article
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Article article, Model model, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(article, c);
		return articleService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param article
	 * @param model
	 */
	@RequestMapping("form")
	public String form(Article article, Model model) {
	    if(article != null && !IdGen.isInvalidId(article.getId())) {
		   article = this.articleService.get(article.getId());
		} else {
		   if(article == null) {
			  article = new Article();
		   }
		   article.setId(IdGen.INVALID_ID);
		   article.setIsEnabled(Article.NO);
		   article.setWeight((int)Article.YES);
		}
		model.addAttribute("article", article);
		return "/cms/ArticleForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Article article, Model model, RedirectAttributes redirectAttributes) {
		this.articleService.save(article);
		addMessage(redirectAttributes, StringUtils.format("%s'%s'%s", "修改文章管理", article.getTitle(), "成功"));
		redirectAttributes.addAttribute("id", article.getId());
		return WebUtils.redirectTo(Globals.getAdminPath(), "/cms/article/form");
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
		List<Article> articles = Lists.newArrayList();
		for(Long id: idList) {
			Article article = new Article();
			article.setId(id);
			articles.add(article);
		}
		this.articleService.delete(articles);
		return AjaxResult.success();
	}
	
	//查询条件
	private void initQc(Article article, Criteria c) {
		if(StringUtils.isNotBlank(article.getTitle())) {
           c.andEqualTo("TITLE", article.getTitle());
        }
        if(article.getCategoryId() != null) {
           c.andEqualTo("CATEGORY_ID", article.getCategoryId());
        }
	}
}
