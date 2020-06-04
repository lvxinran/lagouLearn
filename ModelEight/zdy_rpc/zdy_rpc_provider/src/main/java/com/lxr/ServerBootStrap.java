package com.lxr;

import com.lxr.coder.JSONSerializer;
import com.lxr.coder.RpcDecoder;
import com.lxr.coder.RpcEncoder;
import com.lxr.coder.RpcRequest;
import com.lxr.handler.UserServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lvxinran
 * @date 2020/5/25
 * @discribe
 */
@SpringBootApplication
public class ServerBootStrap {
    private static CuratorFramework client;

    private static String address = "127.0.0.1";

    private static int port = 8081;
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ServerBootstrap.class,args);
        startServer(address,port);
        initZookeeper();
        client.start();
        createSession();
    }

    public static void initZookeeper(){
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework curator = CuratorFrameworkFactory.builder()
                .connectString("127.0.0.1:2181")
                .sessionTimeoutMs(5000)  // 会话超时时间
                .connectionTimeoutMs(5000) // 连接超时时间
                .retryPolicy(retryPolicy)
                .namespace("base") // 包含隔离名称
                .build();
        client = curator;
    }

    public static void createSession() throws Exception {
        String server = "服务端节点";
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL)
                .forPath("/"+address+"/"+port,server.getBytes());
    }
    public static void startServer(String hostName,int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast( new RpcDecoder(RpcRequest.class, new JSONSerializer()))
                        .addLast(new RpcEncoder(RpcRequest.class,new JSONSerializer()))
                        .addLast(new UserServerHandler());
            }
        });
        serverBootstrap.bind(hostName,port).sync();

    }

    public static CuratorFramework getClient() {
        return client;
    }

    public static String getAddress() {
        return address;
    }

    public static int getPort() {
        return port;
    }
}
