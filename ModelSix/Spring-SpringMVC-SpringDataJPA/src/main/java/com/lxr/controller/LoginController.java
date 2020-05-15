package com.lxr.controller;

import com.lxr.service.LoginService;
import com.lxr.servicePojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

/**
 * @author lvxinran
 * @date 2020/4/17
 * @discribe
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;
    @RequestMapping(value = "/userLogin",method = RequestMethod.GET)
    public String login(){
        System.out.println("请求登录页面");
        return "login";
    }
    @RequestMapping(value = "/toLogin",method = RequestMethod.POST)
    public String toLogin(User user, HttpSession session, Model model){
        boolean login = loginService.login(user.getUsername(), user.getPassword());
        if(login){
            session.setAttribute("USER_SESSION",user);
            return "redirect:/resume/showAll";
        }
        model.addAttribute("msg","用户名或密码错误");
        return "login";
    }

}
