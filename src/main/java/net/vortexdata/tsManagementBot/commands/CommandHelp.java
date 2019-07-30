package net.vortexdata.tsManagementBot.commands;

import net.vortexdata.tsManagementBot.Bot;
import net.vortexdata.tsManagementBot.console.ConsoleHandler;
import net.vortexdata.tsManagementBot.console.Logger;

import java.util.Arrays;

public class CommandHelp extends CommandInterface {

    private ConsoleHandler _handler;

    public CommandHelp(Logger logger, ConsoleHandler handler) {
        super(logger);
        _handler = handler;
    }

    @Override
    public String getHelpMessage() {
        return "You want help for help? That's kinda genius :)";
    }

    public void gotCalled(String[] args) {

        if(args.length > 0)
        for (CommandInterface cmd : _handler.getCommands()) {
            if (cmd.getName().equalsIgnoreCase(args[0])) {
                getLogger().printToConsole(cmd.getHelpMessage());
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
