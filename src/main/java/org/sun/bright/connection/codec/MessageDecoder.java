package org.sun.bright.connection.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.util.Formatter;
import java.util.List;

@Slf4j
public class MessageDecoder extends ByteToMessageDecoder {

    /**
     * decode() 会根据接收的数据，被调用多次，知道确定没有新的元素添加到list,
     * 或者是 ByteBuf 没有更多的可读字节为止。
     * 如果 list 不为空，就会将 list 的内容传递给下一个 handler
     *
     * @param ctx 上下文对象
     * @param in  入站后的 ByteBuf
     * @param out 将解码后的数据传递给下一个 handler
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        log.info("-------------------> 解码器被调用！");
        byte[] callData = new byte[in.readableBytes()];
        in.readBytes(callData);
        String str = this.byte2String(callData);
        if (null == str) {
            log.info("出现null数据, msg = {}, 转换后16进制数据为null", in);
            return;
        }
        out.add(str.replace(" ", ""));
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

}
