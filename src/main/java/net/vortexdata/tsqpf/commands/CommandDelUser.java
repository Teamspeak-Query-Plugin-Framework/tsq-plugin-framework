package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.authenticator.UserGroup;
import net.vortexdata.tsqpf.authenticator.UserManager;
import net.vortexdata.tsqpf.console.IShell;
import net.vortexdata.tsqpf.console.Logger;

public class CommandDelUser extends CommandInterface {

    UserManager userManager;

    public CommandDelUser(Logger logger, UserManager userManager) {
        super(logger);
        this.userManager = userManager;
        groups.add(UserGroup.ROOT);
    }

    @Override
    public String getHelpMessage() {
        return "Deletes a user";
    }

    @Override
    public void execute(String[] args, IShell shell) {
        if (args.length == 0) {
            shell.getPrinter().println("Please specify a username.");
            return;
        } else {
            if (args[0].equalsIgnoreCase("ROOT")) {
                shell.getPrinter().println("Root user can not be deleted.");
                return;
            }
            boolean success = userManager.deleteUser(args[0]);
            if (success)
                shell.getPrinter().println("User " + args[0] + " deleted.");
            else
                shell.getPrinter().println("User " + args[0] + " does not exist.");
        }
    }

    @Override
    public String getName() {
        return "deluser";
    }

}
