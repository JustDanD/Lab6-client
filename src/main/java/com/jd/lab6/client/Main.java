package com.jd.lab6.client;

import com.jd.lab6.client.net.TCP;
import com.jd.lab6.client.ui.*;

public class Main {
    public static void main(String[] args) {
        //TCP.openConnection("localhost", 2222);
        Cmd.listen();
    }
}
