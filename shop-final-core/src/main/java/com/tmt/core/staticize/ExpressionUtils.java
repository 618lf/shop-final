package com.tmt.core.staticize;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.ReflectivePropertyAccessor;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.tmt.core.utils.Maps;

/**
 * 利用spring 来支持的表达式工具类
 * 一般会使用freemarker来解决问题，
 * 但是在初始化配置文件时使用spring更方便
 * 类似：applicationCon.xml 中的变量
 * @author root
 */
public class ExpressionUtils {
	
	/**
	 * 返回值
	 * @param expression
	 * @return
	 */
	public static Object getValue(String expression) {
		Map<String, Object> root = Maps.newHashMap();
		return getValue(expression, root, Boolean.FALSE);
	}
	
	/**
	 * 通过spring表达式来获取动态的数据
	 * @param model
	 * @param expression
	 * @param throwEx
	 * @return
	 */
	public static Object getValue(String expression, Object model, boolean throwEx){
		ParserContext parserContext = new ParserContext() {
			public boolean isTemplate() {return true;}
			public String getExpressionPrefix() {return "#{";}
			public String getExpressionSuffix() {return "}";}
		};
		StandardEvaluationContext context= new StandardEvaluationContext();
		context.setRootObject(model);
		PropertyAccessor accessor1 = new MapAccessor();
		PropertyAccessor accessor2 = new ReflectivePropertyAccessor();
		List<PropertyAccessor> propertyAccessors = new ArrayList<PropertyAccessor>();
		propertyAccessors.add(accessor1);
		propertyAccessors.add(accessor2);
		context.setPropertyAccessors(propertyAccessors);
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression(expression, parserContext);
		try{
			return exp.getValue(context);
		}catch(SpelEvaluationException e){
			if (throwEx){
				throw new RuntimeException(e.getMessage(), e);
			}else{
				return null;
			}
		}
	}
}
