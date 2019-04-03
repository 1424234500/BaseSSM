package com.walker.web.mode;

import java.util.Date;

import org.springframework.stereotype.Component;

@Component
public class User {
    private String id;

    private String username;

    private String email;

    private String sex;

    private String pwd;

    private String profilepath;

    private String profilepathwall;

    private String sign;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    public String getProfilepath() {
        return profilepath;
    }

    public void setProfilepath(String profilepath) {
        this.profilepath = profilepath == null ? null : profilepath.trim();
    }

    public String getProfilepathwall() {
        return profilepathwall;
    }

    public void setProfilepathwall(String profilepathwall) {
        this.profilepathwall = profilepathwall == null ? null : profilepathwall.trim();
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign == null ? null : sign.trim();
    }
}