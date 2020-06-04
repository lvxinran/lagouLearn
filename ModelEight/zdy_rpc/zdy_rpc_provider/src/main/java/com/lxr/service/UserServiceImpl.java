package com.lxr.service;


import org.springframework.stereotype.Service;


/**
 * @author lvxinran
 * @date 2020/5/25
 * @discribe
 */
@Service
public class UserServiceImpl implements UserService {
    @Override
    public String sayHello(String word) {
        System.out.println(word);
        return "success";

    }
}
