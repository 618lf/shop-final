package com.tmt.wechat.web;

import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
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
import com.tmt.common.utils.JsonMapper;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.StorageUtils;
import com.tmt.common.utils.StringUtil3;
import com.tmt.common.utils.WebUtils;
import com.tmt.common.web.BaseController;
import com.tmt.wechat.bean.base.Constants.MediaType;
import com.tmt.wechat.bean.material.Material;
import com.tmt.wechat.bean.material.MaterialUploadResult;
import com.tmt.wechat.entity.App;
import com.tmt.wechat.entity.MetaImage;
import com.tmt.wechat.service.AppServiceFacade;
import com.tmt.wechat.service.MetaImageServiceFacade;
import com.tmt.wechat.service.WechatOptionService;
import com.tmt.wechat.utils.WechatUtils;

/**
 * 图片 管理
 * @author 超级管理员
 * @date 2017-01-12
 */
@Controller("wechatMetaImageController")
@RequestMapping(value = "${adminPath}/wechat/meta/image")
public class MetaImageController extends BaseController{
	
	@Autowired
	private MetaImageServiceFacade metaImageService;
	@Autowired
	private AppServiceFacade appService;
	@Autowired
	private WechatOptionService wechatService;
	
	/**
	 * 列表初始化页面
	 * @param model
	 */
	@RequestMapping("list")
	public String list(MetaImage metaImage, Model model){
		return "/wechat/MetaImageList";
	}
	
	/**
	 * 列表页面的数据 
	 * @param metaImage
	 * @param model
	 * @param page
	 * @return Page Json
	 */
	@ResponseBody
	@RequestMapping("page")
	public Page page(MetaImage metaImage, Page page){
        QueryCondition qc = new QueryCondition();
		PageParameters param = page.getParam();
		Criteria c = qc.getCriteria();
		             this.initQc(metaImage, c);
		return metaImageService.queryForPage(qc, param);
	}
	
	/**
	 * 表单
	 * @param metaImage
	 * @param model
	 */
	@RequestMapping("form")
	public String form(MetaImage metaImage, Model model) {
	    if (metaImage != null && !IdGen.isInvalidId(metaImage.getId())) {
		    metaImage = this.metaImageService.get(metaImage.getId());
		} else {
		    if(metaImage == null) {
			   metaImage = new MetaImage();
		    }
		    metaImage.setId(IdGen.INVALID_ID);
		}
	    MaterialUploadResult material = JsonMapper.fromJson(metaImage.getContent(), MaterialUploadResult.class);
	    if (material == null) {
	    	material = new MaterialUploadResult();
	    }
	    metaImage.setMaterial(material);
		model.addAttribute("metaImage", metaImage);
		model.addAttribute("apps", appService.getAll());
		return "/wechat/MetaImageForm";
	}
	
	/**
	 * 保存
	 * @param category
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(MetaImage metaImage, Model model, RedirectAttributes redirectAttributes) {
		// 判断是否修改图片
		if ((IdGen.isInvalidId(metaImage.getId()) && StringUtil3.isNotBlank(metaImage.getImage()))
				|| (metaImage.getIsUpdate() != null 
				&& metaImage.getIsUpdate() == 1 && StringUtil3.isNotBlank(metaImage.getImage()))) {
			App app = WechatUtils.get(metaImage.getAppId());
			AjaxResult result = this.upload(app.getId(), metaImage.getImage());
			metaImage.setContent((String)result.getObj());
		}
		this.metaImageService.save(metaImage);
		addMessage(redirectAttributes, StringUtil3.format("%s'%s'%s", "修改图片", metaImage.getKeyword(), "成功"));
		redirectAttributes.addAttribute("id", metaImage.getId());
		return WebUtils.redirectTo(Globals.adminPath, "/wechat/meta/image/form");
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
	public AjaxResult delete(Long[] idList) {
		List<MetaImage> metaImages = Lists.newArrayList();
		for(Long id: idList) {
			MetaImage metaImage = new MetaImage();
			metaImage.setId(id);
			metaImages.add(metaImage);
		}
		this.metaImageService.delete(metaImages);
		return AjaxResult.success();
	}
	
	/**
	 * 默认是临时
	 * 上传图片到服务器
	 * 返回上传的结果
	 * @return
	 */
	@ResponseBody
	@RequestMapping("upload")
	public AjaxResult upload(String appId, String img) {
		if (StringUtil3.isNotBlank(img)) {
			return AjaxResult.error("请选择图片");
		}
		InputStream is = StorageUtils.download(img);
		try{
			App app = WechatUtils.get(appId);
			Material material = new Material(); material.setUpload(is);
			MaterialUploadResult media = wechatService.bind(app).materialUpload(MediaType.image, material);
			media.setUrl(img);
			return AjaxResult.success(JsonMapper.toJson(media));
		}finally {
			IOUtils.closeQuietly(is);
		}
	}

	//查询条件
	private void initQc(MetaImage metaImage, Criteria c) {
		if(StringUtil3.isNotBlank(metaImage.getKeyword())) {
           c.andEqualTo("KEYWORD", metaImage.getKeyword());
        }
	}
}