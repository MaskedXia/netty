package cn.itcast.nio.c4;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static cn.itcast.nio.c4.ByteBufferUtil.debugRead;

public class ServerPre {

    public static void main(String[] args) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(16);

        ServerSocketChannel ssc = ServerSocketChannel.open();

        ssc.bind(new InetSocketAddress(8080));

        List<SocketChannel> channels = new ArrayList<>();

        while (true) {
            SocketChannel sc = ssc.accept(); //阻塞
            channels.add(sc);
            System.out.println("新客户端连接："+sc.getRemoteAddress());
            for (SocketChannel channel : channels) {
//                buffer.clear();
                channel.read(buffer); // 阻塞
                buffer.flip();
                debugRead(buffer);
                System.out.println("接收到："+new String(buffer.array()));
                buffer.clear();
//                channel.write(buffer);
            }
        }
    }
}
