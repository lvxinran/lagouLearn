package com.lxr.sqlSession;

import com.lxr.pojo.Configuration;

/**
 * @author lvxinran
 * @date 2020/3/27
 * @discribe
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {
    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration){
        this.configuration = configuration;
    }
    @Override
    public SqlSession openSession() {
        return new DefaultSqlSession(configuration);
    }
}
