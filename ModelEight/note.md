# Zookeeper

分布式协同服务

### 集群角色

领导者、观察者、跟随者

### 会话

tcp长连接

### 数据节点

机器节点、数据单元

### 版本

Stat记录ZNode的三个版本

### Watcher（事件监听器）

对节点进行事件监听，有事件触发，把改事件通知给感兴趣的客户端

### ACL

zookeeper使用ACL进行权限控制，**保障数据安全**

权限模式、授权对象、权限

节点（ZNode）

### 持久节点

### 持久顺序节点

### 临时节点

不能添加子节点

### 临时顺序节点

## Watcher

数据变更通知

### 客户端命令

create -s -e path data acl  创建节点

ls/get path 读取节点

set path data 更新节点

delete path 删除节点（存在子节点无法删除）

### ZkClient（对zookeeper内部Api进行封装，同步创建）

可以递归创建节点、递归删除节点、可以对不存在的子节点变更进行监听

### Curator

使用fluent风格（set方法返回实体类对象，通过调用方法拿到对象，链式编程）

## Zookeeper应用场景

推拉相结合的方式，数据发布订阅系统。

命名服务

### 集群管理

两大特性：客户端注册监听（watcher机制）、临时节点会话失效时自动删除

分布式日志收集系统

### master选举

master处理复杂逻辑，同步给其他单元

### 分布式锁

排他锁：写锁，独占锁（使用卫生间？）

利用临时子节点进行实现，没有创建成功的进行监听。

共享锁：读锁

利用临时顺序节点，只关注比自己小的前一个就可以

### 分布式队列

FIFO先入先出队列，相当于一个全写的共享锁模型，向比自己序号小的最后一个节点注册监听

Barrier分布式屏障：设置Barrier值（节点数据值 ），只有子节点数量达到10 之后才会进行处理

### ZAB协议---特别为zookeeper提供的原子广播协议

主备模式

崩溃恢复、消息广播模式