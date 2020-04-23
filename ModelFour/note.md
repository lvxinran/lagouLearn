# SpringBoot

## SpringBoot基础

springboot是一种快速使用spring的方式。约定优于配置思想，没有进行配置时，用假定合理的默认值代替配置。

### spring缺点

缺点：

1.配置文件配置内容繁琐

2.项目依赖以及版本管理繁琐

### 核心

起步依赖

自动配置

配置文件响应注解

### 配置文件属性值和注入

相关注解：

@ConfigurationProperties(prefix=" ")加在类上

@Value("${persion.id}")加在属性上，不能注入除基本属性外的其他属性（很少用）

@Configuration自定义配置类 和@Bean 配合使用（推荐使用）

## SpringBoot源码分析

### 依赖管理

在spring-boot-dependencies中指定了一些常用包的版本。-------引入不用指定版本

...-starter响应组件的启动依赖

### 自动配置

@SpringBootApplication注解内部包括了@SpringBootConfiguration 这里面又包含了@Configuration注解 表明该类为配置类

#### @EnableAutoConfiguration

```java
@AutoConfigurationPackage
@Import(AutoConfigurationImportSelector.class)
```

#### @AutoConfigurationPackage

```java
@Import(AutoConfigurationPackages.Registrar.class)
```

```java
static class Registrar implements ImportBeanDefinitionRegistrar, DeterminableImports {

   @Override
   public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
      register(registry, new PackageImport(metadata).getPackageName());
   }

   @Override
   public Set<Object> determineImports(AnnotationMetadata metadata) {
      return Collections.singleton(new PackageImport(metadata));
   }

}
```

AutoConfigurationPackage会把@SpringBootApplication标注的类所在的包名拿到，并且对该包及其子包进行扫描，将组件添加到容器中。（因此需要将启动类放到外面一个包中）

#### @Import(AutoConfigurationImportSelector.class)

```java
@Override
public String[] selectImports(AnnotationMetadata annotationMetadata) {
   if (!isEnabled(annotationMetadata)) {
      return NO_IMPORTS;
   }
   AutoConfigurationMetadata autoConfigurationMetadata = AutoConfigurationMetadataLoader
         .loadMetadata(this.beanClassLoader);
   AutoConfigurationEntry autoConfigurationEntry = getAutoConfigurationEntry(autoConfigurationMetadata,
         annotationMetadata);
   return StringUtils.toStringArray(autoConfigurationEntry.getConfigurations());
}
```

```java
AutoConfigurationMetadata autoConfigurationMetadata = AutoConfigurationMetadataLoader
      .loadMetadata(this.beanClassLoader);
```

加载配置文件

```java
protected static final String PATH = "META-INF/spring-autoconfigure-metadata.properties";

static AutoConfigurationMetadata loadMetadata(ClassLoader classLoader) {
   return loadMetadata(classLoader, PATH);
}
```

```java
static AutoConfigurationMetadata loadMetadata(ClassLoader classLoader, String path) {
   try {
      Enumeration<URL> urls = (classLoader != null) ? classLoader.getResources(path)
            : ClassLoader.getSystemResources(path);
      Properties properties = new Properties();
      while (urls.hasMoreElements()) {
         properties.putAll(PropertiesLoaderUtils.loadProperties(new UrlResource(urls.nextElement())));
      }
      return loadMetadata(properties);
   }
   catch (IOException ex) {
      throw new IllegalArgumentException("Unable to load @ConditionalOnClass location [" + path + "]", ex);
   }
}
```

通过properties文件将需要加载的类放到AutoConfigurationMetadata中。

```java
protected AutoConfigurationEntry getAutoConfigurationEntry(AutoConfigurationMetadata autoConfigurationMetadata,
      AnnotationMetadata annotationMetadata) {
	...
   List<String> configurations = getCandidateConfigurations(annotationMetadata, attributes);
 ...
}
```

将spring.factories配置下的EnableAutoConfiguration下的所有类名放到configurations中

```java
configurations = filter(configurations, autoConfigurationMetadata);
```

对pom.xml中依赖进行最后筛查，去除无用配置类，剩余的是要自动配置的。

@ConditionalOnClass表示需要在类路径中存在这几个类名才会完成当前配置类的自动配置。

```java
# Auto Configuration Import Listeners
org.springframework.boot.autoconfigure.AutoConfigurationImportListener=\
org.springframework.boot.autoconfigure.condition.ConditionEvaluationReportAutoConfigurationImportListener
```

配置完成之后调用listener提示完成。

#### @ComponentScan

包扫描器，开启包扫描

## 自定义starter

- starter可以理解为一个可插拔式的插件
- springBoot可以对这些starter进行默认配置

流程：

1. 引入依赖，autoConfigure
2. 编写javaBean
3. 编写配置类AutoConfiguration
4. 创建spring.factories

## SpingBoot启动流程分析

分为两个部分，实例化SpringApplication对象，调用这个对象run方法

### 初始化部分

设置应用类型SERVLET、REACTIVE、NONE（springmvc应用为SERVLET）

```java
this.webApplicationType = WebApplicationType.deduceFromClasspath();
```

设置初始化器，根据传进来的ApplicationContextInitializer.class去spring.factories中寻找以这个类开头的配置类路径。

```java
setInitializers((Collection) getSpringFactoriesInstances(ApplicationContextInitializer.class));
```

设置监听器，贯穿Spring Boot整个生命周期根据传进来的ApplicationListener.class去spring.factories中寻找以这个类开头的配置类路径。

```java
setListeners((Collection) getSpringFactoriesInstances(ApplicationListener.class));
```

初始化mainApplicationClass这个属性，用于推断并设置项目main方法启动的主程序类。

```java
this.mainApplicationClass = deduceMainApplicationClass();
```

### run部分

#### 获取并启动监听器

```java
SpringApplicationRunListeners listeners = getRunListeners(args);
listeners.starting();
```

根据SpringApplicationRunListener类型去spring.factories中寻找对应类开头的监听器

```java
private SpringApplicationRunListeners getRunListeners(String[] args) {
   Class<?>[] types = new Class<?>[] { SpringApplication.class, String[].class };
   return new SpringApplicationRunListeners(logger,
         getSpringFactoriesInstances(SpringApplicationRunListener.class, types, this, args));
}
```

#### 项目运行环境Environment的预配置

```
ConfigurableEnvironment environment = prepareEnvironment(listeners, applicationArguments);
```

```java
private ConfigurableEnvironment prepareEnvironment(SpringApplicationRunListeners listeners,
      ApplicationArguments applicationArguments) {
   // Create and configure the environment
    //获取或创建环境
   ConfigurableEnvironment environment = getOrCreateEnvironment();
    //配置环境
   configureEnvironment(environment, applicationArguments.getSourceArgs());
   ConfigurationPropertySources.attach(environment);
   //listeners环境准备
   listeners.environmentPrepared(environment);
   //绑定环境
   bindToSpringApplication(environment);
   if (!this.isCustomEnvironment) {
      environment = new EnvironmentConverter(getClassLoader()).convertEnvironmentIfNecessary(environment,
            deduceEnvironmentClass());
   }
   ConfigurationPropertySources.attach(environment);
   return environment;
}
```

#### 创建Spring容器

```java
context = createApplicationContext();
```

```java
protected ConfigurableApplicationContext createApplicationContext() {
   Class<?> contextClass = this.applicationContextClass;
   if (contextClass == null) {
      try {
         switch (this.webApplicationType) {
         case SERVLET:
            contextClass = Class.forName(DEFAULT_SERVLET_WEB_CONTEXT_CLASS);
            break;
         case REACTIVE:
            contextClass = Class.forName(DEFAULT_REACTIVE_WEB_CONTEXT_CLASS);
            break;
         default:
            contextClass = Class.forName(DEFAULT_CONTEXT_CLASS);
         }
      }
      catch (ClassNotFoundException ex) {
         throw new IllegalStateException(
               "Unable create a default ApplicationContext, please specify an ApplicationContextClass", ex);
      }
   }
   return (ConfigurableApplicationContext) BeanUtils.instantiateClass(contextClass);
}
```

#### Spring容器前置处理

```java
prepareContext(context, environment, listeners, applicationArguments, printedBanner);
```

```java
private void prepareContext(ConfigurableApplicationContext context, ConfigurableEnvironment environment,
      SpringApplicationRunListeners listeners, ApplicationArguments applicationArguments, Banner printedBanner) {
	...
    //加载启动类
   load(context, sources.toArray(new Object[0]));
   listeners.contextLoaded(context);
}
```

#### 刷新容器

```java
refreshContext(context);
```

```java
private void refreshContext(ConfigurableApplicationContext context) {
    //初始化容器
   refresh(context);
   if (this.registerShutdownHook) {
      try {
          //JVM关闭时关闭上下文对象
         context.registerShutdownHook();
      }
      catch (AccessControlException ex) {
         // Not allowed in some environments.
      }
   }
}
```

#### 后置处理

```java
afterRefresh(context, applicationArguments);
```

无逻辑，可拓展

#### 发出结束执行的事件通知

```java
listeners.started(context);
```

#### 执行Runners

进行业务初始化，只执行一次

```java
callRunners(context, applicationArguments);
```

```java
private void callRunners(ApplicationContext context, ApplicationArguments args) {
    //获得所有Runner（两种）
   List<Object> runners = new ArrayList<>();
   runners.addAll(context.getBeansOfType(ApplicationRunner.class).values());
   runners.addAll(context.getBeansOfType(CommandLineRunner.class).values());
   AnnotationAwareOrderComparator.sort(runners);
   for (Object runner : new LinkedHashSet<>(runners)) {
      if (runner instanceof ApplicationRunner) {
         callRunner((ApplicationRunner) runner, args);
      }
      if (runner instanceof CommandLineRunner) {
         callRunner((CommandLineRunner) runner, args);
      }
   }
}
```

发布应用上下文就绪事件

```java
listeners.running(context);
```

## SpringBoot进阶

### 整合mybatis

#### 注解整合

- 创建Mapper接口
- 根据接口的代理对象写测试方法

#### 配置文件整合

- 创建Mapper接口
- 创建xml映射文件
- 配置xml映射配置文件路径
- 单元测试

### 整合JPA 

- 添加依赖
- 编写ORM类
- 编写Repository接口
- 测试

### 整合redis

- 添加启动器依赖

- 编写实体类

  @Indexed 生成二级索引

- 编写Repository接口（继承CrudRepository）

### 视图技术

Springboot在对jsp上有一些限制，因此在SpringBoot项目中不使用jsp作为视图。

### Thymeleaf

是SpringBoot常用的模板引擎技术。

#### 语法

##### 标签（类似于el表达式）

th:insert 

th:replace

th:href

th:text 标签显示的文本内容

##### 标准表达式

${...} 变量表达式 *{...}选择变量表达式 #{...}消息表达式 @{...} 链接url表达式 ~{...} 片段表达式

#### 基本使用

- 引入依赖
- 配置参数
- 静态资源访问

### 缓存管理

 