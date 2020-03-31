package com.lxr.sqlSession;

import com.lxr.pojo.Configuration;

import java.lang.reflect.*;
import java.util.List;

/**
 * @author lvxinran
 * @date 2020/3/27
 * @discribe
 */
public class DefaultSqlSession implements  SqlSession{
    private Configuration configuration;
    DefaultSqlSession(Configuration configuration){
        this.configuration = configuration;
    }
    @Override
    public <E> List<E> selectList(String statementId,Object... param) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        return simpleExecutor.query(configuration,configuration.getMappedStatementMap().get(statementId),param);
    }

    @Override
    public <T> T selectOne(String statementId,Object... param) throws Exception {
        List<Object> objects = selectList(statementId,param);
        if(objects.size()==1){
            return (T) objects.get(0);
        }else{
            throw new RuntimeException("无结果或结果多条");
        }
    }

    @Override
    public int updateOne(String statementId, Object... param) throws Exception {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        int result = simpleExecutor.update(configuration, configuration.getMappedStatementMap().get(statementId), param);
        return result;
    }
    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        Object instance = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{mapperClass},
                (proxy,method,args)-> {
                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();
                String statementId = className+"."+methodName;

                Type genericReturnType = method.getGenericReturnType();
                if(genericReturnType instanceof ParameterizedType)
                    return selectList(statementId,args);
                else if(genericReturnType ==Integer.TYPE)
                    return updateOne(statementId,args);

                return selectList(statementId,args);

        });
        return (T) instance;
    }
}
