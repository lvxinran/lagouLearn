**编程题一：将Web请求IP透传到Dubbo服务中**



通过扩展Dubbo的Filter（TransportIPFilter），完成Web请求的真实IP透传到Dubbo服务当中，并在Dubbo服务中打印请求的IP

讲解：

先使用Springboot创建web项目（consumer），再创建两个provider，之后写一个filter来导入项目

最后测试，本地测试127.0.0.1，内网测试 返回内网ip

**编程题二：简易版Dubbo方法级性能监控**



在真实业务场景中，经常需要对各个业务接口的响应性能进行监控（常用指标为：TP90、TP99）



下面通过扩展Dubbo的Filter（TPMonitorFilter），完成简易版本 Dubbo 接口方法级性能监控，记录下TP90、TP99请求的耗时情况

祥见测试视频。