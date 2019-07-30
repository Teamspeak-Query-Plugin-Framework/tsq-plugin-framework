package net.vortexdata.tsManagementBot.commands;

import net.vortexdata.tsManagementBot.*;
import net.vortexdata.tsManagementBot.console.Logger;

public class CommandStop extends CommandInterface {

    private Bot _bot;

    public CommandStop(Logger logger, Bot bot) {
        super(logger);
        _bot = bot;
    }

    @Override
    public String getHelpMessage() {
        return "Unloads all plugins and ends the program.";
    }

    @Override
    public void gotCalled(String[] args) {_bot.shutdown();
    }

    @Override
    public String getName() {
        return "stop";
    }
}
