package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.console.ConsoleHandler;
import net.vortexdata.tsqpf.console.Logger;

/**
 * Displays help on console
 *
 * @author Sandro Kierner
 * @author Michael Wiesinger
 * @since 1.0.0
 */
public class CommandHelp extends CommandInterface {

    private ConsoleHandler consoleHandler;

    public CommandHelp(Logger logger, ConsoleHandler handler) {
        super(logger);
        consoleHandler = handler;
    }


    @Override
    public String getHelpMessage() {
        return "You need help with help? That's kinda genius :)";
    }

    public void gotCalled(String[] args) {

        if (args.length > 0)
            for (CommandInterface cmd : consoleHandler.getCommands()) {
                if (cmd.getName().equalsIgnoreCase(args[0])) {
                    getLogger().printInfo(cmd.getHelpMessage());
                    return;
                }
            }

        System.out.println("help \t\t\t\t\t Displays a list of all commands.");
        System.out.println("stop \t\t\t\t\t Shuts down the bot.");

    }

    public String getName() {
        return "help";
    }
}
