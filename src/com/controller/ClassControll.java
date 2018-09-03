package com.controller;
 

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.TransformedMap;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import util.Bean;
import util.ClassUtil;
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
		String keyName = "/class/list/" + packageName;
		List<?> list = null;
		Cache<String> cache = new CacheMapImpl();
		if(cache.containsKey(keyName)){
			list = (List<?>) cache.get(keyName);
		}else{
			list = ClassUtil.getPackageClassBean(packageName, true);
			if(list != null && list.size() > 0)
				cache.put(keyName, list, 120 * 1000);
		}
		Page page = Page.getPage(request);
		page.setNUM(list==null?-1:list.size());
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
		String args = getValue(request, "do_args");//String-sss@Bean-{"k":"v"}@Integer-111@Boolean-true, *
		String splitArg = getValue(request, "do_split_arg");
		String splitArr = getValue(request, "do_split_arr");
		splitArg = splitArg.length()==0 ? "-" : splitArg;
		splitArr = splitArr.length()==0 ? "@" : splitArr;
		
		if(args.length() > 0){
			echo(ClassUtil.doClassMethod(className, methodName, ClassUtil.parseObject(args, splitArr, splitArg)));
		}else{
			echo(ClassUtil.doClassMethod(className, methodName));
		}
	}
	/**
	 * 连续 反射 多注入 执行链 builder模式  代码注入？
	 */
	@RequestMapping("/docode.do")
	public void doCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String className = getValue(request, "do_class");
		String methodName = getValue(request, "do_method");
		String args = getValue(request, "do_args");//String-sss@Bean-{"k":"v"}@Integer-111@Boolean-true, *
		String splitArg = getValue(request, "do_split_arg");
		String splitArr = getValue(request, "do_split_arr");
		splitArg = splitArg.length()==0 ? "-" : splitArg;
		splitArr = splitArr.length()==0 ? "@" : splitArr;
		
		if(args.length() > 0){
			echo(ClassUtil.doClassMethod(className, methodName, ClassUtil.parseObject(args, splitArr, splitArg)));
		}else{
			echo(ClassUtil.doClassMethod(className, methodName));
		}
	}
	 
    
}