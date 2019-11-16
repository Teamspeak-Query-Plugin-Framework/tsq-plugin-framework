package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.console.ConsoleCommandHandler;
import net.vortexdata.tsqpf.console.LocalConsole;
import net.vortexdata.tsqpf.console.Logger;
import net.vortexdata.tsqpf.console.VirtualTerminal;

/**
 * Displays help on console
 *
 * @author Sandro Kierner
 * @author Michael Wiesinger
 * @since 1.0.0
 */
public class CommandHelp extends CommandInterface {

    private ConsoleCommandHandler consoleCommandHandler;

    public CommandHelp(Logger logger, ConsoleCommandHandler handler) {
        super(logger);
        consoleCommandHandler = handler;
    }


    @Override
    public String getHelpMessage() {
        return "You need help with help? That's kinda genius :)";
    }

    public void gotCalled(String[] args, VirtualTerminal terminal) {

        if (args.length > 0) {
            for (CommandInterface cmd : consoleCommandHandler.getCommands()) {
                if (cmd.getName().equalsIgnoreCase(args[0])) {
                    terminal.println(cmd.getHelpMessage());
                    return;
                }
            }
        } else if (args.length > 1) {
            terminal.println("Incremented help is not supported in this build.");
        } else {
            for (CommandInterface command : consoleCommandHandler.getCommands()) {
                terminal.println(command.getName() + ": \t\t\t\t" + command.getHelpMessage());
            }
        }

    }

    public String getName() {
        return "help";
    }
}
