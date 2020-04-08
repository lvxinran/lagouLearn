package com.lagou.edu.pojo;

/**
 * @author 应癫
 */
public class Account {

    private String account;
    private String username;
    private int balance;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "account='" + account + '\'' +
                ", username='" + username + '\'' +
                ", balance=" + balance +
                '}';
    }
}
