package com.walker.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * restful模式接口
 */
@Controller
@RequestMapping("/restful")
public class RestfulControll extends BaseControll{
 
	public RestfulControll() {
		super(RestfulControll.class, "student");

	}

	@RequestMapping(value="/{id}/make.do",method=RequestMethod.GET)
	public void get(HttpServletRequest request, HttpServletResponse response, @PathVariable String id) throws IOException{
	    echo(baseService.findOne("select * from student where id=? ", id));
	}

	@RequestMapping(value="/make.do",method=RequestMethod.POST) //, produces = "application/json")
	public void post(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String id = getValue(request, "ID");
		String name = getValue(request, "NAME");
		String time = getValue(request, "TIME");
		echo(baseService.executeSql("insert into student(id,name) values(?,?)", id, name));
	}
	
	@RequestMapping(value="/make.do",method=RequestMethod.PUT)
	public void put(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String id = getValue(request, "ID");
		String name = getValue(request, "NAME");
		String time = getValue(request, "TIME");
		echo(baseService.executeSql("update student set id=?,name=? where id=? ", id, name, id));
	}

	@RequestMapping(value="/{id}/make.do",method=RequestMethod.DELETE)
	public void delete(HttpServletRequest request, HttpServletResponse response, @PathVariable String id) throws IOException{
	    echo(baseService.executeSql("delete from student where id=? ", id));
	}

    
}