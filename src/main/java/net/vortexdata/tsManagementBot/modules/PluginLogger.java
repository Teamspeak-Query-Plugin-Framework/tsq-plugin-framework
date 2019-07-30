package net.vortexdata.tsManagementBot.modules;

import net.vortexdata.tsManagementBot.Bot;
import net.vortexdata.tsManagementBot.console.BotLogger;
import net.vortexdata.tsManagementBot.console.Logger;

public class PluginLogger extends Logger {

    private PluginContainer _container;

    public PluginLogger(Bot bot, PluginContainer container) {
        super(bot);
        _container = container;
    }

    @Override
    public void printDebug(String message) {
        super.printDebug("["+_container.getPluginName()+"] " +message);
    }

    @Override
    public void printError(String message) {
        super.printError("["+_container.getPluginName()+"] " +message);
    }

    @Override
    public void printToConsole(String message) {
        super.printToConsole("["+_container.getPluginName()+"] " +message);
    }
}
