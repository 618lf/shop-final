package com.tmt.system.web;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.common.config.Globals;
import com.tmt.common.entity.AjaxResult;
import com.tmt.common.entity.TreeVO;
import com.tmt.common.persistence.Page;
import com.tmt.common.persistence.PageParameters;
import com.tmt.common.persistence.QueryCondition;
import com.tmt.common.persistence.QueryCondition.Criteria;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.utils.ContextHolderUtils;
import com.tmt.common.utils.CookieUtils;
import com.tmt.common.utils.ExportUtils;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.Maps;
import com.tmt.common.utils.RegexpUtil;
import com.tmt.common.utils.StorageUtils;
import com.tmt.common.utils.StringUtil3;
import com.tmt.common.utils.WebUtils;
import com.tmt.system.entity.Attachment;
import com.tmt.system.entity.AttachmentDir;
import com.tmt.system.entity.User;
import com.tmt.system.utils.UserUtils;

/**
 * 附件管理
 * @author root
 */
@Controller
@RequestMapping(value = "${adminPath}/system/attachment")
public class AttachmentController extends BaseUploadController {
	
	/**
	 * 弹出上传控件
	 * @return
	 */
	@RequestMapping("uploader")
	public String initUploader(String dirId, Model model) {
		User user = UserUtils.getUser();
		model.addAttribute("dirId", StringUtil3.isBlank(dirId)?this.getUserSpaceDir(user).getId():dirId);
		return "/system/AttachmentUploader";
	}
	
	/**
	 * 列表
	 * @return
	 */
	@RequestMapping(value = {"list", ""})
	public String list(Model model, HttpServletRequest request, HttpServletResponse response) {
		String cookie = CookieUtils.getCookie(request, COOKIE_NAME);
		if (cookie != null && RegexpUtil.isNumber(cookie)) {
		    return this.list(Long.parseLong(cookie), model, response);
		}
		return this.list(null, model, response);
	}
	
	/**
	 * 列表
	 * @return
	 */
	@RequestMapping("list/{dir}")
	public String list(@PathVariable Long dir, Model model, HttpServletResponse response) {
		User user = UserUtils.getUser();
		AttachmentDir _target = this.getUserSpaceDir(dir);
		List<AttachmentDir> copys = this.dirService.getUserPaths(user, _target);
		AttachmentDir target = copys.get(copys.size()-1);
		model.addAttribute("paths", copys);
		model.addAttribute("dir", target);
		CookieUtils.setCookie(response, COOKIE_NAME, target.getId().toString(), COOKIE_AGE, new StringBuilder(Globals.adminPath).append("/system/attachment").toString(), Globals.domain, Boolean.FALSE);
		return "/system/AttachmentList";
	}
	
	/**
	 * 数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(Model model) {
		return this.page(1, model);
	}
	
	/**
	 * 数据
	 * @return
	 */
	@ResponseBody
	@RequestMapping("page/{pageNo}")
	public Page page(@PathVariable Integer pageNo, Model model) {
		QueryCondition qc = new QueryCondition();
		qc.setOrderByClause("A.TYPE, A.CREATE_DATE, A.NAME");
		Long dirId = Long.parseLong(WebUtils.getCleanParam("dirId"));
		     dirId = dirId == null?IdGen.ROOT_ID:dirId;
		String name = WebUtils.getSafeParameter("name");
		Criteria c = qc.getCriteria();
		c.andEqualTo("DIR_ID", dirId);
		if(StringUtil3.isNotBlank(name)) {c.andLike("NAME", name);}
		PageParameters param = new PageParameters();
		param.setPageIndex(pageNo);
		param.setPageSize(40);
		Page page = this.attachmentService.queryForPage(qc, param);
		List<Attachment> attachments = page.getData();
		for(Attachment attachment: attachments) {
			attachment.setStorageUrl(StorageUtils.getShowUrl(attachment.getStorageUrl()));
		}
		return page;
	}
	
	/**
	 * 新建文件夹 -- 两个比选条件
	 * name
	 * parentId
	 * @return
	 */
	@ResponseBody
	@RequestMapping("save/dir")
	public AjaxResult save(AttachmentDir dir) {
		dirService.save(dir);
		return AjaxResult.success();
	}
	
	/**
	 * 查看
	 * @return
	 */
	@RequestMapping("detail/{id}")
	public String detail(@PathVariable String id) {
		return "/system/AttachmentDetail";
	}
	
	/**
	 * 删除文件
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete")
	public AjaxResult delete(Long[] idList) {
		List<AttachmentDir> dirs = Lists.newArrayList();
		List<Attachment> attachments = Lists.newArrayList();
		for(Long id: idList) {
			AttachmentDir dir = this.dirService.get(id);
			if(dir != null ) {
				if(dir.isSystemDir()) {
				   return AjaxResult.error("不能删除系统目录（用户目录等）！");
				}
				dirs.add(dir);
			} else {
				Attachment attachment = this.attachmentService.get(id);
				attachments.add(attachment);
			}
		}
		Boolean bFlag = this.dirService.delete(dirs);
		this.attachmentService.delete(attachments);
		if(bFlag) {
		   return AjaxResult.success();
		}
		return AjaxResult.error("要删除的文件夹包含子文件夹或文件或不能删除系统目录（用户目录）！");
	}
	
	/**
	 * 删除文件
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("rename/{id}")
	public AjaxResult rename(@PathVariable Long id, String name) {
		AttachmentDir dir = this.dirService.get(id);
		if(dir != null ) {
			if( dir.isSystemDir() ) {
				return AjaxResult.error("不能修改系统目录(用户目录等)");
			}
			this.dirService.rename(dir, name);
			return AjaxResult.success();
		} 
		Attachment attachment = this.attachmentService.get(id);
		if(attachment != null) {
			attachment.setName(name);
			this.attachmentService.rename(attachment);
			return AjaxResult.success();
		}
		return AjaxResult.success();
	}
	
	/**
	 * 删除文件
	 * @param id
	 * @return
	 */
	@ResponseBody
	@RequestMapping("move")
	public AjaxResult move(Long[] idList, Long moveto) {
		AttachmentDir dir = IdGen.ROOT_ID == moveto?AttachmentDir.getRootDir():this.dirService.get(moveto); 
		if(dir != null) {
			for(Long id: idList) {
				Attachment attachment = this.attachmentService.get(id);
				if(attachment != null) {
					this.attachmentService.moveto(attachment, dir);
				} else if(!moveto.equals(id) && dir.getParentIds().indexOf(","+id+",") ==-1 ){
					AttachmentDir _dir = this.dirService.get(id);
					if(_dir != null && _dir.isSystemDir()) {
						return AjaxResult.error("不能移动系统目录(用户目录)");
					}
					if( dir != null && !_dir.isSystemDir()) {
						this.dirService.moveto(_dir, dir);
					}
				} else {
					return AjaxResult.error("移动文件夹失败");
				}
			}
			return AjaxResult.success();
		}
		return AjaxResult.error("选择的文件夹不存在！");
	}
	
	/**
	 * 文件夹树
	 * @param extId
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("treeSelect")
	public List<Map<String, Object>> treeSelect(@RequestParam(required=false)String extId, HttpServletResponse response) {
		User user = UserUtils.getUser();
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<TreeVO> trees = this.dirService.findTreeList(user);
		for (int i=0; i<trees.size(); i++){
			TreeVO e = trees.get(i);
			if( i == 0 && !UserUtils.getUser().isRoot()) {
				e.setTreeName("我的空间");
			}
			if (extId == null || (extId!=null && !extId.equals(e.getId().toString()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParent());
				map.put("name", e.getTreeName());
				mapList.add(map);
			}
		}
		return mapList;
	}
	
	/**
	 * 
	 * 下载（只能下载文件，不支持下载文件夹）
	 * 先阶段智支持下载第一个文件
	 * 
	 * @param idList
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "download")
	public void download(HttpServletRequest request, HttpServletResponse response) {
		List<Attachment> attachments = WebUtils.fetchItemsFromRequest(request, Attachment.class, WebUtils.DEFAULT_EXPORT_PARAM);
		if(attachments != null && !attachments.isEmpty()) {
		   Attachment attachment = this.attachmentService.get(attachments.get(0).getId());
			if(attachment != null ) {
				File tempfile = ContextHolderUtils.getTempFile(StorageUtils.download(attachment.getStorageUrl()));
				if(tempfile != null) {
				   ExportUtils.downloadFile(tempfile, attachment.getName(), response, Globals.DEFAULT_ENCODING, Boolean.TRUE);
				}
			}
		}
	}
	
	//-----------------在线选择------------------------
	@RequestMapping(value = "select")
	public String select(Model model, HttpServletRequest request, HttpServletResponse response) {
		String cookie = CookieUtils.getCookie(request, COOKIE_NAME);
		if (cookie != null && RegexpUtil.isNumber(cookie)) {
		   return this.select(Long.parseLong(cookie), model, response);
		}
		return this.select(null, model, response);
	}
	
	/**
	 * 列表
	 * @return
	 */
	@RequestMapping("select/{dir}")
	public String select(@PathVariable Long dir, Model model, HttpServletResponse response) {
		this.list(dir, model, response);
		return "/system/AttachmentSelect";
	}
	
	/**
	 * 返回分享的地址
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "share")
	public AjaxResult share(Long id) {
		Attachment attachment = this.attachmentService.get(id);
		if(attachment != null) {
		   return AjaxResult.success(attachment.getStorageUrl());
		}
		return AjaxResult.error("资源已被删除！");
	}
}
