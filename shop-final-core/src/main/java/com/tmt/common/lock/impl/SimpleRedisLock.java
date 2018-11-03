package com.tmt.common.lock.impl;

import java.util.Random;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import com.tmt.common.cache.redis.RedisUtils;
import com.tmt.common.cache.redis.factory.IRedisCacheUtils.Callback;
import com.tmt.common.exception.LockTimeOutException;
import com.tmt.common.lock.Handler;
import com.tmt.common.lock.Lock;
import com.tmt.common.utils.FibonacciUtil;

/**
 * 简单的锁，有问题，如果没删除锁会导致一直死锁
 * 此代码只有备份，不能使用
 * @author lifeng
 */
@Deprecated
public class SimpleRedisLock implements Lock {

	static Logger logger = LoggerFactory.getLogger(SimpleRedisLock.class);
	
	/**
     * 锁等待时间，防止线程饥饿
     */
	private long acquireTimeout = 10 * 1000; // 10s
	
	/**
     * 锁超时时间，防止线程在入锁以后，无限的执行等待
     */
	private long lockTimeout = 60; // 60s
	
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
	public SimpleRedisLock(String name) {
		this.name = new StringBuilder("Lock:").append(name).toString();
	}
	
	/**
	 * 自定义时间(毫秒)
	 * @param name
	 * @param acquireTimeout
	 * @param lockTimeout
	 */
    public SimpleRedisLock(String name, long acquireTimeout, long lockTimeout) {
		this(name);
		this.acquireTimeout = acquireTimeout;
		this.lockTimeout = lockTimeout;
	}
    
	/**
	 * 尝试获得锁
	 * @param handler
	 * @return
	 * @throws LockTimeOutException
	 */
	@Override
	public <T> T doHandler(Handler handler) {
		boolean lockFlag = this.lock();
		logger.error(Thread.currentThread() + " lock:" + lockFlag);
		boolean unlockFlag = this.unlock();
		logger.error(Thread.currentThread() + " unlock:" + unlockFlag);
		return null;
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
		final String value = UUID.randomUUID().toString();
		final long end = System.currentTimeMillis() + acquireTimeout;
		final int lockExpire = (int) (lockTimeout);
		return RedisUtils.invoke(new Callback<Boolean>() {
			@Override
			public Boolean doCall(Jedis jedis) {
				int i = 1;
				while (true) {
					if (jedis.setnx(lockKey, value) == 1) {
						String lockValue = jedis.get(lockKey);
                        if (lockValue != null && lockValue.equals(value)) {
                        	jedis.expire(lockKey, lockExpire);
                        	lock_value = value;
                        	break;
                        }
					}
					
//					if (jedis.ttl(lockKey) == -1) {
//                        Transaction multi = jedis.multi();
//                        multi.expire(lockKey, lockExpire);
//                        multi.set(lockKey, value);   //将锁占为己用，并改变value;
//                        multi.exec();
//                        lock_value = value;
//                        break;
//                    }
					
					// 超时机制
					try {
                        long sleepMillis = 5L * new Random().nextInt(FibonacciUtil.circulationFibonacciNormal(++i > 15 ? 15 : i));
                        if (System.currentTimeMillis() > end) {
                        	logger.warn("Acquire SimpleRedisLock time out. spend[ {}ms ] and await[ {}ms]", System.currentTimeMillis() - end, sleepMillis);
                        }
                        Thread.sleep(sleepMillis);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
				}
				return true;
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
}