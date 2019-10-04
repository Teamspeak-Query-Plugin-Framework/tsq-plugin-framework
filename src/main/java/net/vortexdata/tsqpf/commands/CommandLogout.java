package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.console.ConsoleHandler;
import net.vortexdata.tsqpf.console.Logger;
import net.vortexdata.tsqpf.console.VirtualTerminal;

public class CommandLogout extends CommandInterface {

    private ConsoleHandler consoleHandler;

    public CommandLogout(Logger logger, ConsoleHandler consoleHandler, VirtualTerminal terminal) {
        super(logger, terminal);
        this.consoleHandler = consoleHandler;
    }

    @Override
    public String getHelpMessage() {
        return "Invalidates your current session.";
    }

    @Override
    public void gotCalled(String[] args) {
        consoleHandler.logout();
        getLogger().printDebug("Invalidating console handler session.");
    }

    @Override
    public String getName() {
        return "logout";
    }

}
