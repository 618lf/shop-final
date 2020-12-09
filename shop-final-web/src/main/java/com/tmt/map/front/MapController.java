package com.tmt.map.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.common.entity.AjaxResult;
import com.tmt.common.utils.WebUtils;
import com.tmt.map.service.MapService;
import com.tmt.map.service.impl.BaiduMapService;
import com.tmt.map.service.impl.TencentMapService;

/**
 * 消息
 * 
 * @author root
 */
@Controller("frontMapController")
@RequestMapping(value = "${frontPath}/member/map")
public class MapController {

	private MapService local_mapService;
	private MapService rang_mapService;
	public MapController() {
		local_mapService = new BaiduMapService();
		rang_mapService = new TencentMapService();
	}
	
	/**
	 * 获取当前地址周围的信息
	 * 
	 * @return
	 */
	@RequestMapping("address")
	public String address() {
		return "/front/member/LocaltionAddresss";
	}

	/**
	 * 获取当前地址周围的信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getLocationPois.json")
	public AjaxResult getLocationPois() {
		// 请求者IP地址
		String ip = WebUtils.getRemoteAddr(null);
		AjaxResult resutl = local_mapService.getLocationPois(ip);
		if(!resutl.getSuccess()) {
		   return rang_mapService.getLocationPois(ip);
		}
		return resutl;
	}

	/**
	 * 根据关键字查找地址
	 * @return
	 */
	@ResponseBody
	@RequestMapping("getPoisByKey.json")
	public AjaxResult queryLocationPois(String key) {
		AjaxResult resutl = rang_mapService.queryLocationPois(key);
		if (!resutl.getSuccess()) {
			return local_mapService.queryLocationPois(key);
		}
		return resutl;
	}
}