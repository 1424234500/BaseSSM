package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mode.LoginUser;


@Controller
@RequestMapping("/login")
public class LoginControll {
 
	@RequestMapping("/onlogin.do")
	public String  onlogin(HttpServletRequest request, Map<String,Object> map) {
		return "login";
	}
	
	@RequestMapping("/loginin.do")
	public void loginin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		request.setCharacterEncoding("UTF-8"); 
		response.setCharacterEncoding("UTF-8"); 

		request.getSession().setAttribute("SY_LOGINUSER", LoginUser.getUser().setId("test").setKey("key").setUsername("username"));

		int res = 1;
		 
		response.getWriter().write("" + res); 
	}
 
    
}