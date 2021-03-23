package com.jd.lab6.client.net;


import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;

public class TCP {
    private static Socket socket;
    private static boolean isLastConnectionSuccessful;

    public static void openConnection(String address, int port) {
        if (address != null && port != 0) {
            try {
                socket = new Socket(address, port);
            } catch (java.io.IOException e) {
                return;
            }
            isLastConnectionSuccessful = true;
        }
    }

    public static void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean sendCommand(ByteArrayOutputStream out) {
        byte[] buffer = out.toByteArray();
        ByteBuffer buf = ByteBuffer.wrap(buffer);
        try {
            if (socket == null || !isLastConnectionSuccessful)
                TCP.openConnection("localhost", 2222);
            if (socket == null)
                throw new IOException();
            OutputStream requestStream = socket.getOutputStream();
            requestStream.write(buffer);
            isLastConnectionSuccessful = true;
            return true;
        } catch (java.io.IOException e) {
            System.out.println("Ошибка соединения с сервером. Возможно, сервер временно недоступен или выключен");
            isLastConnectionSuccessful = false;
            return false;
        }
    }

    public static String waitResponse() {
        ByteBuffer responseBuf = ByteBuffer.wrap(new byte[10000]);
        byte[] buffer = new byte[10000];
        try {
            InputStream responseStream = socket.getInputStream();
            responseStream.read(buffer);
            ByteArrayInputStream responseBytes = new ByteArrayInputStream(buffer);
            ObjectInputStream ois = new ObjectInputStream(responseBytes);
            return (String) ois.readObject();
        } catch (IOException e) {
            isLastConnectionSuccessful = false;
            return "Ошибка получения ответа от сервера.";
        } catch (ClassNotFoundException e) {
            return "Битый ответ сервера";
        }
    }
}
