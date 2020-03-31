package com.lxr.sqlSession;

import com.lxr.pojo.Configuration;
import com.lxr.pojo.MappedStatement;

import java.sql.SQLException;
import java.util.List;

public interface Executor {
    public <T> List<T> query(Configuration configuration, MappedStatement mappedStatement,Object... params) throws Exception;

    int update(Configuration configuration,MappedStatement ms, Object... params) throws Exception;

}
