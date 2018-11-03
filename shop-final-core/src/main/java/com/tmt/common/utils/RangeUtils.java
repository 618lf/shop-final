package com.tmt.common.utils;

import java.util.Collections;
import java.util.List;

/**
 * 范围随机算法
 * @author root
 */
public class RangeUtils {

	/**
	 * 裂变算法
	 * @param seed
	 * @param fissionNum
	 * @return
	 */
	public static List<Integer> fission(int seed, int fissionNum, int min) {
		
		// 分裂的数据
		List<Integer> fissions = Lists.newArrayList();
		
		// 小于1就不用分了
		if (seed <= 1 || fissionNum <= 1) {
			fissions.add(seed);
			return fissions;
		}
		
		// 最大分裂数
		fissionNum = fissionNum > seed?seed:fissionNum;
		
		// 随机数
		int remain = seed;
		for(int i= 1; i< fissionNum; i++) {
			int max = remain - (fissionNum - i) * min;
			int avg = remain / (fissionNum - i + 1);
			int _max = (max - avg) / 2;
			    _max  = _max < avg ? max : _max;
			int random = Ints.random(_max, min);
			    random = random < 0 ? 0:random;
			    random = random > remain ? 0: random;
			remain = remain - random;
			
			// 存储随机数
			fissions.add(random);
		}
		
		// 最后的作为一个数
		fissions.add(remain);
		Collections.shuffle(fissions);
		return fissions;
	}
	
	public static void main(String[] args) {
		List<Integer> fissions = fission(9, 2, 5);
		System.out.println(fissions);
		fissions = fission(200, 12, 5);
		System.out.println(fissions);
	}
}