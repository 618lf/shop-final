package com.tmt.core.utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.tmt.core.entity.BaseParentEntity;
import com.tmt.core.entity.BaseTreeEntity;

/**
 * 树实例的工具类
 * @author root
 *
 */
public class TreeEntityUtils {

	//---------------------排序服务--------------------------------------
	/**
	 * 排序
	 * @param entities
	 * @return
	 */
	public static <T extends BaseTreeEntity<Long>> List<T> sort(List<T> entities) {
		//先排个序
		sortAsc(entities);
		List<T> copys = Lists.newArrayList();
		Map<Integer,List<T>> menuMap = classifyByLevel(entities);
		int firstLevel = 1;
		List<T> menuList = menuMap.get(firstLevel);
		if (menuList != null) {
			for(T menu: menuList) {
				copys.add(menu);
				List<T> child = sort(menu, firstLevel+1, menuMap);
				if(child != null && child.size() != 0) {
				   copys.addAll(child);
				   menu.setIsLeaf(Boolean.FALSE);
				}
			}
		}
		return copys;
	}
	
	private static <T extends BaseTreeEntity<Long>> List<T> sort(T parent, int level, Map<Integer,List<T>> menuMap) {
		List<T> copyMenus = Lists.newArrayList();
		List<T> menuList = menuMap.get(level);
		if(menuList != null ) {
			for(T menu: menuList ) {
				if(menu.getParentId().compareTo(parent.getId()) == 0) {
				   copyMenus.add(menu);
				   List<T> child = sort(menu, level+1, menuMap);
				   if(child != null && child.size() != 0) {
					  copyMenus.addAll(child);
					  menu.setIsLeaf(Boolean.FALSE);
				   }
				}
			}
		}
		return copyMenus;
	}
	
	/**
	 * 升序排列
	 * @param entities
	 * @return
	 */
	public static <T extends BaseParentEntity<Long>> List<T> sortAsc(List<T> entities) {
		Collections.sort(entities, new Comparator<T>() {
			@Override
			public int compare(T o1, T o2) {
				int i = o1.getLevel().compareTo(o2.getLevel());
				if( i == 0) {
					i = o1.getSort().compareTo(o2.getSort());
				}
				return i;
			}
		});
		return entities;
	}
	
	//----------------按级别存储-----------------------------
	public static <T extends BaseParentEntity<Long>> Map<Integer,List<T>> classifyByLevel(List<T> entities){
		Map<Integer,List<T>> menuMap = Maps.newHashMap();
		for(T menu: entities ) {
			if(!menuMap.containsKey(menu.getLevel()) ) {
				List<T> menus = Lists.newArrayList();
				menuMap.put(menu.getLevel(), menus);
			}
			menuMap.get(menu.getLevel()).add(menu);
		}
		return menuMap;
	}
	public static <T extends BaseParentEntity<Long>> Map<Integer,List<T>> classifyByLevel(List<T> entities, Filter<T> filter){
		Map<Integer,List<T>> menuMap = Maps.newHashMap();
		for (T menu: entities) {
			if (filter.filter(menu)) {
			   if (!menuMap.containsKey(menu.getLevel()) ) {
					List<T> menus = Lists.newArrayList();
					menuMap.put(menu.getLevel(), menus);
			   }
			   menuMap.get(menu.getLevel()).add(menu);
			}
		}
		return menuMap;
	}
	public static interface Filter<T extends BaseParentEntity<Long>> { 
		public Boolean filter(T menu);
	}
	//----------------------展示服务-----------------------------
	/**
	 * 展示服务
	 * template --- 模板格式
	 * <li><a class="XXX" data-id="${menu.id}" data-pid="${menu.parentId}">${menu.name}<span class="options"><small class="add"><i class="fa fa-plus"></i></small><small class="moveup"><i class="fa fa-arrow-circle-up"></i></small><small class="del"><i class="fa fa-remove"></i></small></span></a>${children}</li>
	 * @param entities
	 * @param template
	 * @return
	 */
	public static <T extends BaseParentEntity<Long>> String getDisplayTree(List<T> entities, String template) {
		Map<Integer,List<T>> menuMap = classifyByLevel(entities);
		StringBuffer menuString = new StringBuffer();
		int firstLevel = 1;
		List<T> firstMenus = menuMap.get(firstLevel); //level从1开始
		if(firstMenus == null){ return "";}
		for( T menu: firstMenus ) {
			 menuString.append(getContentTags(menu, firstLevel, menuMap, template));
		}
		return menuString.toString();
	}
	
	private static <T extends BaseParentEntity<Long>> String getContentTags(T menu, int level, Map<Integer,List<T>> menuMap, String template) {
		Map<String, Object> context = getContent();
		context.put("menu", menu);
		context.put("children", getChildMenu(menu,level+1,menuMap, template));
		return FreemarkerUtils.processNoTemplate(template, context);
	}
	
	//展示子菜单
	private static <T extends BaseParentEntity<Long>> String getChildMenu(T parent, int level, Map<Integer,List<T>> menuMap, String template){
		List<T> menuList = menuMap.get(level);
		if(menuList == null){ return "";}
		StringBuffer menuString = null;
		for( T menu: menuList ) {
			if(menu.getParentId().compareTo(parent.getId()) == 0) {
				if( menuString == null) {
					menuString = new StringBuffer();
					menuString.append("<ul class='nav nav-sub'>");
				}
				menuString.append(getContentTags(menu, level, menuMap, template));
			}
		}
		if( menuString != null) {
			return menuString.append("</ul>").toString();
		}
		return "";
	}
	
	//允许传递执行环境
	private static Map<String, Object> getContent() {
		return Maps.newHashMap();
	}
}
