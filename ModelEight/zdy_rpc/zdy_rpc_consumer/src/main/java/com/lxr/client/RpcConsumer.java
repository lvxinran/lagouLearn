package com.lxr.client;

import com.lxr.coder.JSONSerializer;
import com.lxr.coder.RpcDecoder;
import com.lxr.coder.RpcEncoder;
import com.lxr.coder.RpcRequest;
import com.lxr.service.UserService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.lang.reflect.Proxy;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author lvxinran
 * @date 2020/5/25
 * @discribe
 */
public class RpcConsumer {
    private static ExecutorService executor =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static String address = "127.0.0.1";

    private static Set<Integer> ports = new HashSet<>();

    public static Map<Integer, ChannelFuture> channels;

    static {
        try {
            channels = getChannel(address, ports);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static volatile UserClientHandler client;

    private static Bootstrap bootstrap;

    private static CuratorFramework curator;

    public Object createProxy(final Class<?> serviceClass, final RpcRequest
            request) {
        request.setClassName(UserService.class.getCanonicalName());
        request.setMethodName("sayHello");
        request.setParameterTypes(new Class[]{String.class});
        request.setParameters(new Object[]{"客户端参数"});
        return
                Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                        new Class<?>[]{serviceClass}, (proxy, method, args) -> {
                            client.setParam(request);
                            return executor.submit(client).get();
                        });
    }



    public static void initClient() throws Exception {
        if(curator==null){
            initParam();
            curator.start();
            ini();
        }
        client = new UserClientHandler();

    }

    public static void initParam() throws Exception {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        curator = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(5000)  // 会话超时时间
                .connectionTimeoutMs(5000) // 连接超时时间
                .retryPolicy(retryPolicy)
                .namespace("base") // 包含隔离名称
                .build();

    }
    public static void ini() throws Exception {
        List<String> list = curator.getChildren().forPath("/" + address);
        for(String str:list){
            ports.add(Integer.parseInt(str));
        }
        PathChildrenCache pathChildrenCache = new PathChildrenCache(curator,"/" + address,true);
        try {
            pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
            pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                    switch (pathChildrenCacheEvent.getType()){
                        case CHILD_ADDED:
                            String path = pathChildrenCacheEvent.getData().getPath();
                            ports.add(Integer.parseInt(path.split("/")[2]));
                            doConnect(bootstrap,address,Integer.parseInt(path.split("/")[2]));
                            System.out.println("儿子节点添加" + pathChildrenCacheEvent.getData());
                            break;
                        case CHILD_UPDATED:
                            System.out.println("儿子节点发生了更新  " + pathChildrenCacheEvent.getData());
                            break;
                        case CHILD_REMOVED:
                            String removePath = pathChildrenCacheEvent.getData().getPath();
                            ports.remove(Integer.parseInt(removePath.split("/")[2]));
                            getChannel(address,ports);
                            System.out.println("儿子节点移除  " + pathChildrenCacheEvent.getData());
                            notifyAll();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static final void getBootstrap(EventLoopGroup group) throws Exception {
        if (null == group) {
            group = new NioEventLoopGroup();
        }
        if(client==null){
            initClient();
        }
        bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.handler(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast(new RpcDecoder(RpcRequest.class,new JSONSerializer()))
                        .addLast(new RpcEncoder(RpcRequest.class,new JSONSerializer()))
                        .addLast(client);
            }
        });
    }
    public static final Map<Integer, ChannelFuture> getChannel(String host, Set<Integer> ports) throws Exception {
        Map<Integer, ChannelFuture> result = new HashMap<>();
        getBootstrap(null);
        for (int  port: ports) {
            bootstrap.remoteAddress(host, port);
            //异步连接tcp服务端
            ChannelFuture future = bootstrap.connect().addListener((ChannelFuture futureListener) -> {
                final EventLoop eventLoop = futureListener.channel().eventLoop();
                if (!futureListener.isSuccess()) {
                    //服务器未启动 连接tcp服务器不成功
                    System.out.println(port + "第一次连接与服务端断开连接!在10s之后准备尝试重连!");
                    //10秒后重连
                    eventLoop.schedule(() -> doConnect(bootstrap, host, port), 10, TimeUnit.SECONDS);
                }
            });
            result.put(port, future);
        }
        return result;
    }
    public static void doConnect(Bootstrap bootstrap , String host, int port) {
        try {
            if (bootstrap != null) {
                bootstrap.remoteAddress(host, port);
                ChannelFuture f = bootstrap.connect().addListener((ChannelFuture futureListener) -> {
                    final EventLoop eventLoop = futureListener.channel().eventLoop();
                    if (!futureListener.isSuccess()) {
                        //连接tcp服务器不成功 10后重连
                        System.out.println(port + "服务器断线-----与服务端断开连接!在10s之后准备尝试重连!");
                        eventLoop.schedule(() -> doConnect(bootstrap, host, port), 10, TimeUnit.SECONDS);
                    }
                });
                channels.put(port, f);
            }
        } catch (Exception e) {
            System.out.println("客户端连接失败!" + e.getMessage());
        }
    }
}
