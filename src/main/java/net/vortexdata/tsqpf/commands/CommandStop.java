package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.*;
import net.vortexdata.tsqpf.console.Logger;

/**
 * Shuts down the framework
 *
 * @author Sandro Kierner
 * @since 1.0.0
 */
public class CommandStop extends CommandInterface {

    private Framework _Framework;

    public CommandStop(Logger logger, Framework Framework) {
        super(logger);
        _Framework = Framework;
    }

    @Override
    public String getHelpMessage() {
        return "Unloads all plugins and ends the program.";
    }

    @Override
    public void gotCalled(String[] args) {
        _Framework.shutdown(true);
    }

    @Override
    public String getName() {
        return "stop";
    }
}
