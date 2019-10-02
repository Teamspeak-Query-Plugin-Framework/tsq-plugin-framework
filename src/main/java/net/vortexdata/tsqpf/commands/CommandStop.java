package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.Framework;
import net.vortexdata.tsqpf.console.Logger;

/**
 * Shuts down the framework
 *
 * @author Sandro Kierner
 * @since 1.0.0
 */
public class CommandStop extends CommandInterface {

    private Framework framework;

    public CommandStop(Logger logger, Framework Framework) {
        super(logger);
        framework = Framework;
    }

    @Override
    public String getHelpMessage() {
        return "Unloads all plugins and ends the program.";
    }

    @Override
    public void gotCalled(String[] args) {
        framework.shutdown(true);
    }

    @Override
    public String getName() {
        return "stop";
    }
}
