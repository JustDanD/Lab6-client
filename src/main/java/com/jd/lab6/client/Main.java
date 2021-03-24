package com.jd.lab6.client;

import com.jd.lab6.client.net.TCP;
import com.jd.lab6.client.ui.Cmd;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Введите адрес:");
        String address = in.nextLine();
        int port;
        while (true) {
            try {
                System.out.println("Введите порт:");
                port = Integer.parseInt(in.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Порт должен быть интом");
            }
        }
        TCP.openConnection(address, port);
        Cmd.listen();
        TCP.closeConnection();
    }
}
