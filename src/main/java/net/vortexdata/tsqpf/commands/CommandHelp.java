package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.console.CommandContainer;
import net.vortexdata.tsqpf.console.IShell;
import net.vortexdata.tsqpf.console.Logger;

/**
 * Displays help on console
 *
 * @author Sandro Kierner
 * @author Michael Wiesinger
 * @since 1.0.0
 */
public class CommandHelp extends CommandInterface {

    public CommandHelp(Logger logger) {
        super(logger);
        CommandInterface.allowAllGroups(this);
    }

    @Override
    public String getHelpMessage() {
        return "You need help with help? That's kinda genius :)";
    }

    public void execute(String[] args, IShell shell) {

        if (args.length > 0) {
            for (CommandInterface cmd : CommandContainer.getCommands()) {
                if (cmd.getName().equalsIgnoreCase(args[0])) {
                    shell.getPrinter().println(cmd.getHelpMessage());
                    return;
                }
            }
        } else if (args.length > 1) {
            shell.getPrinter().println("Incremented help is not supported in this build.");
        } else {
            for (CommandInterface command : CommandContainer.getCommands()) {
                shell.getPrinter().println(command.getName() + ": \t\t\t\t" + command.getHelpMessage());
            }
        }

    }

    public String getName() {
        return "help";
    }
}
