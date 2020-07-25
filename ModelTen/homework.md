SpringCloud上作业

1、工程有两个目录 项目目录、nginx目录，项目目录下有多个微服务

2、首先 准备了已经超时的验证码进行验证，10分钟有效 调用验证码服务进行校验

3、再生成验证码以及 进行错误校验码测试

![image-20200725191403480](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200725191403480.png)

4、进行登录测试 和回显测试 

5、进行对应的gateway多次IP校验测试

6、除了登录和注册其他需要进行token验证

![image-20200725191549963](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200725191549963.png)

7、进行了相关的动静分离将页面和工程代码分开

![image-20200725191741561](C:\Users\Administrator\AppData\Roaming\Typora\typora-user-images\image-20200725191741561.png)

8、最后对相应的文件使用Config处理

