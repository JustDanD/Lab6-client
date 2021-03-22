package com.jd.lab6.client.ui;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;
import java.util.TreeSet;

import com.jd.lab6.client.net.TCP;
import com.jd.lab6.commands.*;

public class Cmd {
    private static final HashMap<String, Class<? extends Command>> commandsMap;

    static {
        commandsMap = new HashMap<>();
        commandsMap.put("exit", ExitCommand.class);
        commandsMap.put("help", HelpCommand.class);
        commandsMap.put("info", InfoCommand.class);
        commandsMap.put("show", ShowCommand.class);
        commandsMap.put("add", AddCommand.class);
        commandsMap.put("update", UpdateCommand.class);
        commandsMap.put("remove_by_id", RemoveCommand.class);
        commandsMap.put("clear", ClearCommand.class);
        commandsMap.put("add_if_min", AddMinCommand.class);
        commandsMap.put("remove_lower", RemoveLowerCommand.class);
        commandsMap.put("history", HistoryCommand.class);
        commandsMap.put("print_descending", PrintDescendingCommand.class);
        commandsMap.put("remove_any_by_chapter", RemoveByChapterCommand.class);
        commandsMap.put("group_counting_by_heart_count", GroupCommand.class);
        commandsMap.put("execute_script", ExecuteScriptCommand.class);
    }

    public static void listen() {
        Scanner in = new Scanner(System.in);
        String curCom = "";
        String[] curArgs;
        Class[] params = {String[].class, TreeSet.class, boolean.class};
        System.out.println("Доброго времени суток, уважаемый юзер.\nДобро пожаловать в систему управления вашей коллекцией космических корбалей!\nПриятного пользования!\nДля просмотра существующих команд введите help.");
        while (true) {
            try {
                curCom = in.next();
                curArgs = in.nextLine().replaceAll(" +", " ").split(" ");
                Class<? extends Command> command = commandsMap.get(curCom);
                if (command != null) {
                    Command executedCom = (command.getConstructor(params).newInstance(curArgs, null, true));
                    if (executedCom instanceof ExitCommand) {
                        TCP.closeConnection();
                        executedCom.execute();
                    }
                    if (executedCom.getValid()) {
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        ObjectOutputStream ois = new ObjectOutputStream(bytes);
                        ois.writeObject(executedCom);
                        TCP.sendCommand(bytes);
                        System.out.println(TCP.waitResponse());
                    }
                    else
                        System.out.println("Команда невалидна");
                } else
                    System.out.println("Такой команды не существует");
            } catch (Exception e) {
                if (!in.hasNext()) {
                    System.out.println("Входной поток умер. Возможно, вы нажали ctrl + D. Аварийное(не очень) закрытие.");
                    System.exit(-1);
                }
                System.out.println(e.getMessage());
            }
        }
    }
}
