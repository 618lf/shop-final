package com.tmt;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;

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
		SpringApplication.run(AppRunner.class, args);
	}
}