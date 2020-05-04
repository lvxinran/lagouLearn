## 作业⼀（编程题）：

  开发Minicat V4.0，在已有Minicat基础上进⼀步扩展，模拟出webapps部署效果 磁盘上放置⼀个webapps⽬录，webapps中可以有多个项⽬，⽐如demo1,demo2,demo3... 具体的项⽬⽐如demo1中有serlvet（也即为：servlet是属于具体某⼀个项⽬的servlet），这样的话在 Minicat初始化配置加载，以及根据请求url查找对应serlvet时都需要进⼀步处理

答：首先准备server.xml、webapps文件夹、具体项目

测试步骤：

1、直接BootStrap.main()方法启动Minicat

```java
public static void main(String[] args) throws Exception {
   Bootstrap b = new Bootstrap();
   b.start();
}
```

2、浏览器访问http://localhost:8080/Web_Test_war/app

![image-20200504181953788](\image-20200504181953788.png)



控制台输出

![image-20200504182023720](\image-20200504182023720.png)

输出为自定义的servlet的打印

具体流程：

1. 读取server.xml

   ```java
   private void loadServer() throws DocumentException {
       InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(serverPath);
       SAXReader saxReader = new SAXReader();
       Document document = saxReader.read(inputStream);
       Element rootElement = document.getRootElement();
       Server server = new StandardServer();
       List<Element> serviceList = rootElement.selectNodes("//Service");
       for(Element service:serviceList){
           Service standardService = new StandardService();
           List<Element> connectorList = service.selectNodes("//Connector");
           for(Element connectorNode:connectorList){
               String port = connectorNode.attributeValue("port");
               Connector connector = new Connector();
               connector.setPort(Integer.parseInt(port));
               standardService.addConnector(connector);
               Element engine = (Element) service.selectSingleNode("//Engine");
               if(engine!=null){
                   StandardEngine standardEngine = new StandardEngine();
                   standardService.setEngine(standardEngine);
                   List<Element> hosts = engine.selectNodes("//Host");
                   for(Element host:hosts) {
                       Host standardHost = new StandardHost();
                       standardHost.setAppBase(host.attributeValue("appBase"));
                       standardEngine.addHost(standardHost);
                       mapper.addHosts(standardHost);
                   }
               }
           }
           server.addService(standardService);
       }
   
       this.server = server;
   }
   ```

2. 绑定端口

   ```java
   port = server.getServices().get(0).getConnectors().get(0).getPort();
   ```

3. 初始化容器，读取各个项目的web.xml

   ```java
   public void loadMapper() throws  Exception{
       List<Host> hosts = mapper.getHosts();
       for(Host host:hosts){
           String appBase = host.getAppBase();
           File file = new File(appBase);
           File[] files = file.listFiles();
           for(File contextFile:files){
               Context context = new Context();
               context.setName(contextFile.getName());
               loadServlet(appBase,context);
               host.addContext(context);
           }
       }
       System.out.println();
   }
   ```

   具体测试可修改server.xml下的配置，项目结构如下

   ![image-20200504182416899](\image-20200504182416899.png)

## 作业⼆（简答题）： 请详细描述Tomcat体系结构（图⽂并茂）

Tomcat总体由两大部分组成：连接器、容器

连接器用来处理网络连接，封装请求对象

容器用来接受连接器的请求对象，实现业务逻辑，返回响应

![Tomcat整体流程](\Tomcat整体流程.png)

Catalina容器中又类似嵌套模式分别由Engine、Host、Context、Wrapper组成（根据server.xml标签嵌套可看出逻辑关系）

Server标签下有多个Service标签

每个Service对应多个Connector和一个Engine

一个Engine对应多个Host、Context、Wrapper嵌套式。

![Tomcat内部结构](\Tomcat内部结构.png)