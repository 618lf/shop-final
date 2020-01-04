package com.shop.starter.selector;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 自动引入系統組件
 * 
 * @author lifeng
 */
@Configuration
@ComponentScan(value = { "com.tmt.system", "com.tmt.gen" })
public class AutoConfigurationSystemComponents {
}
