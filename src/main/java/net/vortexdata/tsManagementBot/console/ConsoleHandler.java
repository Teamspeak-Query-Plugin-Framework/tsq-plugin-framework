package net.vortexdata.tsManagementBot.console;

import net.vortexdata.tsManagementBot.commands.CommandInterface;

import java.util.*;

public class ConsoleHandler implements Runnable {

    private Thread thread;
    private ArrayList<CommandInterface> commands = new ArrayList<CommandInterface>();
    private static boolean running = false;
    public ConsoleHandler() {
        thread = new Thread(this);
        if(running) return;
        running = true;
        start();

    }

    private void start() {
        thread.start();
    }


    public void registerCommand(CommandInterface cmd) {
        commands.add(cmd);
    }
    public void run() {

        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        String line;
        String[] data;
        while (true) {
            line = scanner.next();

            data = line.split(" ");
            if(data.length < 1) continue;
            for (CommandInterface cmd : commands) {
                if(cmd.getName().equalsIgnoreCase(data[0])) {
                    cmd.gotCalled(Arrays.copyOfRange(data,1 ,data.length));
                }
            }
        }

    }

}
