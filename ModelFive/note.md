# Tomcat相关

## 使用及原理

### 目录结构

bin：startup.bat 、startup.sh、shutdown.bat、shutdown.sh 启动 关闭服务

conf：logging.properties 打印日志配置 、server.xml、tomcat-users.xml提供用户角色功能、web.xml（tomcat级别）

lib：基础jar包

logs：日志

temp：临时目录

webapps：发布项目目录

work：jsp编译运行产生过程文件目录

## 系统架构与原理剖析

b/s浏览器是客户端 发送http请求

### http请求流程：

1. 用户发起请求到浏览器
2. 浏览器发送tcp连接请求到服务器（socket处理）（http属于应用层协议，底层还是tcp协议）
3. 服务器接受请求，建立连接
4. 浏览器生成http数据包（请求头，请求体）
5. 浏览器发送请求数据包
6. 服务器解析Http格式数据包
7. 完成业务逻辑执行请求
8. 服务器生成Http格式数据包
9. 服务器发送数据包给浏览器
10. 浏览器解析http数据包
11. 浏览器呈现静态数据给用户

### 请求处理大致流程

http服务器转发请求到servlet容器（**并不是直接使用业务代码，进行解耦**），servlet注册到servlet容器中（通过注解或者web.xml），如果发现没有则进行加载servlet。 servlet接口和servlet容器称之为servlet规范。Tomcat实现了servlet规范，因此Tomcat是一个servlet容器。

#### Tomcat两个重要身份

1.http服务器

2.servlet容器

### Servlet容器

处理流程：

1. Http服务器把Request对象封装成ServletRequest对象发给servlet容器
2. servlet容器拿到请求后根据url映射关系，找到对应servlet
3. 如果servlet没被加载，就利用反射创建servlet并调用init方法进行初始化
4. 调用具体servlet的service处理请求，将处理结果用ServletResponse进行封装
5. 把ServletResponse对象返回给Http服务器，Http服务器把响应发给客户端

### Tomcat总体架构

又两大组件组成连接器组件(Connector) ，容器组件(Container)

连接器组件：和客户端交互（socket通信）------>对应http服务器功能

容器组件：处理业务逻辑------>对应servlet容器功能

其中俩着之间还有一些内容。

### 连接器组件Coyote

Coyote是对外接口

- Coyote封装了网络通信用来处理socket链接
- Coyote是的Catalina容器与具体的请求协议及IO方式完全解耦
- Coyote将socket输入转换封装为Request对象，然后交给容器处理封装成Response对象将结果写入输出流
- Coyote负责具体协议（应用层）和IO操作（传输层）

Tomcat支持的IO模型和协议

协议：http/1.1（Tomcat默认）、http/2、AJP

IO模型：NIO（Tomcat8.5默认）、NIO2、APR

### Coyote内部组件和流程

request----->EndPoint（TCP/IP）------>Processor（HTTP）-------->Adaper（将Request转换成ServletRequest）-------->Container

ProtocolHandler（包括EndPoint和Processor）根据不同的协议和IO有不同实现类

### Catalina容器

Tomcat是一个又一系列可配置的组件构成的web容器，而Catalina是Tomcat的Servlet容器，是Tomcat的核心。因此Tomcat本质上是一个servlet容器。也可以认为Tomcat是一个Catalina实例。

一个Catalina实例对应有一个Server实例，一个Server实例有多个Service实例，一个Service实例下有多个Connector和一个Container实例。

- Catalina：负责解析Tomcat的配置文件（server.xml），来创建Server组件
- Server：负责启动后面连接器和service
- Service：将多个Connector绑定到Container组件
- Container：负责处理用户Servlet请求，返回对象给web用户的模块

#### Container组件

Engine：来表Catalina的Servlet引擎，一个Service最多有一个Engine，可以包含多个Host

Host：代表一个虚拟主机，下面有多个Context

Context：表示一个Web应用程序，下面有多个Wrapper

Wrapper：表示一个Servlet，最底层，不包含子容器

体现在conf/server.xml中

### 配置文件详解

核心配置：conf/server.xml

它包含了Servlet容器的相关配置，即Catalina配置。

<Server>标签：

port：端口，管理Tomcat

shutDown：关闭服务器指定字符串

<Listener>标签：监听器，一般不需要改动

<GlobalNamingResource>标签：全局命名服务

<Service>标签：服务组件，默认只有一个

### Service标签详解

service下有这四种标签

<Executor>标签：定义共享线程池，给下面的字标签使用。（每个Connector来都会新建一个线程）

<Connector>标签：一个Service中可以定义多个Connector，连接相关。

<Connector port="8080" protocol="HTTP/1.1"
               connectionTimeout="20000"
               redirectPort="8443" 
			   IEncoding="utf-8" 
               URIEncoding="utf-8"/>

<Engine>标签：对应Catalina的引擎

<Listener>标签，不常用

### Executor标签

<Excetor name = "commonThreadPool"-----名字
			namePrefix="thread-exec-"-----线程名称前缀+一个数字
			maxThreads="200"------最大线程数
			minSpareThreads="100"-------活跃线程数（不会被销毁）
			maxIdleTime="60000"--------空闲线程：如果达到这个时间要被销毁
			maxQueueSize="Integer.MAX_VALUE"------排队线程数量
			prestartminSpareThreads="false"-----启动时是否启动活跃线程
			threadPriority="5"------线程优先级
			className="org.apache.catalina.core.StandardThreadExecutor" -----线程池实现类，默认实现为StandardThreadExecutor />

<Connector port="8080" ------监听端口
					protocol="HTTP/1.1"-------协议类型（无需改动）
					connectionTimeout="20000"-------超时时间
					redirectPort="8443"-----监听https请求
					executor="commonThreadPool"-----使用哪个线程池
					URLEncoding="UTF-8"------用于指定编码/>

### 容器标签

<Engine name="Catalina" -------用于指定Engine名称，默认为Catalina
			defaultHost="localHost"-------默认使用的虚拟主机的名称，对应Host标签的名称/>

<Host name="localHost"------名称
			appBase="webapps"-----资源相对路径（相对tomcat下webapp）
			unpackWARS="true"-----是否把war包解压成文件夹
			autoDeploy="true"----是否自动部署/>

<Valve />----打印连接日志相关

<Context>标签：用于配置一个Web应用（可配置多个）------在Host标签下

属性：

docBase：项目路径

path：请求访问路径

## Minicat实现

实现步骤

1. 提供服务，接收请求（Socket通信）
2. 请求信息封装成Request对象
3. 客户端请求资源，静态资源和动态资源
4. 资源返回给客户端浏览器

## Tomcat源码分析



## Tomcat类加载机制

java类--->字节码文件----->加载到jvm内存中（类加载过程）

### JVM类加载机制

引导类加载器、扩展类加载器、系统类加载器，树形结构

#### 双亲委派机制

先去父加载器加载，如果找不到再往下寻找。

##### 作用：

防止重复加载同一个.class

保证核心.class不被篡改，保证同一类名不被读取

而tomcat没有严格遵从这个机制，如果部署了两个项目，并且有相同全限定类名的jar，利用双亲委派机制是达不到效果的。



- 引导类加载器 和 扩展类加载器 的作⽤不变
- 系统类加载器正常情况下加载的是 CLASSPATH 下的类，但是 Tomcat 的启动脚本并未使⽤该变
- 量，⽽是加载tomcat启动的类，⽐如bootstrap.jar，通常在catalina.bat或者catalina.sh中指定。
- 位于CATALINA_HOME/bin下
- Common 通⽤类加载器加载Tomcat使⽤以及应⽤通⽤的⼀些类，位于CATALINA_HOME/lib下，
- ⽐如servlet-api.jar
- Catalina ClassLoader ⽤于加载服务器内部可⻅类，这些类应⽤程序不能访问
- Shared ClassLoader ⽤于加载应⽤程序共享类，这些类服务器不会依赖
- Webapp ClassLoader，每个应⽤程序都会有⼀个独⼀⽆⼆的Webapp ClassLoader，他⽤来加载

1. 本应⽤程序 /WEB-INF/classes 和 /WEB-INF/lib 下的类。
2. tomcat 8.5 默认改变了严格的双亲委派机制
3. ⾸先从 Bootstrap Classloader加载指定的类
4. 如果未加载到，则从 /WEB-INF/classes加载
5. 如果未加载到，则从 /WEB-INF/lib/*.jar 加载如果未加载到，则依次从 System、Common、Shared 加载（在这最后⼀步，遵从双亲委派机制）

## Tomcat对Https的支持及性能优化

Http超⽂本传输协议，明⽂传输 ，传输不安全，https在传输数据的时候会对数据进⾏加密。

优化指标：

响应时间

吞吐量（TPS 事务/s）

### JVM内存调优

### JVM垃圾回收策略调优

指标：

- 吞吐量：⼯作时间（排除GC时间）占总时间的百分⽐， ⼯作时间并不仅是程序运⾏的时间，还包含内存分配时间。
- 暂停时间：由垃圾回收导致的应⽤程序停⽌响应次数/时间。

### Tomcat配置调优

tomcat线城池

tomcat连接器

禁用AJP连接器

调整IO模式

动静分离（Nginx+Tomcat）

## Nginx相关

Nginx是什么

Nginx是一个高性能HTTP和反向代理web服务器，占有内存小，并发能力强

Nginx做什么

- Http服务器（Web服务器）

  性能非常高，非常注重效率，能接受高负载，cpu和内存占用非常低

- 反向代理服务器

- 负载均衡服务器

- 动静分离

特点：

跨平台

上手容易，配置简单

高并发

### 正向代理

在浏览器端配置服务器地址，通过代理服务器，访问到目标服务器

### 反向代理

而反向代理，对客户端来说是不可见的，浏览器不需要做配置，nginx+tomcat算一个整体。

### 负载均衡

请求到来时，Nginx根据请求找到一个原始服务器来处理请求，如果服务器有多台找哪个服务器，这个就是负载均衡。





