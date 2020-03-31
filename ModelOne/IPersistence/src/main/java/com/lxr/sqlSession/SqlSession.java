package com.lxr.sqlSession;

import java.sql.SQLException;
import java.util.List;

public interface SqlSession {

    public <E> List<E> selectList(String statementId,Object... param) throws Exception;

    public <T> T selectOne(String statementId,Object... param) throws Exception;

    public <T> T getMapper(Class<?> mapperClass);

    public int updateOne(String statementId,Object... param) throws Exception;
}
