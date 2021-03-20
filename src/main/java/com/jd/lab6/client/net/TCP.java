package com.jd.lab6.client.net;

import com.jd.lab6.commands.Command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TCP {
    private static SocketChannel socket;

    public static void openConnection(String address, int port) {
        if (address != null && port != 0) {
            SocketAddress addr = new InetSocketAddress(address, port);
          try {
              socket = SocketChannel.open(addr);
          } catch (java.io.IOException e) {
              System.out.println(e.getMessage());
          }
        }
    }
    public static void sendCommand (ByteArrayOutputStream out) {
        byte[] b = out.toByteArray();
        ByteBuffer buf = ByteBuffer.wrap(b);
        try {
            openConnection("localhost", 2222);
            socket.write(buf);
        } catch (java.io.IOException e) {
                System.out.println(e.getMessage());
        }
    }
    public static String waitCallback() {
        ByteBuffer buf = ByteBuffer.wrap(new byte[10000]);
        String b;
        try {
            socket.read(buf);
            b = new String(buf.array());
            return b;
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
        return "Empty output";
    }
}
