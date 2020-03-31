package com.lxr.sqlSession;

import com.lxr.config.XMLConfigBuilder;
import com.lxr.pojo.Configuration;

import java.io.InputStream;

/**
 * @author lvxinran
 * @date 2020/3/27
 * @discribe
 */
public class SqlSessionFactoryBuilder {
    public SqlSessionFactory build(InputStream stream) throws Exception {
        XMLConfigBuilder builder = new XMLConfigBuilder();
        Configuration configuration = builder.parseConfig(stream);
        return new DefaultSqlSessionFactory(configuration);
    }
}
