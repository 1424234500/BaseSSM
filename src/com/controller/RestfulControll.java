package com.controller;

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

	@RequestMapping(value="/make.do/{id}",method=RequestMethod.GET)
	public void get(HttpServletRequest request, HttpServletResponse response, @PathVariable Long id) throws IOException{
	    echo(baseService.findOne("select * from student where id=? ", id));
	}

	@RequestMapping(value="/make.do",method=RequestMethod.POST) //, produces = "application/json")
	public void get(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String id = getValue(request, "id");
		String name = getValue(request, "name");
		String time = getValue(request, "time");
		echo(baseService.executeSql("insert into student values(?,?,?)", id, name, time));
	}
	
	@RequestMapping(value="/make.do",method=RequestMethod.PUT)
	public void put(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String id = getValue(request, "id");
		String name = getValue(request, "name");
		String time = getValue(request, "time");
		echo(baseService.executeSql("update student set id=?,name=?,time=? where id=? ", id, name, time, id));
	}

	@RequestMapping(value="/make.do/{id}",method=RequestMethod.DELETE)
	public void delete(HttpServletRequest request, HttpServletResponse response, @PathVariable Long id) throws IOException{
	    echo(baseService.findOne("delete from student where id=? ", id));
	}

    
}