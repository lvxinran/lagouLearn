# SpringMVC

## 基础

MVC模式是一种代码的组织形式或方式，Spring MVC是一个应用于表现层的框架，轻量级web框架

M:model模型（数据模型[pojo、vo、po]+业务模型）

V:view视图（jsp，html）

Controller:controller控制器

SpringMVC本质可以认为是对servlet的封装，简化开发

作用：接受请求、返回响应、跳转界面

### SpringMVC 和servlet区别

客户端--->DispatcherServlet控制器（全局只有一个servlet接受请求）

原生servlet每个模块都需要一个servlet

### SpringMVC配置文件

InternalResourceViewResolver----视图解析器prefix、suffix返回页面前缀后缀

mvc:annotation-driven---->处理器映射器，处理器适配器

### 开发过程：

1. 配置DispatcherServlet前端控制器
2. 开发处理具体逻辑的Handler（@Controller，@RequestMapping）
3. xml配置文件配置controller扫描，配置springMVC三大件
4. 将xml文件告诉springMVC（dispatcherServlet中设置参数）

### 请求处理流程

1. 用户请求到DispatcherServlet
2. 向HandlerMapping请求查询对应的Handler
3. 返回处理器执行链
4. DispatcherServlet请求执行Hanlder到HandlerAdapter
5. 执行Handler
6. 返回ModelAndView
7. 解析视图ViewResolver
8. 返回视图对象View
9. 渲染视图

好处DispatcherServlet只进行任务派发，方便组件解耦

### SpringMVC九大组件

1. HandlerMapping（处理器映射器）：存放了对应处理器，作用是方便找到请求对应的处理器Handler和Interceptor
2. HandlerAdapter（处理器适配器）：执行不同形式的Handler
3. HandlerExceptionResolver：异常处理器
4. ViewResolver（视图解析器）：将Controller返回的视图名进行解析添加前缀后缀，返回view
5. RequestToViewNameTranslator：作用是从请求中获取返回的的viewName，来查找view，如果Hanlder处理之后没有返回ViewName，要通过这个组件查找viewName
6. LocaleResolver：国际化组件
7. ThemeResolver：主题解析器，样式、图片
8. MultipartResolver：用于上传请求
9. FlashMapManager：完成重定向时的参数传递

### url-pattern配置

/：处理除以.jsp结尾的其他请求，包括静态资源

如果想去除静态资源的请求，需要在SpringMvc.xml配置文件中配置<mvc:default-servlet-handler/>（原理：添加之后会在上下文中定义一个DefaultServletHttpRequestHandler对象，对DispatcherServlet的url进行筛查，如果发现是静态资源，就把请求转web应用服务器来处理，如果不是再由SpringMVC进行处理）局限性：只能把资源放在webapp下

第二种，SpringMVC自己处理静态资源<mvc:resources location="" mapping=""/>

/*:拦截所有请求，包括.jsp(一般不使用)

*.xxx:拦截.xxx结尾的请求

### 数据输出机制

SpringMVC在Handler方法上传入Map、Model、ModelMap参数，并向这些参数中设置保存数据。

运行时的基本类型都是BindingAwareModelMap，它继承了ExtendedModelMap，ExtendedModelMap继承了ModelMap类，实现了Model接口

### 接收参数机制

- 在Handler方法中声明形参。
- 接收简单数据类型，直接声明形参即可，要求传递参数名和声明形参保持一致。或者使用@RequestParam("")进行转换，推荐使用包装类型，防止null出错
- 接收pojo类型参数，直接形参声明即可，类型就是pojo类型，形参名无所谓，但是传递的参数名一定和pojo的参数名保持一致
- 如果接收pojo包装类型（对象中有对象参数），直接声明形参即可，内部参数名需要一致如果不能定位，需要用属性.属性进行
- 如果接收日期类型，需要自定义一个DateConverter去实现Converter接口，重写convert方法，之后再xml中注册bean ，class="FormattiongConversionServoiceFactory"，在annotation-driven中将conversion-service属性设置成该id

### Restful风格请求

rest是一个url请求风格，认为互联网中所有的东西都是资源，资源 表现层 状态转移

get：查询，获取资源

post：增加，新建资源

put：更新

delete：删除资源

利用@PathVariable进行对应参数

解决post请求乱发需要在web.xml中配置编码过滤器

如果使用put和delete请求方式需要在web.xml中进行请求方式的转换，将post转换成put和delete（HiddenHttpMethodFilter）

### Ajax Json交互

前到后：ajax发送字符串，后台直接接收为pojo参数使用注解@RequestBody（只能用post方式）

后到前：后台返回pojo对象，前端接收为json，使用@ResponseBody不再走视图解析器，等同于response直接输出

### SpringMVC高级技术

1. servlet：处理Request和Response
2. 过滤器（filter）：在请求到达servlet之前进行过滤
3. 监听器（Listener）：做一些初始化工作，启动Spring容器；监听web的特定事件，一直在监听事件直到容器销毁
4. 拦截器（Interceptor）：表现层，只会拦截方法Handler，不会拦截静态资源的访问，配置在MVC自己的xml中，在Handler执行之前拦截，在跳转前拦截，在跳转后拦截。

拦截器使用：实现HandlerInterceptor接口，然后在springMVC配置文件中将拦截器配置进去

如果有多个拦截器拦截顺序为pre1->pre2->handler->post2->post1->render->after2->after1。pre1和pre2的顺序和配置文件相同。

### 文件上传

#### 客户端：

1.post请求

2.enctype="mulitpart/form-data"

3.file组件

#### 服务端：

1.配置文件定义多元素解析器（CommonsMultipartResolver）

2.设置路径

3.设置文件名

### 异常处理机制

@ExceptionHandler方法注解，只会对当前Controller生效

利用@ControllerAdvice可以对多个Controller进行处理

### SpringMVC重定向

转发：url不会变，参数不会对此事，一个请求

重定向：url会变，参数丢失，两个请求

使用return "redirect:"，参数使用RedirectAttributes.addFlashAttribute()，该属性会被放到session中，在跳转到页面之后消失

## 自定义框架

### servlet的init方法大致流程：

- 1.加载配置文件 springmvc.properties
- 2.扫描相关类，注解
- 3.初始化bean对象 实现ioc容器
- 4.实现依赖注入
- 5.构造一个HandlerMapping处理器映射器，配置好url和method建立映射关系

## SpringMVC源码分析

### DispatcherServlet结构

```java
DispatcherServlet extends FrameworkServlet
```

```java
FrameworkServlet extends HttpServletBean
```

```java
HttpServletBean extends HttpServlet
```

核心方法为FrameworkServlet中的doGet和doPost，其中调用了processRequest方法，里面主要调用了doService方法（DispatcherServlet中）里面调用doDispatch（进行主要流程）----重要方法

这行是来执行handler方法的调用

```java
mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
```

这行是来渲染前端页面

```java
processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
```

### doDispatch方法核心步骤（SpringMVC处理请求大致流程）

1. 调用getHandler获取执行链HadnlerExecutionChain
2. 调用getHandlerAdaper获取能够执行Handler的适配器
3. ha.handle适配器调用handle执行（总会返回一个ModelAndView）
4. 调用processDispatchResult完成视图渲染跳转

#### getHandler()方法细节

遍历handlerMappings，根据request找到能处理的handler

#### getHandlerAdapter()方法

里面也是遍历所有的handlerAdapters，根据.support()方法找到能支持当前handler的adapter

### SpringMVC九大组件

```java
@Nullable
private MultipartResolver multipartResolver;//多部件解析器（文件上传）

/** LocaleResolver used by this servlet. */
@Nullable
private LocaleResolver localeResolver;//国际化解析器

/** ThemeResolver used by this servlet. */
@Nullable
private ThemeResolver themeResolver;//主题解析器

/** List of HandlerMappings used by this servlet. */
@Nullable
private List<HandlerMapping> handlerMappings;//处理器映射器（重要）

/** List of HandlerAdapters used by this servlet. */
@Nullable
private List<HandlerAdapter> handlerAdapters;//处理器适配器（重要）

/** List of HandlerExceptionResolvers used by this servlet. */
@Nullable
private List<HandlerExceptionResolver> handlerExceptionResolvers;//异常解析器

/** RequestToViewNameTranslator used by this servlet. */
@Nullable
private RequestToViewNameTranslator viewNameTranslator;//默认视图名转换组件

/** FlashMapManager used by this servlet. */
@Nullable
private FlashMapManager flashMapManager;//flash属性管理组件

/** List of ViewResolvers used by this servlet. */
@Nullable
private List<ViewResolver> viewResolvers;//视图解析器
```

所有的组件都是定义了接口类型

#### 九大组件初始化

```java
@Override
protected void onRefresh(ApplicationContext context) {
   initStrategies(context);
}
```

Spring容器在启动时在AbstractApplicationContext调用了onRefresh，该方法由子类实现

```java
protected void initStrategies(ApplicationContext context) {
   initMultipartResolver(context);
   initLocaleResolver(context);
   initThemeResolver(context);
   initHandlerMappings(context);
   initHandlerAdapters(context);
   initHandlerExceptionResolvers(context);
   initRequestToViewNameTranslator(context);
   initViewResolvers(context);
   initFlashMapManager(context);
}
```

在initHandlerMappings中

```java
private void initHandlerMappings(ApplicationContext context) {
   this.handlerMappings = null;

   if (this.detectAllHandlerMappings) {
      // Find all HandlerMappings in the ApplicationContext, including ancestor contexts.
      Map<String, HandlerMapping> matchingBeans =
            BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerMapping.class, true, false);
      if (!matchingBeans.isEmpty()) {
         this.handlerMappings = new ArrayList<>(matchingBeans.values());
         // We keep HandlerMappings in sorted order.
         AnnotationAwareOrderComparator.sort(this.handlerMappings);
      }
   }
   else {
      try {
         HandlerMapping hm = context.getBean(HANDLER_MAPPING_BEAN_NAME, HandlerMapping.class);
         this.handlerMappings = Collections.singletonList(hm);
      }
      catch (NoSuchBeanDefinitionException ex) {
         // Ignore, we'll add a default HandlerMapping later.
      }
   }

   // Ensure we have at least one HandlerMapping, by registering
   // a default HandlerMapping if no other mappings are found.
   if (this.handlerMappings == null) {
      this.handlerMappings = getDefaultStrategies(context, HandlerMapping.class);
      if (logger.isTraceEnabled()) {
         logger.trace("No HandlerMappings declared for servlet '" + getServletName() +
               "': using default strategies from DispatcherServlet.properties");
      }
   }
}
```

如果detectAllHandlerMappings变量为true，先去Bean容器里面按照查找HandlerMapping类型去查找，false根据名称去找（一般不用），找不到用默认策略getDefaultStrategies，一般用true。

```java
protected <T> List<T> getDefaultStrategies(ApplicationContext context, Class<T> strategyInterface) {
   String key = strategyInterface.getName();
   String value = defaultStrategies.getProperty(key);
   if (value != null) {
      String[] classNames = StringUtils.commaDelimitedListToStringArray(value);
      List<T> strategies = new ArrayList<>(classNames.length);
      for (String className : classNames) {
         try {
            Class<?> clazz = ClassUtils.forName(className, DispatcherServlet.class.getClassLoader());
            Object strategy = createDefaultStrategy(context, clazz);
            strategies.add((T) strategy);
         }
         catch (ClassNotFoundException ex) {
            throw new BeanInitializationException(
                  "Could not find DispatcherServlet's default strategy class [" + className +
                  "] for interface [" + key + "]", ex);
         }
         catch (LinkageError err) {
            throw new BeanInitializationException(
                  "Unresolvable class definition for DispatcherServlet's default strategy class [" +
                  className + "] for interface [" + key + "]", err);
         }
      }
      return strategies;
   }
   else {
      return new LinkedList<>();
   }
}
```

大致根据一个properties文件找到对应类型进行初始化，其他组件流程相似。

#### Handler方法执行细节

```java
mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
```

```java
protected ModelAndView handleInternal(HttpServletRequest request,
      HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {

   ModelAndView mav;
   checkRequest(request);
	...
    mav = invokeHandlerMethod(request, response, handlerMethod);
          ...
   return mav;
}
```

```java
protected ModelAndView invokeHandlerMethod(HttpServletRequest request,
      HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {

   ServletWebRequest webRequest = new ServletWebRequest(request, response);
   try {
      WebDataBinderFactory binderFactory = getDataBinderFactory(handlerMethod);
      ModelFactory modelFactory = getModelFactory(handlerMethod, binderFactory);

      ServletInvocableHandlerMethod invocableMethod = createInvocableHandlerMethod(handlerMethod);
      if (this.argumentResolvers != null) {
         invocableMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
      }
      if (this.returnValueHandlers != null) {
         invocableMethod.setHandlerMethodReturnValueHandlers(this.returnValueHandlers);
      }
      invocableMethod.setDataBinderFactory(binderFactory);
      invocableMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);

      ModelAndViewContainer mavContainer = new ModelAndViewContainer();
      mavContainer.addAllAttributes(RequestContextUtils.getInputFlashMap(request));
      modelFactory.initModel(webRequest, mavContainer, invocableMethod);
      mavContainer.setIgnoreDefaultModelOnRedirect(this.ignoreDefaultModelOnRedirect);

      AsyncWebRequest asyncWebRequest = WebAsyncUtils.createAsyncWebRequest(request, response);
      asyncWebRequest.setTimeout(this.asyncRequestTimeout);

      WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
      asyncManager.setTaskExecutor(this.taskExecutor);
      asyncManager.setAsyncWebRequest(asyncWebRequest);
      asyncManager.registerCallableInterceptors(this.callableInterceptors);
      asyncManager.registerDeferredResultInterceptors(this.deferredResultInterceptors);

      if (asyncManager.hasConcurrentResult()) {
         Object result = asyncManager.getConcurrentResult();
         mavContainer = (ModelAndViewContainer) asyncManager.getConcurrentResultContext()[0];
         asyncManager.clearConcurrentResult();
         LogFormatUtils.traceDebug(logger, traceOn -> {
            String formatted = LogFormatUtils.formatValue(result, !traceOn);
            return "Resume with async result [" + formatted + "]";
         });
         invocableMethod = invocableMethod.wrapConcurrentResult(result);
      }

      invocableMethod.invokeAndHandle(webRequest, mavContainer);
      if (asyncManager.isConcurrentHandlingStarted()) {
         return null;
      }

      return getModelAndView(mavContainer, modelFactory, webRequest);
   }
   finally {
      webRequest.requestCompleted();
   }
}
```

对参数和结果进行封装

这里执行具体方法

```java
invocableMethod.invokeAndHandle(webRequest, mavContainer);
```

doInvoke()调用方法

```java
public Object invokeForRequest(NativeWebRequest request, @Nullable ModelAndViewContainer mavContainer,
      Object... providedArgs) throws Exception {

   Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);
   if (logger.isTraceEnabled()) {
      logger.trace("Arguments: " + Arrays.toString(args));
   }
   return doInvoke(args);
}
```

不同的参数需要不同的解析器。

#### 跳转页面渲染视图

```java
private void processDispatchResult(HttpServletRequest request, HttpServletResponse response,
      @Nullable HandlerExecutionChain mappedHandler, @Nullable ModelAndView mv,
      @Nullable Exception exception) throws Exception {

...
      render(mv, request, response);//渲染
 ...
}
```

判断是否是重定向或者转发，不同情况不同View实现

```java
protected View createView(String viewName, Locale locale) throws Exception {
   // If this resolver is not supposed to handle the given view,
   // return null to pass on to the next resolver in the chain.
   if (!canHandle(viewName, locale)) {
      return null;
   }

   // Check for special "redirect:" prefix.
   if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
      String redirectUrl = viewName.substring(REDIRECT_URL_PREFIX.length());
      RedirectView view = new RedirectView(redirectUrl,
            isRedirectContextRelative(), isRedirectHttp10Compatible());
      String[] hosts = getRedirectHosts();
      if (hosts != null) {
         view.setHosts(hosts);
      }
      return applyLifecycleMethods(REDIRECT_URL_PREFIX, view);
   }

   // Check for special "forward:" prefix.
   if (viewName.startsWith(FORWARD_URL_PREFIX)) {
      String forwardUrl = viewName.substring(FORWARD_URL_PREFIX.length());
      InternalResourceView view = new InternalResourceView(forwardUrl);
      return applyLifecycleMethods(FORWARD_URL_PREFIX, view);
   }

   // Else fall back to superclass implementation: calling loadView.
   return super.createView(viewName, locale);
}
```

将model中的值放入request中，jsp中可以从请求域中拿到数据

```java
protected void exposeModelAsRequestAttributes(Map<String, Object> model,
      HttpServletRequest request) throws Exception {

   model.forEach((name, value) -> {
      if (value != null) {
         request.setAttribute(name, value);
      }
      else {
         request.removeAttribute(name);
      }
   });
}
```

最后调用进行分发

```java
rd.forward(request, response);
```

# SpringData JPA

spring Data JPA是dao层框架，简化数据库开发的，作用和Mybatis框架一样。可以帮我们生成sql语句

## SpringData jpa 和JPA规范和Hibernate的关系

SpringData jpa 对JPA规范的封装，JPA规范依赖于Hibernate框架

## SpringData JPA使用

构建项目，导入jar坐标