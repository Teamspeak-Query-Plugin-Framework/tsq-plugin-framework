package net.vortexdata.tsManagementBot.commands;

import net.vortexdata.tsManagementBot.Bot;

import java.util.Arrays;

public class CommandHelp implements CommandInterface {


    public void gotCalled(String[] args) {
        Bot.getBot().getLogger().info(Arrays.toString(args));
    }

    public String getName() {

        return "help";
    }
}
