package com.tmt.core.persistence.mapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;

import com.tmt.core.persistence.BaseDao;
import com.tmt.core.persistence.BaseDaoImpl;
import com.tmt.core.persistence.PageParameters;
import com.tmt.core.persistence.QueryCondition;

/**
 * Mapper 代理
 * 
 * @author lifeng
 */
public class MapperProxy<T, PK> extends BaseDaoImpl<T, PK> implements InvocationHandler {

	/**
	 * 需要设置 操作模板
	 * 
	 * @param sqlSessionTemplate
	 */
	public MapperProxy(SqlSessionTemplate sqlSessionTemplate) {
		super(sqlSessionTemplate);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		BaseDao<T, PK> me = this;
		String methodName = method.getName();

		try {
			if ("lock".equals(methodName) && args.length == 1) {
				me.lock((T) args[0]);
				return null;
			}

			if ("get".equals(methodName) && args.length == 1) {
				return me.get((PK) args[0]);
			}

			if ("update".equals(methodName) && args.length == 2
					&& args[0].getClass().getName().equals("java.lang.String")
					&& args[1].getClass().getName().equals("java.lang.Object")) {
				return new Integer(me.update((String) args[0], (T) args[1]));
			}

			if ("update".equals(methodName) && args.length == 1
					&& args[0].getClass().getName().equals("java.lang.Object")) {
				return new Integer(me.update((T) args[0]));
			}

			if ("delete".equals(methodName) && args.length == 2
					&& args[0].getClass().getName().equals("java.lang.String")
					&& args[1].getClass().getName().equals("java.lang.Object")) {
				return new Integer(me.delete((String) args[0], (T) args[1]));
			}

			if ("delete".equals(methodName) && args.length == 1
					&& args[0].getClass().getName().equals("java.lang.Object")) {
				return new Integer(me.delete((T) args[0]));
			}

			if ("insert".equals(methodName) && args.length == 1
					&& args[0].getClass().getName().equals("java.lang.Object")) {
				return me.insert((T) args[0]);
			}

			if ("insert".equals(methodName) && args.length == 2
					&& args[0].getClass().getName().equals("java.lang.String")
					&& args[1].getClass().getName().equals("java.lang.Object")) {
				return me.insert((String) args[0], (T) args[1]);
			}

			if ("exists".equals(methodName) && args.length == 1) {
				return new Boolean(me.exists((PK) args[0]));
			}

			if ("compareVersion".equals(methodName) && args.length == 1) {
				me.compareVersion((T) args[0]);
				return null;
			}

			if ("queryForLimitList".equals(methodName) && args.length == 3
					&& args[0].getClass().getName().equals("java.lang.String")
					&& args[1].getClass().getName().equals("java.lang.Object")
					&& args[2].getClass().getName().equals("int")) {
				return me.queryForLimitList((String) args[0], (Object) args[1], ((Number) args[2]).intValue());
			}

			if ("queryForLimitList".equals(methodName) && args.length == 2
					&& args[0].getClass().getName().equals("com.swak.persistence.QueryCondition")
					&& args[1].getClass().getName().equals("int")) {
				return me.queryForLimitList((QueryCondition) args[0], ((Number) args[1]).intValue());
			}

			if ("queryForLimitList".equals(methodName) && args.length == 3
					&& args[0].getClass().getName().equals("java.lang.String")
					&& args[1].getClass().getName().equals("java.util.Map")
					&& args[2].getClass().getName().equals("int")) {
				return me.queryForLimitList((String) args[0], (Map<String, ?>) args[1], ((Number) args[2]).intValue());
			}

			if ("queryForLimitList".equals(methodName) && args.length == 3
					&& args[0].getClass().getName().equals("java.lang.String")
					&& args[1].getClass().getName().equals("com.swak.persistence.QueryCondition")
					&& args[2].getClass().getName().equals("int")) {
				return me.queryForLimitList((String) args[0], (QueryCondition) args[1], ((Number) args[2]).intValue());
			}

			if ("queryForGenericsList".equals(methodName) && args.length == 2
					&& args[0].getClass().getName().equals("java.lang.String")
					&& args[1].getClass().getName().equals("java.lang.Object")) {
				return me.queryForGenericsList((String) args[0], (Object) args[1]);
			}

			if ("queryForGenericsList".equals(methodName) && args.length == 2
					&& args[0].getClass().getName().equals("java.lang.String")
					&& args[1].getClass().getName().equals("java.util.Map")) {
				return me.queryForGenericsList((String) args[0], (Map<String, ?>) args[1]);
			}

			if ("queryForMapPageList".equals(methodName) && args.length == 3) {
				return me.queryForMapPageList((String) args[0], (Map<String, ?>) args[1], (PageParameters) args[2]);
			}

			if ("getAll".equals(methodName) && args.length == 0) {
				return me.getAll();
			}

			if ("batchInsert".equals(methodName) && args.length == 1
					&& args[0].getClass().getName().equals("java.util.List")) {
				me.batchInsert((List<T>) args[0]);
				return null;
			}

			if ("batchInsert".equals(methodName) && args.length == 2
					&& args[0].getClass().getName().equals("java.lang.String")
					&& args[1].getClass().getName().equals("java.util.List")) {
				me.batchInsert((String) args[0], (List<T>) args[1]);
				return null;
			}

			if ("countByCondition".equals(methodName) && args.length == 1
					&& args[0].getClass().getName().equals("com.swak.persistence.QueryCondition")) {
				return me.countByCondition((QueryCondition) args[0]);
			}

			if ("countByCondition".equals(methodName) && args.length == 2
					&& args[0].getClass().getName().equals("java.lang.String")
					&& args[1].getClass().getName().equals("java.lang.Object")) {
				return me.countByCondition((String) args[0], (Object) args[1]);
			}

			if ("countByCondition".equals(methodName) && args.length == 2
					&& args[0].getClass().getName().equals("java.lang.String")
					&& args[1].getClass().getName().equals("com.swak.persistence.QueryCondition")) {
				return me.countByCondition((String) args[0], (QueryCondition) args[1]);
			}

			if ("countByCondition".equals(methodName) && args.length == 2
					&& args[0].getClass().getName().equals("java.lang.String")
					&& args[1].getClass().getName().equals("java.util.Map")) {
				return me.countByCondition((String) args[0], (Map<String, ?>) args[1]);
			}

			if ("queryByCondition".equals(methodName) && args.length == 1) {
				return me.queryByCondition((QueryCondition) args[0]);
			}

			if ("queryForList".equals(methodName) && args.length == 2
					&& args[0].getClass().getName().equals("java.lang.String")
					&& args[1].getClass().getName().equals("java.util.Map")) {
				return me.queryForList((String) args[0], (Map<String, ?>) args[1]);
			}

			if ("queryForList".equals(methodName) && args.length == 2
					&& args[0].getClass().getName().equals("java.lang.String")
					&& args[1].getClass().getName().equals("com.swak.persistence.QueryCondition")) {
				return me.queryForList((String) args[0], (QueryCondition) args[1]);
			}

			if ("queryForList".equals(methodName) && args.length == 2
					&& args[0].getClass().getName().equals("java.lang.String")
					&& args[1].getClass().getName().equals("java.lang.Object")) {
				return me.queryForList((String) args[0], (Object) args[1]);
			}

			if ("queryForList".equals(methodName) && args.length == 1
					&& args[0].getClass().getName().equals("java.lang.String")) {
				return me.queryForList((String) args[0]);
			}

			if ("queryForMapList".equals(methodName) && args.length == 2) {
				return me.queryForMapList((String) args[0], (Map<String, ?>) args[1]);
			}

			if ("queryForIdList".equals(methodName) && args.length == 2) {
				return me.queryForIdList((String) args[0], (Map<String, ?>) args[1]);
			}

			if ("queryForObject".equals(methodName) && args.length == 1
					&& args[0].getClass().getName().equals("com.swak.persistence.QueryCondition")) {
				return me.queryForObject((QueryCondition) args[0]);
			}

			if ("queryForObject".equals(methodName) && args.length == 1
					&& args[0].getClass().getName().equals("java.lang.String")) {
				return me.queryForObject((String) args[0]);
			}

			if ("queryForObject".equals(methodName) && args.length == 2
					&& args[0].getClass().getName().equals("java.lang.String")
					&& args[1].getClass().getName().equals("java.lang.Object")) {
				return me.queryForObject((String) args[0], (Object) args[1]);
			}

			if ("queryForObject".equals(methodName) && args.length == 2
					&& args[0].getClass().getName().equals("java.lang.String")
					&& args[1].getClass().getName().equals("java.util.Map")) {
				return me.queryForObject((String) args[0], (Map<String, ?>) args[1]);
			}

			if ("queryForAttr".equals(methodName) && args.length == 2) {
				return me.queryForAttr((String) args[0], (Object) args[1]);
			}

			if ("queryForPage".equals(methodName) && args.length == 2) {
				return me.queryForPage((QueryCondition) args[0], (PageParameters) args[1]);
			}

			if ("queryForPageList".equals(methodName) && args.length == 3
					&& args[0].getClass().getName().equals("java.lang.String")
					&& args[1].getClass().getName().equals("com.swak.persistence.QueryCondition")
					&& args[2].getClass().getName().equals("com.tmt.core.persistence.PageParameters")) {
				return me.queryForPageList((String) args[0], (QueryCondition) args[1], (PageParameters) args[2]);
			}

			if ("queryForPageList".equals(methodName) && args.length == 3
					&& args[0].getClass().getName().equals("java.lang.String")
					&& args[1].getClass().getName().equals("java.util.Map")
					&& args[2].getClass().getName().equals("com.tmt.core.persistence.PageParameters")) {
				return me.queryForPageList((String) args[0], (Map<String, ?>) args[1], (PageParameters) args[2]);
			}

			if ("batchUpdate".equals(methodName) && args.length == 1
					&& args[0].getClass().getName().equals("java.util.List")) {
				me.batchUpdate((List<T>) args[0]);
				return null;
			}

			if ("batchUpdate".equals(methodName) && args.length == 2
					&& args[0].getClass().getName().equals("java.lang.String")
					&& args[1].getClass().getName().equals("java.util.List")) {
				me.batchUpdate((String) args[0], (List<T>) args[1]);
				return null;
			}

			if ("batchDelete".equals(methodName) && args.length == 1) {
				me.batchDelete((List<T>) args[0]);
				return null;
			}
		} catch (Throwable e) {
			throw new InvocationTargetException(e);
		}

		throw new NoSuchMethodException("Not found method in class com.swak.wrapper.OrderDao.");
	}

	/**
	 * 创建代理类
	 * 
	 * @param <T>
	 * @param mapperInterface
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T newProxy(Class<T> mapperInterface, SqlSessionTemplate sqlSessionTemplate) {
		return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface },
				new MapperProxy(sqlSessionTemplate));
	}
}