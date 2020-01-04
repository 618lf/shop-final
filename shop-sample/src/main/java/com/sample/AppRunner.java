package com.sample;

import com.shop.Application;
import com.shop.autoconfigure.EnableSystemAutoConfiguration;
import com.shop.starter.ApplicationBoot;

/**
 * 
 * 系统启动项目
 * 
 * @author lifeng
 */
@EnableSystemAutoConfiguration
@ApplicationBoot
public class AppRunner {

	public static void main(String[] args) {
		Application.run(AppRunner.class, args);
	}
}