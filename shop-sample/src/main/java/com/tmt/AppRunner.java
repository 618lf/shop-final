package com.tmt;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
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

		// 打印所有的 filter
		String[] filters = Application.CONTEXT.getBeanNamesForType(Filter.class);
		for (String filter : filters) {
			System.out.println(filter);
		}

		// 打印所有的 filter
		filters = Application.CONTEXT.getBeanNamesForType(FilterRegistrationBean.class);
		for (String filter : filters) {
			System.out.println(filter);
		}
	}
}