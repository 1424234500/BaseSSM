package com.walker.web.mode;

import org.springframework.stereotype.Component;
/**
 * 登录角色信息
 * @author Walker
 *
 */
@Component
public class LoginUser {
	
	public static LoginUser getUser(){
		return new LoginUser();
	}
	
    private String id;

    private String username;

    private String key;

	public String getId() {
		return id;
	}

	public LoginUser setId(String id) {
		this.id = id;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public LoginUser setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getKey() {
		return key;
	}

	public LoginUser setKey(String key) {
		this.key = key;
		return this;
	}

	@Override
	public String toString() {
		return "LoginUser [id=" + id + ", username=" + username + ", key=" + key + "]";
	}
 
}