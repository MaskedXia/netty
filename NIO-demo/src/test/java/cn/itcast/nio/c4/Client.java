package cn.itcast.nio.c4;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress("localhost", 8080));
        SocketAddress address = sc.getLocalAddress();
        System.out.println("waiting...");


        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String next = scanner.next();
            if ("quit".equalsIgnoreCase(next)) {
                sc.close();
                break;
            }
            sc.write(Charset.defaultCharset().encode(next));

        }

        scanner.close();
        sc.close();

    }
}