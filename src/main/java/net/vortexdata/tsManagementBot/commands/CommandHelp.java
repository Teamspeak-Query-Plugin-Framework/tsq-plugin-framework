package net.vortexdata.tsManagementBot.commands;

import net.vortexdata.tsManagementBot.Bot;

import java.util.Arrays;

public class CommandHelp implements CommandInterface {

    public void gotCalled(String[] args) {

        System.out.println("help \t\t\t\t\t Displays a list of all commands.");
        System.out.println("stop \t\t\t\t\t Shuts down the bot.");

    }

    public String getName() {
        return "help";
    }
}
