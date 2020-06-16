# Dubbo模块

## 配置文件：

**dubbo:application**     name：服务名字 owner：服务负责人  标签中间可以dubbo:parameter qos.enable，qos.port

**dubbo:registry**  代表注册中心，一个服务可以注册到多个注册中心 address：地址   id：服务id

**dubbo:protocol**  dubbo协议

**dubbo:service** 服务信息 interface:暴露的接口 ref:引用对象 version:版本

**dubbo:refrence** 引用信息 registry:注册中心

## 调用时拦截操作

利用spi机制，对服务的消费方或者提供方执行增强

## 负载均衡

dubbo中多种负载均衡策略

random（按照权重）、轮询、最少活跃调用数、一致性Hash（引入了虚拟节点）

### 自定义负载均衡

实现LoadBalance端口，新建spi文件

## 异步调用

## 线程池

## 路由规则

路由线上应用场景流程

引入Curator框架，结合zookeeper

### Dubbo调用关系

四部分组成 provider、consumer、registry、Monitor

Dubbo分层

Business 业务逻辑层：Service层
RPC层（远程过程调用层）：Config层（ReferenceConfig，ServiceConfig），Proxy层，Registry层，Cluster层，Monitor层，Protocol层
Remoting层（远程数据传输层）：Exchange层、Transport层、Serialize数据序列化层















