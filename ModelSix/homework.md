## 作业一：

1）基于SpringBoot整合SSS框架（即整合第一阶段模块三作业第二题内容，含有登录拦截验证）

2）在 1 的基础上开发SpringSession进行Session一致性控制

3）将工程打成war包

4）将war包部署到分布式集群架构中，要求一个Nginx节点，两个Tomcat节点

​    —> Nginx（轮询策略） —> Tomcat1—> Tomcat2

5）完成测试

注意：作业提交时提交可运行的工程代码（源代码和war包）以及sql脚本，nginx配置及tomcat配置，redis配置统一修改

答：

演示：先看是否进行了session控制以及 分布式环境下是否产生session问题？

##### 测试步骤：

1. 首先启动两个不同端口的tomcat
2. 直接访问resume/showAll显示需要登录--------session进行了控制访问（通过拦截器）
3. 输入用户名和密码，进行登录，一直显示“您没有登录”--------Nginx轮询 ，分布式环境下，session无法共享
4. 加入Redis作为session 共享缓存后，即可正常访问。

## 作业二：

请描述你对分布式调度的理解（结合Elastic-Job-lite图文并茂描述）

答：

任务调度我们常使用两种框架Quartz、Elastic-job，而在分布式环境下，常使用Elastic-job，

任务调度的意思是：

1. 多个服务器节点同时开启，但只用其中一个定时任务程序在执行

![image-20200514210302729](https://github.com/lvxinran/lagouLearn/blob/master/ModelSix/%E4%BD%9C%E4%B8%9A%E6%96%87%E4%BB%B6/image-20200514210302729.png)

如果正在执行的断开，就会使用其他的节点进行任务处理

![image-20200514210404905](https://github.com/lvxinran/lagouLearn/blob/master/ModelSix/%E4%BD%9C%E4%B8%9A%E6%96%87%E4%BB%B6/image-20200514210404905.png)

​	2.任务可以进行分片，把大的任务分解成小的任务，放在多个服务器去执行

![image-20200515095133642](https://github.com/lvxinran/lagouLearn/blob/master/ModelSix/%E4%BD%9C%E4%B8%9A%E6%96%87%E4%BB%B6/image-20200515095133642.png)

在节点断开之后仍然可以使用其他实例进行task的执行

![image-20200515095422482](https://github.com/lvxinran/lagouLearn/blob/master/ModelSix/%E4%BD%9C%E4%B8%9A%E6%96%87%E4%BB%B6/image-20200515095422482.png)