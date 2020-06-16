**编程题一：**

  在基于Netty的自定义RPC的案例基础上，进行改造。基于Zookeeper实现简易版服务的注册与发现机制

**要求完成改造版本：**

\1. 启动2个服务端，可以将IP及端口信息自动注册到Zookeeper

\2. 客户端启动时，从Zookeeper中获取所有服务提供端节点信息，客户端与每一个服务端都建立连接

\3. 某个服务端下线后，Zookeeper注册列表会自动剔除下线的服务端节点，客户端与下线的服务端断开连接

\4. 服务端重新上线，客户端能感知到，并且与重新上线的服务端重新建立连接

**
**

**编程题二：**

  基于作业一的基础上，实现基于Zookeeper的简易版负载均衡策略

**要求完成改造版本：**

\1. Zookeeper记录每个服务端的最后一次响应时间，有效时间为5秒，5s内如果该服务端没有新的请求，响应时间清零或失效

\2. 当客户端发起调用，每次都选择最后一次响应时间短的服务端进行服务调用，如果时间一致，随机选取一个服务端进行调用，从而实现负载均衡

**
**

**编程题三：**

  基于Zookeeper实现简易版配置中心

**要求实现以下功能：**

\1. 创建一个Web项目，将数据库连接信息交给Zookeeper配置中心管理，即：当项目Web项目启动时，从Zookeeper进行MySQL配置参数的拉取

\2. 要求项目通过数据库连接池访问MySQL（连接池可以自由选择熟悉的）

\3. 当Zookeeper配置信息变化后Web项目自动感知，正确释放之前连接池，创建新的连接池



## 测试步骤

1、首先启动两个服务端，并启动一个客户端，与其中一个进行相连

2、断开其中一个服务端，发现客户端连接变成另外一个客户端

3、再次启动刚刚服务端，客户端监听到节点添加事件，与新客户端进行连接

4、开多个客户端，发现根据响应时间长短进行连接。