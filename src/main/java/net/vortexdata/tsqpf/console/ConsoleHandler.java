package net.vortexdata.tsqpf.console;

import net.vortexdata.tsqpf.commands.CommandInterface;

import java.util.*;

public class ConsoleHandler implements Runnable {

    private Thread thread;
    private List<CommandInterface> commands = Collections.synchronizedList(new ArrayList<CommandInterface>());
    private static boolean running = false;
    public ConsoleHandler() {
        thread = new Thread(this);
        if(running) return;
        running = true;


    }

    public void start() {
        thread.start();
    }


    public boolean registerCommand(CommandInterface cmd) {
        for(CommandInterface c : commands)
            if(c.getName().equalsIgnoreCase(cmd.getName())) return false;

        commands.add(cmd);
        return true;
    }

    public List<CommandInterface> getCommands() {
        return Collections.unmodifiableList(commands);
    }
    public void run() {

        System.out.println("-----------------------------------------------------------------------------------");
        System.out.println("Teamspeak Query Plugin Framework Terminal | Copyright (C) 2019 VortexdataNET");
        System.out.println("TSQPF comes with ABSOLUTELY NO WARRANTY, to the extent permitted by applicable law.");
        System.out.println("-----------------------------------------------------------------------------------");

        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter(System.getProperty("line.separator"));
        //scanner.useDelimiter("\n");
        String line;
        String[] data;
        while (true) {
            System.out.print("admin@> ");
            line = scanner.next();
            data = line.split(" ");

            if (data.length > 0 && !data[0].isEmpty()) {
                boolean commandExists = false;
                for (CommandInterface cmd : commands) {
                    if (cmd.getName().equalsIgnoreCase(data[0])) {
                        cmd.gotCalled(Arrays.copyOfRange(data, 1, data.length));
                        commandExists = true;
                        break;
                    }
                }
                if (!commandExists) {
                    System.out.println(data[0] + ": command not found");
                }
            }
        }

    }

}
