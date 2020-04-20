# 第一部分

## 介绍

Spring分层开源框架，full-stack全栈，轻量级（不需要依赖第三方软件），无缝整合第三方框架

spring优势：

- 解耦
- aop，执行前后加入逻辑
- 声明式事务 @Transactional，底层通过aop实现
- 方便测试，直接注入
- 集成其他优秀框架
- 对jdk底层代码进行了很多封装
- 源码精妙清晰

spring模块化思想，数据处理模块，web模块，aop模块，core模块，test模块

## IoC（Inversion of Control）

IoC，控制反转，是个技术思想，不用自己来创建对象，spring帮我们实例化对象和管理

控制：对象实例化、管理权力

IoC是站在对象的角度，DI是在容器的角度，容器会把对象依赖的其他对象注入

## AOP（面向切面编程）

横向切割方法，减少重复代码。

## 传统开发方式问题分析：

- new 关键字将实现类和逻辑绑定在了一起
- service层需要添加事务控制，则会有很多重复代码

**解决：xml+工厂模式+反射技术**

工厂：读取解析xml，反射创建对象并存储，对外提供获取接口

xml：beans标签，bean标签，property标签

connection用了两次每次提交了一次事务，事务控制在dao层进行没在service层。

**解决：让两次update使用同一个connectinon，事务添加在service**

代码重复：使用动态代理来解决使用jdk动态代理或者cglib动态代理

# 第二部分

### Spring的IOC实现三种方式：

纯xml，xml+注解，纯注解

### 启动方式

#### 纯xml，xml+注解：

JavaSE应用：ClassPahtXmlApplicationContext("beans.xml");

JavaWeb应用：ContextLoaderListener （监听器加载xml）

#### 纯注解：

JavaSE应用：AnnotationConfigApplicationContext(SpringConfig.class)

JavaWeb应用：ContextLoaderListener （监听器加载注解配置类）

BeanFactory是IOC框架的顶层接口，ApplicationContext是BeanFactory的子接口，因此ApplicationContext具有BeanFactory的全部功能。BeanFactory也为IOC的基础容器，ApplicationContext是容器的高级接口，也是个容器。

web项目需要在web.xml进行监听，在servlet中需要用WebApplicationContextUtils.getWebApplicationContext方法获取容器。

## SpringIOC高级特性

### 延迟加载

默认在启动时将singleton bean初始化，如果lazy-init为true时在getBean才会初始化。如果设置为prototype尽管设置lazy-init也为在getBean时初始化。注解对应为@Lazy，对于不常用的bean来延迟加载。

### FactoryBean

可以让我们自定义bean创建过程（复杂创建）

### 后置处理器（扩展接口）

#### BeanFactoryPostProcesser

在BeanFactory初始化之后使用

#### BeanPostProcesser

 在Bean初始化之后可以使用（并不是在生命周期完成）

## 源码解析

### BeanFactory

getBean()  多态方法，基本行为

getBeanProvider() 获取是由哪个factory产生的

ListableBeanFactor（多了批量返回方法）-->BeanFactory

层级实现：ApplicationContext--->ResourceLoader（加载资源），HierarchicalBeanFactory（获取父工厂）

### 初始化主体流程

创建管理Bean对象，Bean有生命周期

 构造器、初始化方法在其中Bean后置处理器before、after方法

AbstractApplicationContext-->refresh()--->finishBeanFactoryInitialization()

Bean工厂后置处理器初始化、Bean工厂后置处理器执行

AbstractApplicationContext-->refresh()--->invokeBeanFactoryPostProcessors

Bean后置处理器初始化

AbstractApplicationContext-->refresh()--->registerBeanPostProcessors

**refresh() 方法** （核心方法）

方法被加锁，意为不能同时启动，同时关闭。

```java
obtainFreshBeanFactory();//创建工厂，将bean标签封装成BeanDefition，并且完成注册BeanDefitionRegistry(核心步骤)
```

```java
registerBeanPostProcessors();//注册Processer
```

```java
finishBeanFactoryInitialization(beanFactory);//（核心步骤）
```

大致主流程：（refresh中）创建工厂->工厂处理器->初始化bean

### BeanFactory创建流程

obtainFreshBeanFactory()流程：

```java
refreshBeanFactory();//AbstractRefreshableApplicationContext类中
```

```java
createBeanFactory();//refreshBeanFactory中
```

```java
loadBeanDefinitions();//加载注册
```

```java
getBeanFactory();//获取工厂
```

### BeanDefinition加载子流程

AbstractXmlApplicationContext中

```java
loadBeanDefinitions();
```

AbstractBeanDefinitionReader

```java
loadBeanDefinitions();//处理配置文件
```

XmlBeanDefinitionReader

```java
doLoadBeanDefinitions();//真正开始
```

```
registerBeanDefinitions();//解析配置文件完成，开始组装注册对象
```

DefaultBeanDefinitionDocumentReader

```
doRegisterBeanDefinitions();
```

```java
parseBeanDefinitions();
```

```java
parseDefaultElement();//处理各个标签
```

BeanDefinitionReaderUtils

```
registerBeanDefinition();//完成注册
```

DefaultListableBeanFactory

```java
this.beanDefinitionMap.put(beanName, beanDefinition);//注册最终
```

### AOP相关

在不改变原有业务逻辑的情况下，增强逻辑

连接点：方法开始时，结束时，正常运行完毕时，异常时等这些时机点。

切入点Pointcut：指定AOP思想想要影响的具体方法。

增强Advice：指的是横切逻辑

Aspect切面：是上述综合（切点+增强）

### 声明式事务

编程式事务：业务代码中加入事务控制代码

声明式事务：通过xml或者注解将事务代码和逻辑代码分开

#### 事务四大特性：

原子性：事务中的各个操作要么都成功，要么都失败

一致性：数据库状态变换到哦另一个一致的状态（转账）

隔离性：每个事务不被其他的事务所干扰

持久性：提交了就是永久性的

#### 事务隔离级别：

不考虑隔离级别会出现以下情况

脏读：一个线程中的事务读到了另外一个线程未提交的事务

不可重复读：一个线程中的事务读到了另外一个线程中的update数据（数据不同）

幻读：一个线程中的事务读到了另外一个线程中的insert或delete数据（条数不同）

数据库四个隔离级别：

Serializable(串行化)：可避免幻读、脏读、不可重复读 （最高）

Repeatable read(可重复读)：可避免脏读、不可重复读，这个下会将update的行进行加锁（第二）

Read commited(读已提交)：可避免脏读（第三）

Read uncommitted(读未提交)：全不可避免（第四）

事务传播行为：

A调用B，在B的角度看问题