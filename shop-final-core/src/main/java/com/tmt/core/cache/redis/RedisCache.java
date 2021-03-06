package com.tmt.core.cache.redis;

import java.util.List;
import java.util.Set;

import com.tmt.core.cache.Cache;
import com.tmt.core.exception.CacheException;
import com.tmt.core.utils.Lists;
import com.tmt.core.utils.StringUtils;

/**
 * Redis 需要在配置文件中配置,以后在添加详细的参数
 * @author lifeng
 */
public class RedisCache implements Cache {
	
	private static int EXPIRATION_IN = -1; //默认不过期
	private String name;
	private String prex = "#";
	private int timeToLive = EXPIRATION_IN;//固定的存活时间，到点就清除
	private int timeToIdle = EXPIRATION_IN;//最大空闲时间，每次访问会修改最大的空闲时间
	
	public RedisCache(){}
	public RedisCache( String name){
		this.setName(name);
	}
	
	/**
	 * 得到key 的string表示(全部使用string的key)
	 * @param key
	 * @return
	 */
	protected String getKeyName(Object key) {
		return new StringBuilder(name).append(prex).append(key).toString();
	}
	
	/**
	 * 得到缓存的名称
	 */
	@Override
	public String getName() {
		return name.toString();
	}
	
	/**
	 * 得到本地存储对象
	 * 直接返回缓存（作为最小的存储单元，保护后面的连接）
	 */
	@Override
	public Object getNativeCache() {
		return this;
	}
	
	/**
	 * 类似最大空闲时间
	 * get,如果设置了过期时间，每次获取会更新过期时间,默认的实现
	 * 如果不需要这样的功能，可以使用 RedisUtils 来获取
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key) {
		if( key == null) {return null;}
		String _key = getKeyName(key);
		Object value = RedisUtils.getObject(_key);
		Object _value = value;
	    int expiration = this.getTimeToIdle();
		if (value instanceof ExpireTimeValueWrapper) {//自定义了过期时间
			expiration = -1;
			_value = ((ExpireTimeValueWrapper)value).get();
		}
		if (ExpireTimeValueWrapper.isValid(expiration)) {//设置了空闲时间的key每次访问才会更新过期时间为最大空闲时间，不会累加
			RedisUtils.expire(_key, expiration);
		}
		return (T) _value;
	}
	
	/**
	 * 获得原始值 (子类才能使用)
	 * @param key
	 * @return
	 */
	protected Object _get(Object key) {
		if( key == null) {return null;}
		String _key = getKeyName(key);
		Object value = RedisUtils.getObject(_key);
	    int expiration = this.getTimeToIdle();
		if (value instanceof ExpireTimeValueWrapper) {//自定义了过期时间
			expiration = -1; // 自定义的过期时间，不支持这种模式
		}
		if (ExpireTimeValueWrapper.isValid(expiration)) {//设置了空闲时间的key每次访问才会更新过期时间为最大空闲时间，不会累加
			RedisUtils.expire(_key, expiration);
		}
		return value;
	}
	
	/**
	 * 添加
	 */
	@Override
	public void put(Object key, Object value) {
		String _key = getKeyName(key);
		int expiration = this.getExpiration(); //设置过期时间
		if (ExpireTimeValueWrapper.isValid(expiration)) {
			RedisUtils.set(_key, value, expiration);
		} else {
			RedisUtils.set(_key, value);
		}
	}
	
	/**
	 * 值s
	 */
	@Override
	public <T> List<T> values(String pattern) {
		String _key = new StringBuilder(name).append(prex).append(pattern).toString();
		return RedisUtils.valueOfObjects(_key);
	}
	
	/**
	 *  keys
	 */
	@Override
	public List<Object> keys(String pattern) {
		String _key = new StringBuilder(name).append(prex).append(pattern).toString();
		Set<String> keys = RedisUtils.keys(_key);
		List<Object> _keys = Lists.newArrayList();
		String keyPrex = new StringBuilder(name).append(prex).toString();
		for(String key: keys) {
			if(StringUtils.isNotBlank(key)) {
			   key = StringUtils.removeStart(key, keyPrex);
			}
			_keys.add(key);
		}
		return _keys;
	}
	
	/**
	 * 删除 pattern 指定的key
	 * @param pattern
	 */
	@Override
	public void delete(Object key){
		if (key != null && key instanceof String) {
			String _key = String.valueOf(key);
			if (StringUtils.isNotBlank(_key) && StringUtils.contains(_key, "*")) {
				this.deletePattern(_key); return;
			}
		}
		RedisUtils.delete(getKeyName(key));
	}
	
	@Override
	public void evict(Object key) {
		this.delete(key);
	}
	
	@Override
	public void deletePattern(String pattern) {
		String _key = new StringBuilder(name).append(prex).append(pattern).toString();
		RedisUtils.deletes(_key);
	}
	
	@Override
	public void delete(List<Object> keys) {
		if (keys != null && keys.size() != 0) {
			List<String> _keys = Lists.newArrayList();
			for(Object key: keys) {
				_keys.add(this.getKeyName(key));
			}
			RedisUtils.delete(_keys.toArray(new String[]{}));
		}
	}
	
	@Override
	public boolean exists(Object key) {
		String _key = getKeyName(key);
		int expiration = this.getTimeToIdle();
		if (ExpireTimeValueWrapper.isValid(expiration)) {//设置了空闲时间的key每次访问才会更新过期时间为最大空闲时间，不会累加
			RedisUtils.expire(_key, expiration);
		}
		return RedisUtils.exists(_key);
	}
	
	/**
	 * redis 中不能设置为0
	 */
	@Override
	public void put(Object key, Object value, int timeToLive) {
		if (!ExpireTimeValueWrapper.isValid(timeToLive)) {
		    throw new CacheException("过期时间必须大于0");
		}
		String _key = getKeyName(key);
		int expiration = this.getExpiration(timeToLive, timeToIdle); //设置过期时间
		if (ExpireTimeValueWrapper.isValid(expiration)) {
			Object _value = new ExpireTimeValueWrapper(value, timeToLive);
			RedisUtils.set(_key, _value, expiration);
		} else {
			this.put(key, value); //设置的动态时间无效才走默认的规则
		}
	}
	
	@Override
	public void clear() {
		String _key = new StringBuilder(name).append(prex).append("*").toString();
		RedisUtils.deletes(_key);
	}
	
	@Override
	public <T> List<T> values() {
		String _key = new StringBuilder(name).append(prex).append("*").toString();
		return RedisUtils.valueOfObjects(_key);
	}
	
	@Override
	public List<Object> keys() {
		return this.keys("*");
	}
	
	@Override
	public long size() {
		String _key = new StringBuilder(name).append(prex).append("*").toString();
		return RedisUtils.size(_key);
	}
	
	@Override
	public long ttl(Object key) {
		String _key = getKeyName(key);
		return RedisUtils.ttl(_key);
	}
	
	/**
	 * 默认值设置过期时间
	 * @return
	 */
	protected int getExpiration() {
		//这两个只能使用一个,简单点
		if (ExpireTimeValueWrapper.isValid(this.timeToLive)) {
			return this.timeToLive;
		}
		if (ExpireTimeValueWrapper.isValid(this.timeToIdle)) {
			return this.timeToIdle;
		}
		return EXPIRATION_IN;
	}
	
	/**
	 * 动态设置过期时间(设置了无效的动态时间)
	 * @param timeToLive
	 * @param timeToIdle
	 * @return
	 */
	protected int getExpiration( int timeToLive, int timeToIdle) {
		if (ExpireTimeValueWrapper.isValid(timeToLive)) {
			return timeToLive;
		}
		if (ExpireTimeValueWrapper.isValid(timeToIdle)) {
			return timeToIdle;
		}
		return EXPIRATION_IN;
	}
	public void setName(String name) {
		if (name != null) {
			this.name = name.toUpperCase();
		}
	}
	public String getPrex() {
		return prex;
	}
	public void setPrex(String prex) {
		this.prex = prex;
	}
	public int getTimeToLive() {
		return timeToLive;
	}
	public void setTimeToLive(int timeToLive) {
		this.timeToLive = timeToLive;
	}
	public int getTimeToIdle() {
		return timeToIdle;
	}
	public void setTimeToIdle(int timeToIdle) {
		this.timeToIdle = timeToIdle;
	}
}
