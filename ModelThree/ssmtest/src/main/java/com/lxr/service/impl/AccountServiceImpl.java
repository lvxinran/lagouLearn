package com.lxr.service.impl;

import com.lxr.mapper.AccountMapper;
import com.lxr.pojo.Account;
import com.lxr.service.AcccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lvxinran
 * @date 2020/4/16
 * @discribe
 */
@Service
@Transactional
public class AccountServiceImpl implements AcccountService {
    @Autowired
    private AccountMapper accountMapper;
    @Override
    public List<Account> queryAccountList() {
        return accountMapper.queryAccountList();
    }
}
