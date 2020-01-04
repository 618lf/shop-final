package com.tmt.core.web.listener;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tmt.core.boot.Boot;
import com.tmt.core.utils.SpringContextHolder;

/**
 * 必须放在Spring 启动之后
 * 
 * @author root
 */
public class BootListener implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// 启动项
		Logger logger = LoggerFactory.getLogger(BootListener.class);
		logger.debug("========= system startup loading ====================");
		Map<String, Boot> boots = SpringContextHolder.getBeans(Boot.class);
		if (boots != null && !boots.isEmpty()) {
	    	Set<String> keys = boots.keySet();
	    	Iterator<String> it = keys.iterator();
	    	while(it.hasNext()){
	    		try {
	    			Boot realm = boots.get(it.next());
	    			logger.debug("Async loading - {}", realm.describe());
	    			realm.start();
				} catch (Exception e) {}
	    	}
	    }
		logger.debug("========= system startup loaded ====================");
	}

	/**
	 * 交给Spring 停止
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce) {}
}