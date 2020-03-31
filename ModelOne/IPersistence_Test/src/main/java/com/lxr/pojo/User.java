package com.lxr.pojo;

/**
 * @author lvxinran
 * @date 2020/3/27
 * @discribe
 */
public class User {
    private int id;
    private  String username;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}
