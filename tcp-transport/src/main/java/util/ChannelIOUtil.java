package util;

import server.core.ServerContext;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class ChannelIOUtil
{



    public static String readFromChannel(SocketChannel c) throws IOException
    {
        ByteBuffer buffer =  ByteBuffer.allocate(ServerContext.get().getConfiguration().getReadBufferSize());
        int readed = c.read(buffer);
        if(readed == -1)
            throw new IOException("连接关闭");
        return new String(buffer.array(), 0, readed, StandardCharsets.UTF_8);
    }

    public static void writeToChannel(String msg, SocketChannel c) throws IOException
    {
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8));
        c.write(buffer);
    }
}
