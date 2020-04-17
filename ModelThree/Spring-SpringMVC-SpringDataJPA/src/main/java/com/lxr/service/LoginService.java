package com.lxr.service;

import org.springframework.stereotype.Service;

/**
 * @author lvxinran
 * @date 2020/4/17
 * @discribe
 */
@Service
public class LoginService {

    public boolean login(String username,String password){
        if("admin".equals(username)&&"admin".equals(password)){
            return true;
        }
        return false;
    }
}
