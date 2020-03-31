package com.lxr.dao;

import com.lxr.pojo.User;

import java.util.List;

public interface IUserDao {

    List<User> findAll();

    User findOneByCondition(User user);

    int updateOne(User user);

    int insertOne(User user);

    int deleteOne(int id);

}
