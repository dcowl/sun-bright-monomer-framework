package org.sun.bright.connection.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.sun.bright.connection.ChannelHandlerHelper;

import java.util.Formatter;

@Slf4j
@ChannelHandler.Sharable
public class ConnectionChannelInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("=============> 通道[{}]注册 ", ChannelHandlerHelper.getChannelId(ctx));
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("===========> 通道[{}]状态活跃！", ctx.channel().id().asLongText());
        super.channelActive(ctx);
    }

    /**
     * <code>
     * ByteBuf byteBuf = (ByteBuf) msg;
     * String in = byteBuf.toString(CharsetUtil.UTF_8);
     * </code>
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 接收的数据
        ByteBuf buf = (ByteBuf) msg;
        byte[] callData = new byte[buf.readableBytes()];
        buf.readBytes(callData);
        String str = this.byte2String(callData);
        if (null == str) {
            log.info("出现null数据, msg = {}, 转换后16进制数据为null", msg);
            return;
        }
        log.info("=======> 通道[{}]上报数据, 设备上报数据(HEX): {}", ctx.channel().id().asLongText(), str);
        String data = str.replace(" ", "");
        log.info("client data is {}", data);
        super.channelRead(ctx, msg);
    }

    /**
     * 字节流转字符串
     *
     * @param ba 字节数组
     * @return 字符串
     */
    private String byte2String(byte[] ba) {
        if (ba == null || ba.length == 0) {
            return null;
        }
        try (Formatter f = new Formatter()) {
            for (byte b : ba) {
                f.format("%02x ", b);
            }
            return f.toString().toUpperCase();
        }
    }

    /**
     * 客户端 失去连接
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ctx.close();
    }

    /**
     * 连接异常, 关闭连接, 请求<code>ConnectionSessionManager</code>中的通道管理模块
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

}
