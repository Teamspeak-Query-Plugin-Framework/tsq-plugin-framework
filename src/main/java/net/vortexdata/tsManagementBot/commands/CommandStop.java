package net.vortexdata.tsManagementBot.commands;

import net.vortexdata.tsManagementBot.*;

public class CommandStop implements CommandInterface {

    @Override
    public String getHelpMessage() {
        return "Unloads all plugins and ends the program.";
    }

    @Override
    public void gotCalled(String[] args) {
        Bot.getBot().shutdown();
    }

    @Override
    public String getName() {
        return "stop";
    }
}
