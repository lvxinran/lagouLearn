package com.lxr.handler;

import com.lxr.ServerBootStrap;
import com.lxr.coder.RpcRequest;
import com.lxr.service.UserService;
import com.lxr.service.UserServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author lvxinran
 * @date 2020/5/25
 * @discribe
 */
public class UserServerHandler extends ChannelInboundHandlerAdapter {

    private UserService userService  = new UserServiceImpl();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        long startTime = System.currentTimeMillis();
        RpcRequest request = (RpcRequest) msg;
        if(!request.getClassName().equals(userService.getClass().getInterfaces()[0].getName())){
            return;
        }
        Method method = userService.getClass().getDeclaredMethod(request.getMethodName(), request.getParameterTypes());
        if(method==null){
            return;
        }
        Object result = method.invoke(userService, request.getParameters());
        request.setParameters(new Object[]{result});
        ctx.writeAndFlush(request);
        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;
        String s = responseTime + "&" + endTime;
        CuratorFramework client = ServerBootStrap.getClient();
        Stat stat = client.setData().forPath("/" + ServerBootStrap.getAddress() + "/" + ServerBootStrap.getPort(),s.getBytes());
    }
}
