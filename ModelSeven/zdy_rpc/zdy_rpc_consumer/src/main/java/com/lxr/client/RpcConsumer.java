package com.lxr.client;

import com.lxr.coder.JSONSerializer;
import com.lxr.coder.RpcDecoder;
import com.lxr.coder.RpcEncoder;
import com.lxr.coder.RpcRequest;
import com.lxr.service.UserService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author lvxinran
 * @date 2020/5/25
 * @discribe
 */
public class RpcConsumer {
    private static ExecutorService executor =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static UserClientHandler client;

    public Object createProxy(final Class<?> serviceClass, final RpcRequest
            request) {
        request.setClassName(UserService.class.getCanonicalName());
        request.setMethodName("sayHello");
        request.setParameterTypes(new Class[]{String.class});
        request.setParameters(new Object[]{"客户端参数"});
        return
                Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class<?>[]{serviceClass}, (proxy, method, args) -> {
                            if (client == null) {
                                initClient();
                            }
                            client.setParam(request);
                            return executor.submit(client).get();
                        });
    }



    public static void initClient() throws InterruptedException {
        client = new UserClientHandler();

        EventLoopGroup group = new NioEventLoopGroup();

        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY,true).handler(new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new RpcDecoder(RpcRequest.class,new JSONSerializer()))
                        .addLast(new RpcEncoder(RpcRequest.class,new JSONSerializer()))
                        .addLast(client);
            }

        });
        bootstrap.connect("127.0.0.1",8081).sync();

    }
}
