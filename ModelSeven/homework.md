编程题：

  在基于Netty的自定义RPC的案例基础上，进行改造



案例版本：

序列化方式为String, 并根据自定义的providerName做为通信协议，服务端判断是否以”UserService“开头 完成的案例



要求完成改造版本：

 序列化协议修改为JSON，使用fastjson作为JSON框架，并根据RpcRequest实体作为通信协议，服务端需根据客户端传递过来的RpcRequest对象通过反射，动态代理等技术，最终能够执行目标方法，返回字符串"success"



要点提示：

（1）客户端代理的invoke方法中需封装RpcRequest对象，将其当做参数进行传递



（2）服务端的UserServiceImpl类上添加@Service注解，在启动项目时，添加到容器中



（3）服务端要添加@SpringBootApplication注解，main方法中添加SpringApplication.run(ServerBootstrap.class, args);，进行启动扫描（注意项目启动类位置：扫描路径）



（4）服务端在收到参数，可以借助反射及动态代理（如需用到ApplicationContext对象，可以借助实现ApplicationContextAware接口获取），来调用UserServiceImpl方法，最终向客户端返回”success“即可



（5）既然传递的是RpcRequest对象了，那么客户端的编码器与服务端的解码器需重新设置



示例： pipeline.addLast( new RpcDecoder(RpcRequest.class, new JSONSerializer()));



**具体协议对象、序列化接口及实现类、编解码器如下**



  通信协议对象：

public class RpcRequest{

  /**

   \* 请求对象的ID

   */

  private String requestId;

  /**

   \* 类名

   */

  private String className;

  /**

   \* 方法名

   */

  private String methodName;

  /**

   \* 参数类型

   */

  private Class<?>[] parameterTypes;

  /**

   \* 入参

   */

  private Object[] parameters;



  /getter/setter方法.....

}



fastjson依赖：



   <dependency>

​    <groupId>com.alibaba</groupId>

​    <artifactId>fastjson</artifactId>

​    <version>1.2.41</version>

  </dependency>





序列化接口：



public interface Serializer {

  /**

   \* java对象转换为二进制

   *

   \* @param object

   \* @return

   */

  byte[] serialize(Object object) throws IOException;



  /**

   \* 二进制转换成java对象

   *

   \* @param clazz

   \* @param bytes

   \* @param <T>

   \* @return

   */

  <T> T deserialize(Class<T> clazz, byte[] bytes) throws IOException;

}

\```

采用JSON的方式，定义JSONSerializer的实现类:（其他序列化方式，可以自行实现序列化接口）



public class JSONSerializer implements Serializer{



  @Override

  public byte[] serialize(Object object) {

​    return JSON.toJSONBytes(object);

  }



  @Override

  public <T> T deserialize(Class<T> clazz, byte[] bytes) {

​    return JSON.parseObject(bytes, clazz);

  }

}

\```

编码器实现：(现在传输的是RpcRequest, 需要编码器将请求对象转换为适合于传输的格式)



public class RpcEncoder extends MessageToByteEncoder {

  private Class<?> clazz;

  private Serializer serializer;



  public RpcEncoder(Class<?> clazz, Serializer serializer) {

​    this.clazz = clazz;

​    this.serializer = serializer;

  }



  @Override

  protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf byteBuf) throws Exception {

​    if (clazz != null && clazz.isInstance(msg)) {

​      byte[] bytes = serializer.serialize(msg);

​      byteBuf.writeInt(bytes.length);

​      byteBuf.writeBytes(bytes);

​    }

  }

}

### 答：具体请看代码及测试视频。