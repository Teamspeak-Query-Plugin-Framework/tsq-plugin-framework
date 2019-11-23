package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.console.IShell;
import net.vortexdata.tsqpf.console.Logger;

public class CommandLogout extends CommandInterface {



    public CommandLogout(Logger logger) {
        super(logger);
        CommandInterface.allowAllGroups(this);
    }

    @Override
    public String getHelpMessage() {
        return "Invalidates your current session.";
    }

    @Override
    public void execute(String[] args, IShell shell) {
        shell.logout();
        shell.getPrinter().println("Logged out");

        //getLogger().printDebug("Invalidating console handler session.");
    }

    @Override
    public String getName() {
        return "logout";
    }

}
