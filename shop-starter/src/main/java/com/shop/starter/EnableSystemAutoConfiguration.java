package com.shop.starter;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Import;

import com.shop.starter.selector.AutoConfigurationSystemComponents;

/**
 * 启用基础系统： 必须放在 @ApplicationBoot 的前面
 * 
 * @author lifeng
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(AutoConfigurationSystemComponents.class)
public @interface EnableSystemAutoConfiguration {
}