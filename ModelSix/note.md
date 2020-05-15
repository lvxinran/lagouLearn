 

# 分布式和集群相关

### 分布式一定是集群，集群不一定是分布式。

分布式：一个系统拆分成多个子系统，每个子系统负责一部分功能。

集群：多个实例共同工作，一个应用复制多份部署。

## 一致性hash算法

直接寻址法（线性构造hash算法）、除留余数法

### 应用场景

Redis、Hadoop、ElasticSearch、Mysql分库分表、Nginx负载均衡等

请求的负载均衡（Nginx 的 ip_hash）

分布式存储

## Nginx使用一致性哈希

配置在upstream中（需要ngx_http_consistent_hash_master包）

consistent_hash $request_uri：根据客户端请i求的uri映射

consistent_hash $remote_addr：根据ip进行映射

consistent_hash  $args： 根据客户端的参数进行映射

## 时钟同步问题

1、集群服务器都可以连接互联网

​	Linux上使用 同步网络时钟

```shell
ntpdate -u ntp.api.bz
```

2、某一个服务器可以连接互联网或者都不能访问（若都不能访问就手动设置时间）

- 设置好A的时间

- 修改A的/etc/ntp.conf文件

  

  ```shell
  -如果有restrict default ignore，注释掉
  -添加restrict 172.17.0.0（A机器局域网段）mask 255.255.255.0 nomodify notrap
  server 127.127.1.0  //local clock
  fudge 127.127.1.0 stratum 10
  - 重启生效并配置ntpd服务开机自启动
  service ntpd restart 
  chkconfig ntpd on 
  ```

- 集群中其他服务器从A同步时间

  ```shell
  ntpdate 127.17.0.17
  ```

最后可以使用定时任务定时同步时钟

## 分布式ID解决方案

### 为什么需要分布式ID？

Mysql分表之后不能重复，因此不能主键自增，因此需要提供全局唯一ID。

### 分布式ID生成方案

- JDK中的UUID（缺点：特别长，没有规律）

- 独立数据库的自增ID（缺点：多了一个数据库，如果宕机，出现问题）

- 雪花算法SnowFlake

  64位Long：符号位+时间戳+机器Id+序列号（一台机器的一个毫秒内产生4000个id）

- 借助Redis的Increment命令获取ID

## 分布式调度（定时任务）

任务调度框架Quartz

1. 创建任务调度器
2. 创建一个任务
3. 创建任务事件触发器
4. 使用任务调度器根据事件触发器执行我们的任务

## 分布式任务调度框架Elastic-job

基于zookeepe（分布式协调服务），利用通知。

#### 轻量级和去中心化：

轻便，使用jar和zookeeper

都很对等，自我驱动

主节点非固定

### 分片

大任务拆分成小任务

## Session共享问题

Nginx的ip_hash

Session复制

Redis几种存储（使用Spring Session简化操作）