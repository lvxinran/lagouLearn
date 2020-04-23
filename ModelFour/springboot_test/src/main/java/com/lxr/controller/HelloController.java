package com.lxr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lvxinran
 * @date 2020/4/20
 * @discribe
 */
@RestController
public class HelloController {

    @RequestMapping("/demo")
    public String demo(){
        return "hello";
    }

}
