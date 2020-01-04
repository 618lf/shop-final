package com.tmt.core.lock.impl;

import java.util.Random;

import com.tmt.core.cache.redis.RedisUtils;
import com.tmt.core.cache.redis.factory.IRedisCacheUtils.Callback;
import com.tmt.core.exception.LockTimeOutException;
import com.tmt.core.lock.Handler;
import com.tmt.core.lock.Lock;
import com.tmt.core.utils.FibonacciUtil;

import redis.clients.jedis.Jedis;

/**
 * 严格的 redis 的锁
 * @author lifeng
 */
public class StrictRedisLock implements Lock {

	/**
     * 锁等待时间，防止线程饥饿
     */
	private long acquireTimeout = 10 * 1000; // 10s
	
	/**
     * 锁超时时间，防止线程在入锁以后，无限的执行等待
     */
	private long lockTimeout = 60 * 1000; // 60s
	
	/**
	 * lock name
	 */
	private String name;
	
	/**
	 * lock value
	 */
	private String lock_value;
	
	/**
	 * 使用默认的时间
	 * @param name
	 */
	public StrictRedisLock(String name) {
		this.name = new StringBuilder("Lock:").append(name).toString();
	}
	
	/**
	 * 自定义时间(毫秒)
	 * @param name
	 * @param acquireTimeout
	 * @param lockTimeout
	 */
    public StrictRedisLock(String name, long acquireTimeout, long lockTimeout) {
		this(name);
		this.acquireTimeout = acquireTimeout;
		this.lockTimeout = lockTimeout;
	}
    
	/**
	 * 阻塞式-获得锁-有等待超时事件和执行超时时间
	 * @param handler
	 * @return
	 * @throws LockTimeOutException
	 */
	@Override
	public <T> T doHandler(Handler handler) {
		boolean hasLock = false;
		try {
			if (!!(hasLock = this.lock())) {
				return handler.doHandle();
			}
			return null;
		} finally {
			if (hasLock) {
				this.unlock();
			}
		}
	}

	@Override
	public String getName() {
		return name;
	}
	
	/**
	 * 一个线程最多等待10秒
	 * 最多执行 60秒
	 */
	private boolean lock() {
		final String lockKey = this.getName();
		final long end = localTimeMillis() + acquireTimeout;
		return RedisUtils.invoke(new Callback<Boolean>() {
			@Override
			public Boolean doCall(Jedis jedis) {
				int i = 1; 
				while (true) {
					long expires = localTimeMillis() + lockTimeout + 1;
					String value = String.valueOf(expires);
					if (jedis.setnx(lockKey, value) == 1) {
						lock_value = value;
						return true;
					}
					
					// redis 当前的值
					String cValue = jedis.get(lockKey);
					if (cValue != null && Long.parseLong(cValue) < localTimeMillis()) {
						//判断是否为空，不为空的情况下，如果被其他线程设置了值，则第二个条件判断是过不去的
		                // lock is expired
						
						String oldValue = jedis.getSet(lockKey, value);
						//获取上一个锁到期时间，并设置现在的锁到期时间，
		                //只有一个线程才能获取上一个线上的设置时间，因为jedis.getSet是同步的
						if (oldValue != null && oldValue.equals(cValue)) {
							//防止误删（覆盖，因为key是相同的）了他人的锁——这里达不到效果，这里值会被覆盖，但是因为什么相差了很少的时间，所以可以接受
							//[分布式的情况下]:如过这个时候，多个线程恰好都到了这里，但是只有一个线程的设置值和当前值相同，他才有权利获取锁
		                    // lock acquired
							lock_value = value;
							return true;
						}
					}
					
					// 超时机制
					try {
                        long sleepMillis = 5L * new Random().nextInt(FibonacciUtil.circulationFibonacciNormal(++i > 15 ? 15 : i));
                        if (localTimeMillis() > end) {
                            return false;
                        }
                        Thread.sleep(sleepMillis);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
				}
			}
		});
	}
	
	/**
	 * 释放 -- 是否时当前的 value 才需要删除
	 * @return
	 */
	public boolean unlock() {
		if (lock_value != null) {
			final String lockKey = this.getName();
		    return RedisUtils.invoke(new Callback<Boolean>() {
				@Override
				public Boolean doCall(Jedis jedis) {
					String value = jedis.get(lockKey);
					if (value != null && lock_value.equals(value)) {
						jedis.del(lockKey);
						return true;
					}
					return false;
				}
			});
		}
		return false;
	}
	
	/**
	 * redis服务器系统时间
	 * 用上这个，会导致2000线程多2秒左右
	 * @param jedis
	 * @return
	 */
	@SuppressWarnings("unused")
	private long serverTimeMillis(Jedis jedis) {
		return Long.parseLong(jedis.time().get(0));
	}
	
	/**
	 * 本地服务器的时间
	 * @return
	 */
	private long localTimeMillis() {
		return System.currentTimeMillis();
	}
}