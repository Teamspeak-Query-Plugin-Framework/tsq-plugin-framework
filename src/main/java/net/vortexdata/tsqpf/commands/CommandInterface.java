package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.console.Logger;

/**
 * Interface for Commands
 *
 * @author Michael Wiesinger
 * @since 1.0.0
 */
public abstract class CommandInterface {

    private Logger _logger;

    public CommandInterface(Logger logger) {
        _logger = logger;
    }

    protected Logger getLogger() {
        return _logger;
    }

    /**
     * This message is shown with the help command.
     * @return      Desired help message.
     */
    abstract public String getHelpMessage();

    /**
     * This method is run when the user runs the command.
     */
    abstract public void gotCalled(String[] args);

    /**
     * This method is run when the user runs the command.
     * @return          Prefix of the command.
     */
    abstract public String getName();
}
