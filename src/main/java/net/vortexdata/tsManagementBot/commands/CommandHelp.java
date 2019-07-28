package net.vortexdata.tsManagementBot.commands;

import net.vortexdata.tsManagementBot.Bot;

import java.util.Arrays;

public class CommandHelp implements CommandInterface {

    @Override
    public String getHelpMessage() {
        return "You want help for help? That's kinda genius :)";
    }

    public void gotCalled(String[] args) {

        if(args.length > 0)
        for (CommandInterface cmd : Bot.getBot().getConsoleHandler().getCommands()) {
            if (cmd.getName().equalsIgnoreCase(args[0])) {
                Bot.getBot().getLogger().info(cmd.getHelpMessage());
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
