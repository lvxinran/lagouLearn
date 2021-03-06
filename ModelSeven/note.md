# 分布式理论

分布式系统：一个业务拆分成多个子业务，分布在不同的服务器节点。

通信异常：会产生消息丢失和消息延迟

网络分区：部分节点无法通信，分区。

三态：成功、失败、超时。

节点故障：节点宕机。

## 一致性概念

一致性级别：强一致性、弱一致性、最终一致性。

## 分布式事务

分布式环境下的ACID

## CAP定理

一致性、可用性、分区容错性只能同时满足两个。

分区容错性是最基本的需求，因此在一致性和可用性之间寻求平衡。

## BASE理论

既然无法做到强一致性，但可以根据业务特点，采用适当方式使系统达到最终一致性。

### 基本可用：

响应时间损失、功能上的损失。

### 软状态

### 最终一致性

保证数据一致，不需要实时。

## 一致性协议

### 2pc

两阶段提交，协调者和参与者

提交事务请求、执行事务提交。

优点：原理简单、实现方便。

缺点：同步阻塞、单点问题、数据不一致、过于保守。

### 3pc

分成三个阶段，CanCommit、preCommit、doCommit。

## Paxos算法

解决问题：解决了一致性问题，对某一个值快速准确达到一致。

提案：包括提案编号和提议的值Value

三个角色：提案发起者、决策者、最终学习者。

#### 保证活性

活性：一定要选定一个value值

选取主proposer，只有主才能提出提案。

## 一致性算法Raft

分为两个阶段：领导人选举、日志复制

使用心跳机制触发选举。

## 心跳检测机制

检测节点是否还存活者

周期检测心跳机制

累计次数检测机制

## 高可用设计

高可用：减少不能提供服务的时间

主备模式、互备模式、集群模式。

容错性

负载均衡

## 网络通信

将流从一台计算机输送到另外一台计算机，通过传输协议和I/O来实现

## RPC 远程过程调用

RMI：java原生远程调用 

## 同步和异步

关注的是消息通信机制

## 阻塞和非阻塞

关注的是程序在等待调用结果时的状态

BIO、NIO、AIO

BIO：一个连接一个线程（Java1.4之前）

NIO：一个请求一个线程（连接比较多且连接比较短，一个线程处理更多的事）

AIO：一个有效请求一个线程（异步非阻塞）

## Netty

异步事件驱动的网络框架，完全基于NIO实现。