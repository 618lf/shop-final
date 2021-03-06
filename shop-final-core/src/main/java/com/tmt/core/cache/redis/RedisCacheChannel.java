package com.tmt.core.cache.redis;

import java.util.List;

import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;

import redis.clients.util.SafeEncoder;

/**
 * 如果本地缓存还有，但是远程缓存删除了--怎么处理
 * 二级缓存功能
 * @author root
 */
public class RedisCacheChannel extends RedisCache {

	// 本地缓存
	private RedisLocalCache local;
		
	public RedisLocalCache getLocal() {
		return local;
	}

	public void setLocal(RedisLocalCache local) {
		this.local = local;
	}

	// 以下实现了使用本地缓存的方法
	/**
	 * 优先从1级缓存获取数据。 
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T get(Object key) {
		String localKey = this.getKeyName(key);
		T t = local.get(localKey);
		if (t == null) {
			Object o = super._get(key);
			if (o != null && o instanceof ExpireTimeValueWrapper) {
				t = (T) ((ExpireTimeValueWrapper)o).get();
			} else if(o != null){
				t = (T) o;
				local.put(localKey, t);
			}
		}
		return t;
	}

	@Override
	public void put(Object key, Object value) {
		String localKey = this.getKeyName(key);
		_sendEvictCmd(localKey);// 清除原有的一级缓存的内容
		local.put(localKey, value);
		super.put(key, value);
	}

	@Override
	public void delete(Object key) {
		
		// 是否是批量删除
		if (key != null && key instanceof String) {
			String _key = String.valueOf(key);
			if (StringUtils.isNotBlank(_key) && StringUtils.contains(_key, "*")) {
				this.deletePattern(_key); 
				return;
			}
		} 
		
		// 单个删除
		String localKey = this.getKeyName(key);
		super.delete(key);
		local.delete(localKey);
		_sendEvictCmd(localKey);
	}

	@Override
	public void evict(Object key) {
		this.delete(key);
	}

	@Override
	public void deletePattern(String pattern) {
		String localKey = this.getKeyName(pattern);
		List<Object> keys = super.keys(pattern);
		local.deletePattern(localKey);
		super.deletePattern(pattern);
		if (keys != null && keys.size() != 0) {
			_sendEvictCmd(keys);
		}
	}

	@Override
	public void delete(List<Object> keys) {
		List<String> _keys = this._delete(keys);
		local.delete(_keys);
		if (_keys != null && _keys.size() != 0) {
			_sendEvictCmd(_keys);
		}
	}
	
	// 返回整理好的keys
	private List<String> _delete(List<Object> keys) {
		if (keys != null && keys.size() != 0) {
			List<String> _keys = Lists.newArrayList();
			for(Object key: keys) {
				_keys.add(this.getKeyName(key));
			}
			RedisUtils.delete(_keys.toArray(new String[]{}));
			return _keys;
		}
		return null;
	}

	@Override
	public void clear() {
		local.clear();
		super.clear();
		_sendClearCmd();
	}

	/**
	 * 发送清除缓存的广播命令
	 * @param region: Cache region name
	 */
	private void _sendClearCmd() {
		// 发送广播
		Command cmd = new Command(Command.OPT_CLEAR_KEY, "");
		RedisUtils.publish(SafeEncoder.encode("local_channel"), cmd.toBuffers());
	}
	
	/**
	 * 发送清除缓存的广播命令
	 * 
	 * @param region
	 *            : Cache region name
	 * @param key
	 *            : cache key
	 */
	private void _sendEvictCmd(Object key) {
		if (key != null) {
			// 发送广播
			Command cmd = new Command(Command.OPT_DELETE_KEY, key);
			RedisUtils.publish(SafeEncoder.encode("local_channel"), cmd.toBuffers());
		}
	}
}