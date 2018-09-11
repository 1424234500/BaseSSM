package com.event.listener;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.service.impl.FileServiceImpl;

import util.Bean;
import util.cache.Cache;
import util.cache.CacheFactory;

@Component
public class OnLoaded implements ApplicationListener<ContextRefreshedEvent> {
	static public Logger logger = LoggerFactory.getLogger("OnLoaded"); 

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		logger.info(event.toString());
		// 需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
		if(event.getApplicationContext().getParent() == null){
			//root application context 没有parent，他就是老大.
			logger.info("\n-----------------\n onload \n --1234567-- \n-------------\n"+event.toString());
			
			new FileServiceImpl().initDirs();
			
			List<Object> list = new ArrayList<>();
			list.add("string item");
			list.add(1111111);
			list.add(Bean.getBean().put("key of list map", "aldkjfakljf").put("keyint", 2222));
			Bean map = Bean.getBean().put("key1", 111).put("key2", 222);
			Bean bean = Bean.getBean().put("key1", 111).put("key2", map);
			Bean bean2 = Bean.getBean().put("key1", 333).put("key2", map).put("key3", bean);
			list.add(bean);
			list.add(bean2);
			list.add(map);
			
			Cache<String> cache = CacheFactory.getInstance();
			cache.put("int", 1);
			cache.put("long", 998);
			cache.put("string", "the is a string");
			cache.put("map", 
				Bean.getBean().put("key-int", 2)
					.put("key-map", Bean.getBean().put("key-map-key-int", 3))
					.put("key-list", list)
					.put("list", list)
					.put("map1", map)
					.put("map2", bean)
					.put("map3", bean2)
					);
			cache.put("list", list);
			
		}
	}
}