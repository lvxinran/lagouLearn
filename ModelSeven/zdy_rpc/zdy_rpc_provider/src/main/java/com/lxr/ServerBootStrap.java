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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author lvxinran
 * @date 2020/5/25
 * @discribe
 */
@SpringBootApplication
public class ServerBootStrap {
    public static void main(String[] args) throws InterruptedException{
        SpringApplication.run(ServerBootstrap.class,args);
        startServer("127.0.0.1",8081);
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
}
