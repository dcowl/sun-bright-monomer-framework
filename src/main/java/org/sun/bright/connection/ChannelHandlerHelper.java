package org.sun.bright.connection;

import io.netty.channel.ChannelHandlerContext;
import org.springframework.lang.NonNull;

import javax.validation.constraints.NotNull;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Locale;
import java.util.Objects;

public final class ChannelHandlerHelper {

    private ChannelHandlerHelper() {
    }

    public static String getSessionId(@NotNull ChannelHandlerContext ctx) {
        return getChannelId(ctx) + "-" + getListeningPort(ctx.channel().localAddress());
    }

    public static String getChannelIdUpperCase(@NotNull ChannelHandlerContext ctx) {
        return ctx.channel().id().asLongText().toUpperCase(Locale.getDefault());
    }

    public static String getChannelId(@NonNull ChannelHandlerContext ctx) {
        return ctx.channel().id().asLongText();
    }

    public static int getListeningPort(SocketAddress socketAddress) {
        int port = -1;
        if (Objects.nonNull(socketAddress) && socketAddress instanceof InetSocketAddress) {
            port = ((InetSocketAddress) socketAddress).getPort();
        }
        return port;
    }

    public static String getIpAddress(@NonNull SocketAddress socketAddress) {
        String ip = null;
        if (socketAddress instanceof InetSocketAddress) {
            ip = ((InetSocketAddress) socketAddress).getAddress().getHostAddress();
        }
        return ip;
    }
}
