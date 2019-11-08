package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.console.ConsoleCommandHandler;
import net.vortexdata.tsqpf.console.LocalConsole;
import net.vortexdata.tsqpf.console.Logger;
import net.vortexdata.tsqpf.console.VirtualTerminal;

public class CommandLogout extends CommandInterface {

    private LocalConsole localConsole;

    public CommandLogout(Logger logger, LocalConsole localConsole) {
        super(logger);
        this.localConsole = localConsole;
    }

    @Override
    public String getHelpMessage() {
        return "Invalidates your current session.";
    }

    @Override
    public void gotCalled(String[] args, VirtualTerminal terminal) {
        localConsole.logout();
        getLogger().printDebug("Invalidating console handler session.");
    }

    @Override
    public String getName() {
        return "logout";
    }

}
