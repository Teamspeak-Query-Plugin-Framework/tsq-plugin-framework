package net.vortexdata.tsqpf.console;

import net.vortexdata.tsqpf.Framework;

public abstract class Logger {

    private Framework framework;

    public Logger(Framework Framework) {
        framework = Framework;
    }

    public void printInfo(String message) {
        framework.getRootLogger().info(message);
    }

    public void printDebug(String message) {
        framework.getRootLogger().debug(message);
    }

    public void printError(String message, Exception e) {
        framework.getRootLogger().error(message, e);
    }

    public void printError(String message) {
        framework.getRootLogger().error(message);
    }

    public void printWarn(String message, Exception e) {
        framework.getRootLogger().warn(message, e);
    }

    public void printWarn(String message) {
        framework.getRootLogger().warn(message);
    }

}
