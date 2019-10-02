package net.vortexdata.tsqpf.modules;

import net.vortexdata.tsqpf.Framework;
import net.vortexdata.tsqpf.console.Logger;

/**
 * Wrapper class adding prefixes for logging framework
 *
 * @author Michael Wiesinger
 * @author Sandro Kierner
 * @since 1.0.0
 */
public class PluginLogger extends Logger {

    private PluginContainer pluginContainer;

    public PluginLogger(Framework Framework, PluginContainer container) {
        super(Framework);
        pluginContainer = container;
    }

    /**
     * Logs a message to console or log file on debug level.
     *
     * @param message Message that gets logged to console or log file.
     */
    @Override
    public void printDebug(String message) {
        super.printDebug("[" + pluginContainer.getPluginName() + "] " + message);
    }

    /**
     * Logs a message to console or log file on error level.
     *
     * @param message Message that gets logged to console or log file.
     */
    @Override
    public void printError(String message) {
        super.printError("[" + pluginContainer.getPluginName() + "] " + message);
    }

    /**
     * Logs a message to console or log file on warning level.
     *
     * @param message Message that gets logged to console or log file.
     */
    @Override
    public void printWarn(String message) {
        super.printWarn("[" + pluginContainer.getPluginName() + "] " + message);
    }

    /**
     * Logs a message to console or log file on info level.
     *
     * @param message Message that gets logged to console or log file.
     */
    @Override
    public void printInfo(String message) {
        super.printInfo("[" + pluginContainer.getPluginName() + "] " + message);
    }
}
