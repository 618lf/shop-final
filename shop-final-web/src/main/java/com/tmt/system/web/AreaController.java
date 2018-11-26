package com.tmt.system.web;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tmt.common.config.Globals;
import com.tmt.common.entity.AjaxResult;
import com.tmt.common.http.LocalHttpClient;
import com.tmt.common.persistence.QueryCondition;
import com.tmt.common.persistence.incrementer.IdGen;
import com.tmt.common.utils.ContextHolderUtils;
import com.tmt.common.utils.ExportUtils;
import com.tmt.common.utils.FileUtils;
import com.tmt.common.utils.FreemarkerUtils;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.Maps;
import com.tmt.common.utils.StringUtil3;
import com.tmt.common.utils.TreeEntityUtils;
import com.tmt.common.utils.WebUtils;
import com.tmt.common.web.BaseController;
import com.tmt.system.entity.Area;
import com.tmt.system.service.AreaServiceFacade;
import com.tmt.system.utils.AreaParse;
import com.tmt.system.utils.UserUtils;

/**
 * 区域管理
 * @author root
 */
@Controller
@RequestMapping(value = "${adminPath}/system/area")
public class AreaController extends BaseController{

	@Autowired
	private AreaServiceFacade areaService;
	
	/**
	 * 列表
	 * @param area
	 * @param model
	 * @return
	 */
	@RequestMapping("list")
	public String list(Area area, Model model) {
		if(area != null && area.getId() != null) {
		   model.addAttribute("id", area.getId());
		}
		return "/system/AreaList";
	}
	
	/**
	 * 表单页面
	 * @param area
	 * @param model
	 * @return
	 */
	@RequestMapping("form")
	public String form(Area area, Model model) {
		if (area != null && area.getId() != null) {
			area = this.areaService.get(area.getId());
			Area parent = this.areaService.get(area.getParentId());
			if (parent != null) {
				area.setParentId(parent.getId());
				area.setParentName(parent.getName());
			}
		} else {
			area.setId(IdGen.INVALID_ID);
			if( area.getParentId() == null ){
				area.setParentId(IdGen.ROOT_ID);
			}
		}
		Area parent = this.areaService.get(area.getParentId());
		if (parent!= null) {
		    area.setParentId(parent.getId());
		    area.setParentName(parent.getName());	
		}
		model.addAttribute("area", area);
		return "/system/AreaForm";
	}
	
	/**
	 * 保存
	 * @param area
	 * @param model
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping("save")
	public String save(Area area, Model model,RedirectAttributes redirectAttributes){
		area.userOptions(UserUtils.getUser());
		Long id = this.areaService.save(area);
		addMessage(redirectAttributes, "保存区域'" + area.getName() + "'成功");
		redirectAttributes.addAttribute("id", id);
		return WebUtils.redirectTo(new StringBuilder(Globals.adminPath).append("/system/area/form").toString());
	}
	
	/**
	 * 删除
	 * @param idList
	 * @param model
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete")
	public AjaxResult delete(Long[] idList , Model model,HttpServletResponse response) {
		List<Area> areas = Lists.newArrayList();
		Boolean bFalg = Boolean.TRUE;
		Area oneParent = null;
		for (Long id: idList) {
			 Area org = this.areaService.get(id);
			 int iCount = this.areaService.deleteAreaCheck(org);
			 if (iCount > 0){
				 bFalg = Boolean.FALSE; break;
			 }
			 areas.add(org);
			 if (oneParent == null){
				 oneParent = new Area();
				 oneParent.setId(org.getParentId());
			 }
		}
		if (!bFalg) {//删除失败
			return AjaxResult.error("要删除的区域存在子区域或存在组织结构!");
		}
		this.areaService.delete(areas);
		return AjaxResult.success(oneParent);
	}
	
	/**
	 * 从中华人民共和国国家统计局网站统计数据
	 * 统计到街道
	 * @return
	 */
	@ResponseBody
	@RequestMapping("sync")
	public AjaxResult sync_stats_gov_cn() {
		String baseUrl = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2015/";
		String url = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2015/index.html";
		HttpUriRequest request = RequestBuilder.get().setUri(url).build();
		String html = LocalHttpClient.execute(request, Charset.forName("gb2312"));
		if (StringUtil3.isBlank(html)) {
			return AjaxResult.error("同步失败");
		}
		
		List<Area> areas = Lists.newArrayList();
		
		// 省
		AreaParse parse = new AreaParse();
		List<Area> provinces = parse.parseProvinces(html);
		
		// 市
		for(Area province: provinces) {
			String pcode = province.getCode();
			url = baseUrl + pcode;
			request = RequestBuilder.get().setUri(url).build();
			html = LocalHttpClient.execute(request, Charset.forName("gb2312"));
			List<Area> citys = parse.parseOthers(html, "citytr");
			areas.add(province);
			areas.addAll(citys);
			
			// 区县
			for(Area city: citys) {
				String ccode = city.getCode();
				url = baseUrl + ccode;
				request = RequestBuilder.get().setUri(url).build();
				html = LocalHttpClient.execute(request, Charset.forName("gb2312"));
				List<Area> countys = parse.parseOthers(html, "countytr");
				city.setParentId(province.getId());
				areas.addAll(countys);

				// 街道
				for(Area county: countys) {
					String cocode = county.getCode();
					county.setParentId(city.getId());
					if (StringUtil3.endsWith(cocode, ".html")) {
						url = baseUrl + province.getId() + "/" + cocode;
						request = RequestBuilder.get().setUri(url).build();
						html = LocalHttpClient.execute(request, Charset.forName("gb2312"));
						List<Area> towns = parse.parseOthers(html, "towntr");
						county.setChildren(towns);
						for(Area town: towns) {
							town.setParentId(county.getId());
							town.setName(StringUtil3.remove(town.getName(), "办事处"));
						}
						areas.addAll(towns);
					}
				}
			}
			
			// 一个省一个省的存储
			this.areaService.batchImport(areas);
			areas.clear();
		}
		return AjaxResult.success();
	}
	
	/**
	 * 生成选择控件
	 * 导出控件数据
	 * @return
	 */
	@RequestMapping("picker/data")
	public void pickerData(HttpServletResponse response) {
		QueryCondition qc = new QueryCondition();
		List<Area> areas = this.areaService.queryByCondition(qc);
		Map<Integer,List<Area>> menuMap = TreeEntityUtils.classifyByLevel(areas);
		int firstLevel = 1;
		List<Area> firstMenus = menuMap.get(firstLevel);
		for(Area menu: firstMenus ) {
			this.fillChildren(menu, firstLevel + 1, menuMap);
		}
		Map<String, Object> model = Maps.newHashMap();
		model.put("areas", areas);
		String content = FreemarkerUtils.processUseTemplate("/picker/jquery.citypicker.data.js.ftl", model);
		File file = ContextHolderUtils.getTempFile();
		try {
			FileUtils.write(file, content, Globals.DEFAULT_ENCODING);
		} catch (IOException e) {}
		ExportUtils.downloadFile(file, "jquery.citypicker.data.js", response, Globals.DEFAULT_ENCODING, true);
	}
	
	private void fillChildren(Area parent,int level, Map<Integer,List<Area>> menuMap) {
		List<Area> firstMenu = menuMap.get(level); //level从1开始
		if(firstMenu == null){ return;}
		for(Area menu: firstMenu) {
			if(menu.getParentId().compareTo(parent.getId()) == 0) {
			   parent.addChild(menu);
			   fillChildren(menu, level+1, menuMap);
			}
		}
	}
	
	/**
	 * 选择区域
	 * @param extId
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping("treeSelect")
	public List<Map<String, Object>> treeSelect(@RequestParam(required=false)String extId, HttpServletResponse response) {
		List<Map<String, Object>> mapList = Lists.newArrayList();
		List<Area> trees = this.areaService.queryAreasByBeforeLevel(3, null);
		for(int i=0; i<trees.size(); i++){
			Area e = trees.get(i);
			if (extId == null || (extId!=null && !extId.equals(e.getId().toString()) && e.getParentIds().indexOf(","+extId+",")==-1)){
				Map<String, Object> map = Maps.newHashMap();
				map.put("id", e.getId());
				map.put("pId", e.getParentId());
				map.put("name", e.getName());
				mapList.add(map);
			}
		}
		return mapList;
	}
}