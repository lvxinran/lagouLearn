package com.lxr.sqlSession;

import com.lxr.config.BoundSql;
import com.lxr.pojo.Configuration;
import com.lxr.pojo.MappedStatement;
import com.lxr.util.GenericTokenParser;
import com.lxr.util.ParameterMapping;
import com.lxr.util.ParameterMappingTokenHandler;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lvxinran
 * @date 2020/3/27
 * @discribe
 */
public class SimpleExecutor implements Executor{


    private PreparedStatement preparedStatement;

    private void initPreparedStatement(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
//      1、注册驱动，获取链接
        Connection connection = configuration.getDataSource().getConnection();
        String sql = mappedStatement.getSql();
        //2、获取sql语句
        BoundSql boundSql = getBoundSql(sql);
        //3、预处理对象
        preparedStatement = connection.prepareStatement(boundSql.getSql());
        //4、设置参数
        String paramterType = mappedStatement.getParamterType();
        Class<?> clazz = getClassType(paramterType);
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        for (int i = 0; i < parameterMappings.size(); i++) {
            ParameterMapping parameterMapping = parameterMappings.get(i);
            String content = parameterMapping.getContent();
            if(clazz==Integer.class){
                preparedStatement.setObject(i + 1, params[0]);
            }else{
                Field declaredField= clazz.getDeclaredField(content);
                declaredField.setAccessible(true);
                Object o = declaredField.get(params[0]);
                preparedStatement.setObject(i + 1, o);
            }
        }
    }
    @Override
    public <T> List<T> query(Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception {
        initPreparedStatement(configuration,mappedStatement,params);
        //5、执行
        ResultSet resultSet = preparedStatement.executeQuery();
        //6、封装成对象
        String resultType = mappedStatement.getResultType();
        Class<?> resultClazz= getClassType(resultType);
        List<Object> objects = new ArrayList<>();
        while(resultSet.next()){
            ResultSetMetaData metaData = resultSet.getMetaData();
            Object o = resultClazz.newInstance();
            for(int i = 1;i<=metaData.getColumnCount();i++){
                String columnName = metaData.getColumnName(i);
                Object object = resultSet.getObject(columnName);

                PropertyDescriptor descriptor = new PropertyDescriptor(columnName,resultClazz);
                Method writeMethod = descriptor.getWriteMethod();
                writeMethod.invoke(o,object);
            }
            objects.add(o);
        }
        return (List<T>) objects;
    }

    @Override
    public int update(Configuration configuration, MappedStatement ms, Object... params) throws Exception {
        initPreparedStatement(configuration,ms,params);
        int i = preparedStatement.executeUpdate();
        return i;
    }
    private Class<?> getClassType(String paramterType) throws ClassNotFoundException {
        if(paramterType!=null){
            Class<?> aClass = Class.forName(paramterType);
            return aClass;
        }
        return null;
    }

    private BoundSql getBoundSql(String sql){
        ParameterMappingTokenHandler parameterMappingTokenHandler = new ParameterMappingTokenHandler();
        GenericTokenParser parser = new GenericTokenParser("#{","}",parameterMappingTokenHandler);
        String parse = parser.parse(sql);
        List<ParameterMapping> parameterMappings = parameterMappingTokenHandler.getParameterMappings();
        BoundSql boundSql = new BoundSql(parse,parameterMappings);
        return boundSql;
    }
}
