package com.jd.lab6.client.net;

import com.sun.xml.internal.ws.api.ha.StickyFeature;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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

    public static void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void sendCommand(ByteArrayOutputStream out) {
        byte[] b = out.toByteArray();
        ByteBuffer buf = ByteBuffer.wrap(b);
        try {
            socket.write(buf);
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static String waitResponse() {
        ByteBuffer responseBuf = ByteBuffer.wrap(new byte[10000]);
        String b;
        try {
            socket.read(responseBuf);
            ByteArrayInputStream responseBytes = new ByteArrayInputStream(responseBuf.array());
            ObjectInputStream ois = new ObjectInputStream(responseBytes);
            return (String) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return "Empty output";
    }
}
