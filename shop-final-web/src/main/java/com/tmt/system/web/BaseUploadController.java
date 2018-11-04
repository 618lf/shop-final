package com.tmt.system.web;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.tmt.common.entity.AjaxResult;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.utils.ContextHolderUtils;
import com.tmt.common.utils.DateUtil3;
import com.tmt.common.utils.FreemarkerUtils;
import com.tmt.common.utils.JsonMapper;
import com.tmt.common.utils.Maps;
import com.tmt.common.utils.StringUtil3;
import com.tmt.common.utils.WebUtils;
import com.tmt.system.entity.AttachmentDir;
import com.tmt.system.entity.Site;
import com.tmt.system.entity.User;
import com.tmt.system.service.AttachmentDirServiceFacade;
import com.tmt.system.service.AttachmentServiceFacade;
import com.tmt.system.utils.SiteUtils;
import com.tmt.system.utils.UserUtils;
import com.tmt.system.web.UploadConfig.Action;

/**
 * 基础的上传服务
 * @author root
 */
public class BaseUploadController {

	protected static String  COOKIE_NAME = "A_D";
	protected static Integer COOKIE_AGE = 7*24*60*60;
	protected static String  DIR_HOME = "D_HOME";
		
	@Autowired
	protected AttachmentServiceFacade attachmentService;
	@Autowired
	protected AttachmentDirServiceFacade dirService;
	protected UploadConfig config = UploadConfig.getInstance();
	
	/**
	 * 分片上传之后的合并服务
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("doMerge")
	@SuppressWarnings("unchecked")
	public AjaxResult doMerge(String guid, String fileName, long size, HttpServletRequest request) {
		if(UserUtils.isUser()) {
		   Action _action = Action.valueBy("uploadfile");
		   Map<String, Object> config = this.getConfig(_action);
		   String suffix = StringUtil3.substringAfterLast(fileName, ".").toLowerCase();
			      suffix = "." + StringUtil3.defaultString(StringUtil3.trimToNull(suffix), "jpg").toLowerCase();
	       if(!((List<String>) config.get("allowFiles")).contains(suffix)) {
			  return AjaxResult.error("不允许的文件类型");
		   }
		   String path = this.pathFormat((String)config.get("pathFormat"), suffix);
		   Long dir = Long.parseLong(request.getParameter("dirId"));
		   AttachmentDir _dir = this.getUserSpaceDir(dir);
		   String url = attachmentService.mergeChunks(guid, path, _dir, fileName, size, UserUtils.getUser());
		   Map<String, String> rMap = Maps.newHashMap();
		   rMap.put("url", url);
		   rMap.put("title", StringUtil3.substringAfterLast(url, "/"));
		   return AjaxResult.success(rMap);
		}
		return AjaxResult.error("无操作权限");
	} 
	
	/**
	 * 新版的上传服务
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping("doUpload")
	public String doUpload(String action, String callback, HttpServletRequest request) {
		if(UserUtils.isUser()) {
			AjaxResult result = this.doAction(action, request);
			Map<String,Object> _r = result.getObj();
			if(_r == null) { _r = Maps.newHashMap();}
			if(result.getSuccess()) {
			   _r.put("state", "SUCCESS");
			} else {
			   _r.put("state", result.getMsg());
			}
			return JsonMapper.toJson(_r);
		}
		Map<String, Object> _r = Maps.newHashMap();
		 _r.put("state", "权限不足");
		return JsonMapper.toJson(_r);
	} 
	
	/**
	 * 提供服务
	 * @param action
	 * @return
	 */
	public AjaxResult doAction(String action, HttpServletRequest request) {
		Action _action = Action.valueBy(action);
		if( _action == null ) {
			return AjaxResult.error("无效的请求");
		}
		switch (_action) {
		    case config: return AjaxResult.success(this.getConfig());
		    case uploadimage:
			case uploadscrawl:
			case uploadvideo:
			case uploadfile:
		    	return this.doUpload(_action, request);
			default:break;
		}
		return AjaxResult.error("不支持的操作");
	}
	
	/**
	 * 上传文件
	 * @param request
	 * @return
	 */
	public AjaxResult doUpload(Action action, HttpServletRequest request) {
		Map<String, Object> config = this.getConfig(action);
		if ("true".equals(config.get("isBase64"))) {
			return this.uploadContent(action, request);
		} else {
			return this.uploadFile(action, request);
		}
	}
	
	/**
	 * 传内容
	 * @param action
	 * @param request
	 * @return
	 */
	private AjaxResult uploadContent(Action action, HttpServletRequest request) {
		Map<String, Object> config = this.getConfig(action);
		String filename = (String) config.get("filename");
		String fieldName = (String) config.get("fieldName");
		String content = request.getParameter(fieldName);
		if(StringUtil3.isNotBlank(content)) {
			String suffix = WebUtils.getCleanParam("suffix");
			       suffix = StringUtil3.isBlank(suffix)?".jpg":suffix;
			String original = WebUtils.getCleanParam(filename);
			       original = StringUtil3.isBlank(original)?"截图":original;
			byte[] data = Base64.decodeBase64(content);
			long maxSize = ((Integer) config.get("maxSize")).longValue();
			long length = data.length;
			if ( length > maxSize) {
				return AjaxResult.error("文件大小超出限制");
			}
			String path = this.pathFormat((String)config.get("pathFormat"), suffix);
			Long dir = Long.parseLong(request.getParameter("dirId"));
			AttachmentDir _dir = this.getUserSpaceDir(dir);
			String url = attachmentService.storage(data, path, _dir, original, length, UserUtils.getUser());
			if (StringUtil3.isBlank(url)) {
				return AjaxResult.error("上传失败");
			}
			Map<String, Object> result = Maps.newHashMap();
			result.put("url", url);
			result.put("type", suffix);
			result.put("original", original);
			result.put("size", length);
			result.put("title", StringUtil3.substringAfterLast(url, "/"));
		    return AjaxResult.success(result);
		}
		return AjaxResult.error("没有找到数据");
	}
	
	/**
	 * 传文件
	 * @param action
	 * @param request
	 * @return
	 */
	private AjaxResult uploadFile(Action action, HttpServletRequest request) {
		Map<String, Object> config = this.getConfig(action);
		MultipartFile[] files = WebUtils.uploadFile(request);
		if(files == null || files.length == 0) {
		   return AjaxResult.error("没找到上传的数据");
		}
		MultipartFile file = files[0];
	    if(request.getParameter("chunk") != null) {
	    	return this.uploadChunkFile(config, file, request);
	    } else {
	    	return this.uploadSingleFile(config, file, request);
	    }
	}
	
	@SuppressWarnings("unchecked")
	private AjaxResult uploadSingleFile(Map<String, Object> config, MultipartFile file, HttpServletRequest request) {
		File tmpFile = null;
		try {
			String originFileName = file.getOriginalFilename();
			String suffix = StringUtil3.substringAfterLast(originFileName, ".").toLowerCase();
			       suffix = "." + StringUtil3.defaultString(StringUtil3.trimToNull(suffix), "jpg").toLowerCase();
			if(!((List<String>) config.get("allowFiles")).contains(suffix)) {
			   return AjaxResult.error("不允许的文件类型");
			}
			tmpFile = ContextHolderUtils.getTempFile(file.getInputStream());
			long maxSize = ((Integer) config.get("maxSize")).longValue();
			long length = tmpFile.length();
			if (length > maxSize) {
				return AjaxResult.error("文件大小超出限制");
			}
			byte[] datas = FileUtils.readFileToByteArray(tmpFile);
			String path = this.pathFormat((String)config.get("pathFormat"), suffix);
			Long dir = Long.parseLong(request.getParameter("dirId"));
			AttachmentDir _dir = this.getUserSpaceDir(dir);
			String url = attachmentService.storage(datas, path, _dir, originFileName, length, UserUtils.getUser());
			if (StringUtil3.isBlank(url)) {
				return AjaxResult.error("上传失败");
			}
			Map<String, Object> result = Maps.newHashMap();
			result.put("url", url);
			result.put("type", suffix);
			result.put("original", originFileName);
			result.put("size", length);
			result.put("title", StringUtil3.substringAfterLast(url, "/"));
		    return AjaxResult.success(result);
		} catch (IOException e) {}finally {
			if(tmpFile != null ) {tmpFile.delete();}
		}
		return AjaxResult.error("权限不足， 多指写权限");
	}
	
	/**
	 * 分片上传文件
	 * @param file
	 * @param request
	 * @return
	 */
	private AjaxResult uploadChunkFile(Map<String, Object> config, MultipartFile file, HttpServletRequest request) {
		try {
			String originFileName = file.getOriginalFilename();
			String suffix = StringUtil3.substringAfterLast(originFileName, ".").toLowerCase();
			       suffix = "." + StringUtil3.defaultString(StringUtil3.trimToNull(suffix), "jpg").toLowerCase();
			String uuid = request.getParameter("guid");
			String chunks = request.getParameter("chunks");
			String chunk = request.getParameter("chunk");
			byte[] datas = IOUtils.toByteArray(file.getInputStream());
			String url = attachmentService.storageChunk(datas, null, StringUtil3.format("%s_%s", chunks, chunk), uuid);
			Map<String, Object> result = Maps.newHashMap();
			result.put("url", url);
			result.put("type", suffix);
			result.put("original", originFileName);
			result.put("size", file.getSize());
			result.put("title", originFileName);
			return AjaxResult.success(result);
		} catch (IOException e) {}
		return AjaxResult.error("权限不足， 多指写权限");
	}
	
	/**
	 * 获得相应的配置项
	 * --- 每次会读取site中的配置（只配置文件修改的）
	 * @param type
	 * @return
	 */
	private Map<String, Object> getConfig(Action type) { 
		Site site = SiteUtils.getSite();
		return this.config.getConfig(site, type);
	}
	
	/**
	 * 实例化后初始化的方法
	 */
	protected Map<String, Object> getConfig() {
		return this.config.getConfig();
	}
	
	/**
	 * 文件格式化
	 * @param pathFormat
	 * @param suffix
	 * @return
	 */
	public String pathFormat(String pathFormat, String suffix) {
		Map<String, Object> root = this.getRootMap();
		root.put("suffix", suffix);
		root.put("type", this.getFileType(suffix));
		return FreemarkerUtils.processNoTemplate(pathFormat, root);
	}
	
	private Map<String, Object> getRootMap() {
		Map<String, Object> root = Maps.newHashMap();
		root.put("data", DateUtil3.getTodayStr("yyyy-MM-dd"));
		root.put("datatime", DateUtil3.getTodayStr("yyyyMMddHHmmss"));
		root.put("rand", IdGen.stringKey());
		return root;
	}
	@SuppressWarnings("unchecked")
	private FileType getFileType(String suffix) {
		String _suffix = StringUtil3.lowerCase(suffix);
		List<String> files = (List<String>)config.get("imageAllowFiles");
		if(files.contains(_suffix)) {
		   return FileType.IMAGE;
		}
		files = (List<String>)config.get("videoAllowFiles");
		if(files.contains(_suffix)) {
		   return FileType.VIDEO;
		}
		return FileType.FILE;
	}
	public static enum FileType {
		IMAGE, FILE, VIDEO;
	}
	
	/**
	 * 用户空间(有个缓存支持)
	 * @return
	 */
	public AttachmentDir getUserSpaceDir(User user) {
		AttachmentDir dir = UserUtils.getAttribute(DIR_HOME);
		if (dir == null ) {
		    dir = dirService.getUserSpaceDir(user);
		    UserUtils.setAttribute(DIR_HOME, dir);
		}
		return dir;
	}
	
	/**
	 * 用户空间
	 * @param dir
	 * @return
	 */
	public AttachmentDir getUserSpaceDir(Long dir) {
		User user = UserUtils.getUser();
		AttachmentDir dirObj = dirService.get(dir);
		if (user.isRoot()) {
		    if (dirObj == null) {
			    dirObj = AttachmentDir.getRootDir();
		    }
		} else {
			//如果是home下的目录才能访问
			AttachmentDir home = getUserSpaceDir(user);
			if(dirObj == null || !StringUtil3.contains(dirObj.getParentIds(), home.getId().toString())) {
			   dirObj = home;
			}
		}
		return dirObj;
	}
}