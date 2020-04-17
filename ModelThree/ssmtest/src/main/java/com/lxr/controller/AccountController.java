package com.lxr.controller;

import com.lxr.pojo.Account;
import com.lxr.service.AcccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author lvxinran
 * @date 2020/4/16
 * @discribe
 */
@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AcccountService acccountService;
    @RequestMapping("/queryAll")
    @ResponseBody
    public List<Account> listAccount(){
        return  acccountService.queryAccountList();

    }
}
