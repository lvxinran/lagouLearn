package com.lxr.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @author lvxinran
 * @date 2020/5/25
 * @discribe
 */
public class RpcDecoder extends MessageToMessageDecoder<ByteBuf> {

    private Class<?> clazz;
    private Serializer serializer;

    public RpcDecoder(Class<?> clazz, Serializer serializer) {
        this.clazz = clazz;
        this.serializer = serializer;
    }


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List out) throws Exception {
//        if (clazz != null && clazz.isInstance(msg)) {
//            byte[] bytes = serializer.deserialize(msg);
//            byteBuf.writeInt(bytes.length);
//            byteBuf.writeBytes(bytes);
//
        final int length = msg.readableBytes();
        byte[] b = new byte[length];
        msg.getBytes(msg.readerIndex(), b,0,length);
        Object object = serializer.deserialize(clazz, b);
        out.add(object);
    }
}
