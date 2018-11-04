package com.tmt.system.web;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.tmt.common.cache.Cache;
import com.tmt.common.cache.redis.RedisLocalCache;
import com.tmt.common.entity.AjaxResult;
import com.tmt.common.entity.LabelVO;
import com.tmt.common.utils.CacheUtils;
import com.tmt.common.utils.JsonMapper;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.Maps;
import com.tmt.common.utils.Sets;
import com.tmt.common.utils.StringUtil3;

/**
 * 缓存监控的服务,内部缓存不同，监控也不一定相同
 * 只能出缓存统计信息，其他的信息不能给出
 * 系统主要提供了四大缓存，见 CacheUtil + shiro
 * @author lifeng
 */
@Controller
@RequestMapping(value = "${adminPath}/system/cache")
public class CacheMonitorController {

	@Autowired(required=false)
	private RedisLocalCache localCache;
	
	@RequestMapping("monitor")
	public String monitor(Model model) {
		return "system/CacheMonitor";
	}
	
	/**
	 * 查询
	 * @param name
	 * @param model
	 * @return
	 */
	@ResponseBody
	@RequestMapping("search")
	public List<LabelVO> search(String name, Model model) {
		List<LabelVO> keys = Lists.newArrayList();
		
		if (StringUtil3.isNotBlank(name)) {
			Cache cache = CacheUtils.getSysCache();
			List<Object> _keys = cache.keys(name + "*");
			for(Object object : _keys) {
				keys.add(LabelVO.newLabel("sys", object.toString()));
			}
		}
		
		if (StringUtil3.isNotBlank(name)) {
			Cache cache = CacheUtils.getSessCache();
			List<Object> _keys = cache.keys(name + "*");
			for(Object object : _keys) {
				keys.add(LabelVO.newLabel("sess", object.toString()));
			}
		}
		
		if (StringUtil3.isNotBlank(name)) {
			Cache cache = CacheUtils.getDictCache();
			List<Object> _keys = cache.keys(name + "*");
			for(Object object : _keys) {
				keys.add(LabelVO.newLabel("dict", object.toString()));
			}
		}
		
		if (StringUtil3.isNotBlank(name)) {
			Cache cache = CacheUtils.getCache("authorization");
			List<Object> _keys = cache.keys(name + "*");
			for(Object object : _keys) {
				keys.add(LabelVO.newLabel("authorization", object.toString()));
			}
		}
		return keys;
	}
	
	/**
	 * 缓存详情
	 * @param type
	 * @param name
	 * @return
	 */
	@ResponseBody
	@RequestMapping("detail")
	public AjaxResult detail(String type, String name) {
		Set<String> caches = Sets.newHashSet();
		caches.add("sys");caches.add("sess");caches.add("dict");caches.add("authorization");
		if (StringUtil3.isNotBlank(type) && caches.contains(type)
				&& StringUtil3.isNotBlank(name)) {
			Cache cache = CacheUtils.getCache(type);
			Object o = cache.get(name);
			if (o != null) {
				Map<String, Object> values = Maps.newHashMap();
				values.put("key", type + "#" + name);
				values.put("_ttl", cache.ttl(name));
				values.put("json", JsonMapper.toJson(o));
				
				// 二级缓存
				if (localCache != null) {
					values.put("local", localCache.exists(type.toUpperCase() + "#" + name));
					values.put("local_ttl", localCache.ttl(type.toUpperCase() + "#" + name));
				}
				return AjaxResult.success(values);
			}
		}
		return AjaxResult.error("查询失败");
	}
	
	/**
	 * 删除
	 * @return
	 */
	@ResponseBody
	@RequestMapping("delete")
	public AjaxResult delete(String type, String name) {
		Set<String> caches = Sets.newHashSet();
		caches.add("sys");caches.add("sess");caches.add("dict");caches.add("authorization");
		if (StringUtil3.isNotBlank(type) && caches.contains(type)
				&& StringUtil3.isNotBlank(name)) {
			Cache cache = CacheUtils.getCache(type);
			cache.delete(name);
			return AjaxResult.success();
		}
		return AjaxResult.error("删除失败");
	}
}