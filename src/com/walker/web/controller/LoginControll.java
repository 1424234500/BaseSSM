package com.walker.web.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.walker.service.LoginService;


@Controller
@RequestMapping("/login")
public class LoginControll extends BaseControll{
    @Autowired
	LoginService loginService;
    
	public LoginControll() {
		super(LoginControll.class, "");

	}

	@RequestMapping("/onlogin.do")
	public String  onlogin(HttpServletRequest request, Map<String,Object> map) {
		return "login";
	}
	
	@RequestMapping("/loginin.do")
	public void loginin(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String id = getValue(request, "id");
		String pwd = getValue(request, "pwd");
		
		echo(loginService.login(id, pwd));
	}
 
    
}