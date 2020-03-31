package com.lxr.pojo;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lvxinran
 * @date 2020/3/27
 * @discribe
 */
public class Configuration {
    private DataSource dataSource;

    private Map<String,MappedStatement> mappedStatementMap= new HashMap<>();

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public Map<String, MappedStatement> getMappedStatementMap() {
        return mappedStatementMap;
    }
}
