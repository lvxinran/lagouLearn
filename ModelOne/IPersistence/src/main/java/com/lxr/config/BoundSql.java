package com.lxr.config;

import com.lxr.util.ParameterMapping;

import java.util.List;

/**
 * @author lvxinran
 * @date 2020/3/27
 * @discribe
 */
public class BoundSql {
    private String sql;
    List<ParameterMapping> parameterMappings;
    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public List<ParameterMapping> getParameterMappings() {
        return parameterMappings;
    }

    public void setParameterMappings(List<ParameterMapping> parameterMappings) {
        this.parameterMappings = parameterMappings;
    }

    public BoundSql(String sql, List<ParameterMapping> parameterMappings) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
    }
}
