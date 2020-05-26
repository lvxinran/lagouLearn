package com.lxr.client;

import com.lxr.coder.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * @author lvxinran
 * @date 2020/5/25
 * @discribe
 */
public class UserClientHandler extends ChannelInboundHandlerAdapter implements Callable {
    private ChannelHandlerContext context;
    private String result;
    private RpcRequest param;
    @Override
    public synchronized Object call() throws Exception {
        context.writeAndFlush(param);
        wait();
        return result;

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }

    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        result = ((RpcRequest)msg).getParameters()[0].toString();
        notify();
    }

    public void setParam(RpcRequest param) {
        this.param = param;
    }
}
