package com.tmt.core.cache.redis;

import java.util.List;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.tmt.core.cache.Cache;
import com.tmt.core.cache.ehcache.EhCacheCache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.util.SafeEncoder;

/**
 * 本地缓存
 * @author root
 */
public class RedisLocalCache extends BinaryJedisPubSub implements Cache, InitializingBean, DisposableBean{

	private CacheManager cacheManager;
	// 本地缓存
	private EhCacheCache local;
		
	public void setCacheManager(CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Ehcache ehcache = cacheManager.getEhcache("local");
		this.local = new EhCacheCache(ehcache);
	}
	
	@Override
	public void destroy() throws Exception {
		cacheManager.shutdown();
	}
	
	/**
	 * 订阅的频道
	 * @return
	 */
	public byte[] getChannels() {
		return SafeEncoder.encode("local_channel");
	}
	
	@Override
	public String getName() {
		return local.getName();
	}

	@Override
	public Object getNativeCache() {
		return local.getNativeCache();
	}

	@Override
	public List<Object> keys(String pattern) {
		return local.keys(pattern);
	}

	@Override
	public <T> List<T> values(String pattern) {
		return local.values(pattern);
	}

	@Override
	public <T> T get(Object key) {
		return local.get(key);
	}

	@Override
	public void delete(Object key) {
		local.delete(key);
	}

	@Override
	public void evict(Object key) {
		local.evict(key);
	}

	@Override
	public void deletePattern(String pattern) {
		local.deletePattern(pattern);
	}

	@Override
	public void delete(List<Object> keys) {
		local.delete(keys);
	}

	@Override
	public boolean exists(Object key) {
		return local.exists(key);
	}

	@Override
	public void put(Object key, Object value) {
		local.put(key, value);
	}

	/**
	 * 自定义了过期时间的key不支持二级缓存
	 */
	@Override
	public void put(Object key, Object value, int timeToLive) {}

	@Override
	public void clear() {
		local.clear();
	}

	@Override
	public <T> List<T> values() {
		return local.values();
	}

	@Override
	public List<Object> keys() {
		return local.keys();
	}

	@Override
	public long size() {
		return local.size();
	}

	//------------- 消息订阅 -------------
	/**
	 * 获取订阅的消息
	 */
	@Override
	public void onMessage(byte[] channel, byte[] message) {
		
		// 无效消息
		if (message != null && message.length <= 0) {
			return;
		}
		
		try {
			Command cmd = Command.parse(message);
			if (cmd == null || cmd.isLocalCommand()) {
				return;
			}
			switch (cmd.getOperator()) {
			case Command.OPT_DELETE_KEY:
				onDeleteCacheKey(cmd.getKey());
				break;
			case Command.OPT_CLEAR_KEY:
				onClearCacheKey();
				break;
			}
		} catch (Exception e) {}
	}
	
	/**
	 * 删除一级缓存的键对应内容
	 * @param region : Cache region name
	 * @param key  : cache key
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void onDeleteCacheKey(Object key) {
		if (key instanceof List)
			local.delete((List) key);
		else
			local.delete(key);
	}

	/**
	 * 清除一级缓存的键对应内容
	 * @param region Cache region name
	 */
	protected void onClearCacheKey(){
		local.clear();
	}

	@Override
	public long ttl(Object key) {
		Element element = local.getNativeCache().get(key);
		if (element != null && element.getTimeToLive() != 0) {
			return (element.getExpirationTime() - System.currentTimeMillis()) / 1000;
		}
		return -2;
	}
}