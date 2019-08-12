package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.console.Logger;

public abstract class CommandInterface {

    private Logger _logger;

    public CommandInterface(Logger logger) {
        _logger = logger;
    }

    protected Logger getLogger() {
        return _logger;
    }
    abstract public String getHelpMessage();
    abstract public void gotCalled(String[] args);
    abstract public String getName();
}
