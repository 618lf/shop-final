package com.sample;

import org.springframework.context.annotation.ComponentScan;

import com.shop.Application;
import com.shop.starter.ApplicationBoot;

/**
 * 
 * 系统启动项目
 * 
 * @author lifeng
 */
@ComponentScan
@ApplicationBoot
public class AppRunner {

	public static void main(String[] args) {
		Application.run(AppRunner.class, args);
	}
}