package com.tmt.common.cache.ehcache;

import java.util.List;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import com.tmt.common.cache.Cache;
import com.tmt.common.utils.Lists;
import com.tmt.common.utils.StringUtils;

public class EhCacheCache implements Cache {

	private final Ehcache cache;

	/**
	 * Create an {@link EhCacheCache} instance.
	 * @param ehcache backing Ehcache instance
	 */
	public EhCacheCache(Ehcache ehcache) {
		this.cache = ehcache;
	}

	@Override
	public String getName() {
		return this.cache.getName();
	}

	@Override
	public Ehcache getNativeCache() {
		return this.cache;
	}

	@Override
	public void put(Object key, Object value) {
		this.cache.put(new Element(key, value));
	}

	/**
	 * pattern 是XXX* 的这种格式
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> values(String pattern) {
		List<T> values = Lists.newArrayList();
		if (StringUtils.isNotBlank(pattern) && StringUtils.endsWith(pattern, "*")) {
			String _pattern = StringUtils.substringBeforeLast(pattern, "*");
			List<Object> keys = cache.getKeys();
			if(keys != null && keys.size() != 0) {
				for(Object key: keys) {
					String _key = String.valueOf(key);
					if(StringUtils.startsWith(_key, _pattern)) {
						T o = (T) cache.getQuiet(key);
						values.add(o);
					}
				}
			}
		} else if( StringUtils.isNotBlank(pattern) ) {
			T o = (T) cache.getQuiet(pattern);
			values.add(o);
		}
		return values;
	}

	@Override
	public List<Object> keys(String pattern) {
		return null;
	}

	@Override
	public void delete(Object key){
		if (key != null && key instanceof String) {
			String _key = String.valueOf(key);
			if (StringUtils.isNotBlank(_key) && StringUtils.endsWith(_key, "*") ) {
				this.deletePattern(_key); return;
			}
		}
		this.cache.remove(key);
	}
	
	@Override
	public void evict(Object key) {
		this.delete(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void deletePattern(String pattern) {
		if (StringUtils.isNotBlank(pattern) && StringUtils.endsWith(pattern, "*")) {
			String _pattern = StringUtils.substringBeforeLast(pattern, "*");
			List<String> keys = cache.getKeys();
			if(keys != null && keys.size() != 0) {
				for(String key: keys) {
					if(StringUtils.startsWith(key, _pattern)) {
						this.cache.remove(key);
					}
				}
			}
		}
	}

	@Override
	public void delete(List<Object> keys) {
		if (keys != null && keys.size() != 0) {
			this.cache.removeAll(keys);
		}
	}
	
	@Override
	public boolean exists(Object key) {
		return this.cache.isKeyInCache(key);
	}

	@Override
	public void put(Object key, Object value, int timeToLive) {
		Element e = new Element(key, value);
		e.setTimeToLive(timeToLive < 0? 0: timeToLive);
		this.cache.put(e);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(Object key) {
		Element element = this.cache.get(key);
		return  (element != null ? (T)(element.getObjectValue()) : null);
	}
	
	@Override
	public void clear() {
		this.cache.removeAll();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> values() {
		List<T> values = Lists.newArrayList();
		List<Object> keys = cache.getKeys();
		if(keys != null && keys.size() != 0) {
			for(Object key: keys) {
				T o = (T) cache.getQuiet(key);
				values.add(o);
			}
		}
		return values;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> keys() {
		return cache.getKeys();
	}

	@Override
	public long size() {
		return this.cache.getSize();
	}

	@Override
	public long ttl(Object key) {
		Element element = this.cache.get(key);
		return (element != null ? element.getTimeToIdle(): -2);
	}
}