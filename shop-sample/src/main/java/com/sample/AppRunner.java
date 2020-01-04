package com.sample;

import com.shop.Application;
import com.shop.starter.ApplicationBoot;
import com.shop.starter.EnableSystemAutoConfiguration;

/**
 * 
 * 系统启动项目
 * 
 * @author lifeng
 */
@ApplicationBoot
@EnableSystemAutoConfiguration
public class AppRunner {

	public static void main(String[] args) {
		Application.run(AppRunner.class, args);
	}
}