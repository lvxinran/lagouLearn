SpringCloud(下) 作业

1、配置用户服务、网关服务、邮件服务、验证码服务 以及Sentinel服务

用户微服务改造成Nacos注册

![image-20201201034135930](\image-20201201034135930.png)

验证码服务注册到Nacos，同时使用Nacos作为配置中心进行配置

![image-20201201034312902](D:\git\lagouLearn\ModelEleven\image-20201201034312902.png)

![image-20201201034435431](D:\git\lagouLearn\ModelEleven\image-20201201034435431.png)

网关服务注册到Nacos 同时关联到Sentinel监控

![image-20201201034531150](D:\git\lagouLearn\ModelEleven\image-20201201034531150.png)

2、启动Nacos和用户服务、网关服务、邮件服务、验证码服务 以及Sentinel DashBoard服务	

3、对网关进行流量监控配置

![image-20201201034701671](D:\git\lagouLearn\ModelEleven\image-20201201034701671.png)

4、进行验证测试，（快速多次请求，会发现被拒绝的请求增加）

![image-20201201034641126](D:\git\lagouLearn\ModelEleven\image-20201201034641126.png)