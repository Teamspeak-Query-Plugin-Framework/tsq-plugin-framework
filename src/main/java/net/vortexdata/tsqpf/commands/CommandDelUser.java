package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.authenticator.UserGroup;
import net.vortexdata.tsqpf.authenticator.UserManager;
import net.vortexdata.tsqpf.console.ConsoleHandler;
import net.vortexdata.tsqpf.console.Logger;
import net.vortexdata.tsqpf.console.VirtualTerminal;

public class CommandDelUser extends CommandInterface {

    UserManager userManager;

    public CommandDelUser(Logger logger, ConsoleHandler consoleHandler) {
        super(logger);
        this.userManager = consoleHandler.getUserManager();
        groups.add(UserGroup.ROOT);
    }

    @Override
    public String getHelpMessage() {
        return "Deletes a user";
    }

    @Override
    public void gotCalled(String[] args, VirtualTerminal terminal) {
        if (args.length == 0) {
            terminal.println("Please specify a username.");
            return;
        } else {
            if (args[0].equalsIgnoreCase("ROOT")) {
                terminal.println("Root user can not be deleted.");
                return;
            }
            boolean success = userManager.deleteUser(args[0]);
            if (success)
                terminal.println("User " + args[0] + " deleted.");
            else
                terminal.println("User " + args[0] + " does not exist.");
        }
    }

    @Override
    public String getName() {
        return "deluser";
    }

}
