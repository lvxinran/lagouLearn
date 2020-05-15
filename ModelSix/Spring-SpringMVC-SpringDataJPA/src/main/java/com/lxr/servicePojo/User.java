package com.lxr.servicePojo;

import java.io.Serializable;

/**
 * @author lvxinran
 * @date 2020/4/17
 * @discribe
 */
public class User implements Serializable {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
