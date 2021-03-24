package com.jd.lab6.client.net;


import java.io.*;
import java.net.Socket;

public class TCP {
    private static Socket socket;
    private static boolean isLastConnectionSuccessful;
    private static String addr;
    private static int port = 0;
    public static void openConnection(String address, int inPort) {
        if (address != null && inPort != 0) {
            try {
                if (addr == null)
                    addr = address;
                if (port == 0)
                    port = inPort;
                socket = new Socket(addr, port);
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
        try {
            if (socket == null || !isLastConnectionSuccessful)
                TCP.openConnection(addr, port);
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
        byte[] buffer = new byte[10000];
        try {
            InputStream responseStream = socket.getInputStream();
            int responseSize = responseStream.read(buffer);
            if (responseSize == 0)
                return "Пустой ответ сервера";
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
