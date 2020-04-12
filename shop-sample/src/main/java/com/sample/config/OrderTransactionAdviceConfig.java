package com.sample.config;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.TransactionInterceptor;

/**
 * 事务配置 -- 这种方式没配置成功过
 */
@Aspect
@Deprecated
//@Configuration
public class OrderTransactionAdviceConfig {

	@Bean
	public TransactionInterceptor orderTxAdvice(
			@Qualifier("orderTransactionManager") DataSourceTransactionManager orderTransactionManager) {

		// 开启事务
		DefaultTransactionAttribute txAttr_REQUIRED = new DefaultTransactionAttribute();
		txAttr_REQUIRED.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);

		// 只读
		DefaultTransactionAttribute txAttr_REQUIRED_READONLY = new DefaultTransactionAttribute();
		txAttr_REQUIRED_READONLY.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		txAttr_REQUIRED_READONLY.setReadOnly(true);

		// 事务配置
		NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
		source.addTransactionalMethod("save*", txAttr_REQUIRED);
		source.addTransactionalMethod("delete*", txAttr_REQUIRED);
		source.addTransactionalMethod("update*", txAttr_REQUIRED);
		source.addTransactionalMethod("exec*", txAttr_REQUIRED);
		source.addTransactionalMethod("set*", txAttr_REQUIRED);
		source.addTransactionalMethod("get*", txAttr_REQUIRED_READONLY);
		source.addTransactionalMethod("query*", txAttr_REQUIRED_READONLY);
		source.addTransactionalMethod("find*", txAttr_REQUIRED_READONLY);
		source.addTransactionalMethod("list*", txAttr_REQUIRED_READONLY);
		source.addTransactionalMethod("count*", txAttr_REQUIRED_READONLY);
		source.addTransactionalMethod("is*", txAttr_REQUIRED_READONLY);

		// 关联事务管理器
		return new TransactionInterceptor(orderTransactionManager, source);
	}

	@Bean
	public Advisor orderTxAdviceAdvisor(@Qualifier("orderTxAdvice") TransactionInterceptor orderTxAdvice) {
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression("execution (public * com.sample.service..*.*(..)))");
		return new DefaultPointcutAdvisor(pointcut, orderTxAdvice);
	}
}