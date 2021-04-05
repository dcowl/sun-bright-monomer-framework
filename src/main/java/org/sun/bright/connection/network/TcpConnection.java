package org.sun.bright.connection.network;

import io.netty.buffer.*;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;

@Slf4j
public class TcpConnection {

    private final ChannelHandlerContext context;

    public TcpConnection(ChannelHandlerContext context) {
        this.context = context;
    }

    public static TcpConnection valueOf(@NonNull ChannelHandlerContext context) {
        return new TcpConnection(context);
    }

    /**
     * 对设备下发数据
     *
     * <pre>
     * (ChannelFutureListener) future -> {
     *      if (future.isSuccess()) {
     *          log.info("===================> Netty send command success");
     *      } else {
     *          log.info("===================> Netty send command field");
     *      }
     *  }
     * </pre>
     *
     * @param sendData 十六进制数据
     */
    public void sendMessage(final String sendData) {
        log.info("下发设备命令: {}", sendData);
        byte[] hexBytes = getHexBytes(sendData);
        ByteBuf byteBuf = context.alloc().buffer(hexBytes.length);
        log.info("向设备下发的信息为： {}", bytesToHexString(hexBytes));
        byteBuf.writeBytes(hexBytes);
        context.channel().writeAndFlush(byteBuf).addListener(
                future -> {
                    if (future.isSuccess()) {
                        log.info("===================> Netty send command success");
                    } else {
                        log.info("===================> Netty send command field");
                    }
                }
        );
    }

    private String bytesToHexString(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (byte b : bArray) {
            sTemp = Integer.toHexString(0xFF & b);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static byte[] getHexBytes(String str) {
        str = str.replace(" ", "");
        byte[] bytes = new byte[str.length() / 2];
        for (int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }
        return bytes;
    }

}
