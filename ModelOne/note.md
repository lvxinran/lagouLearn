## 第一部分

orm框架是对象-关系映射框架

jdbc存在硬编码问题，用配置文件解决，存在不断开启释放连接的问题，使用数据库连接池解决。

自定义orm框架

先将xml进行解析，将mapper的路径放到sqlMapperConfig中，可以一起读出来。

封装参数时要得出Class和里面的字段，来赋值。

## 第二部分

两种方法，使用接口实现类和使用动态代理来实现。

properties标签可以读取properties文件，可以将数据库配置写到里面。

typeAliases标签可以简化时用到的类型参数

动态sql解决了拼装sql语句的问题

一级缓存最终是使用了Map结构，在执行事务后会刷新。

二级缓存可以给予redis实现。

Mybatis插件其实都是实现了interceptor接口，都是拦截器。

## 第三部分

架构分为3部分，接口层，数据处理层，框架支撑层。

总体流程为4步。加载配置初始化，接受调用请求，处理请求，返回结果。

Executor将配置文件解析成MapperStatment，内部调用jdbc使用StatementHandler实现

StatementHandler内流程为先调用paramHandler处理BoundSql传入的参数，然后使用TypeHandler对参数和jdbcTyep类型进行对应，最后生成statment对象 执行query方法返回结果集，结果集使用TypeHandler进行解析成java对象，然后ResultHandler进行对结果集最后的封装，返回list。