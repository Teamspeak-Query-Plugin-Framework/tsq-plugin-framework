package net.vortexdata.tsManagementBot.console;

import jdk.nashorn.internal.runtime.ECMAErrors;
import net.vortexdata.tsManagementBot.Bot;

public abstract class Logger {

    private Bot _bot;

    public Logger(Bot bot) {
        _bot = bot;
    }

    public void printToConsole(String message) {
        _bot.getRootLogger().info(message);
    }
    public void printDebug(String message) {
        _bot.getRootLogger().debug(message);
    }
    public void printError(String message, Exception e) {
        _bot.getRootLogger().error(message, e);
    }
    public void printError(String message) {
        _bot.getRootLogger().error(message);
    }

}
