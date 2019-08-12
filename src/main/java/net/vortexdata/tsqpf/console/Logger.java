package net.vortexdata.tsqpf.console;

import net.vortexdata.tsqpf.Framework;

public abstract class Logger {

    private Framework _Framework;

    public Logger(Framework Framework) {
        _Framework = Framework;
    }

    public void printInfo(String message) {
        _Framework.getRootLogger().info(message);
    }

    public void printDebug(String message) {
        _Framework.getRootLogger().debug(message);
    }

    public void printError(String message, Exception e) {
        _Framework.getRootLogger().error(message, e);
    }
    public void printError(String message) {
        _Framework.getRootLogger().error(message);
    }

    public void printWarn(String message, Exception e) {
        _Framework.getRootLogger().warn(message, e);
    }
    public void printWarn(String message) {
        _Framework.getRootLogger().warn(message);
    }

}
