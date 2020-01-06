package com.shop.starter.selector;

import org.springframework.context.annotation.ComponentScan;

/**
 * 自动引入系統組件
 * 
 * @author lifeng
 */
@ComponentScan(value = { "com.tmt.system", "com.tmt.gen" })
public class AutoConfigurationSystemComponents {
}
