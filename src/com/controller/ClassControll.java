package com.controller;
 

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import util.Bean;
import util.ClassUtil;
import util.FileUtil;
import util.Tools; 


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
		List<Bean> list = ClassUtil.getPackage(packageName, true);
		Page page = Page.getPage(request);
		page.setNUM(list.size());
		echo(list, page);
	}
	@RequestMapping("/detail.do")
	public void fileDir(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String className = getValue(request, "PACKAGE");
		List<?> list = ClassUtil.getMethod(className);
		Page page = Page.getPage(request);
		page.setNUM(list.size());
		echo(list, page);
	} 
	
	 
    
}