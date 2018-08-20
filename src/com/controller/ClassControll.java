package com.controller;
 

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import util.Bean;
import util.ClassUtil;
import util.FileUtil;
import util.JsonUtil;
import util.Tools;
import util.cache.Cache;
import util.cache.CacheMapImpl; 


@Controller
@RequestMapping("/class")
public class ClassControll extends BaseControll{
	public ClassControll() {
		super(ClassControll.class, "");
	}
	
	@RequestMapping("/list.do")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//page, package -> page, list
		String packageName = getValue(request, "package");
		List<?> list = null;
		Cache<String> cache = new CacheMapImpl();
		if(cache.containsKey("/class/list")){
			list = (List<?>) cache.get("/class/list");
		}else{
			list = ClassUtil.getPackage(packageName, true);
			cache.put("/class/list", list, 120 * 1000);
		}
		Page page = Page.getPage(request);
		page.setNUM(list.size());
		echo(list, page);
	}
	@RequestMapping("/detail.do")
	public void fileDir(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String className = getValue(request, "PACKAGE");
		List<?> list = null;
		
		Cache<String> cache = new CacheMapImpl();
		Bean bean = cache.get("/class/detail", new Bean());

		String toName = className.replace('.', '-');
		if(bean.containsKey(toName)){ 
			list = (List<?>) bean.get(toName);
		}else{//若子级未存过 detail map
			list = ClassUtil.getMethod(className, true, true);
			bean.put(toName, list);
			cache.put("/class/detail", bean);
		}
		Page page = Page.getPage(request);
		page.setNUM(list.size());
		echo(list, page);
	}
	
	@RequestMapping("/do.do")
	public void doMethod(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String className = getValue(request, "do_class");
		String methodName = getValue(request, "do_method");
		String args = getValue(request, "do_args");//[sss, ssaa, lll, *]
		args = "[" + args + "]";
		//参数顺序问题 
		List<?> objs = JsonUtil.getList(args);

		echo(ClassUtil.doClassMethod(className, methodName, objs.toArray()));
	}
	
	 
    
}