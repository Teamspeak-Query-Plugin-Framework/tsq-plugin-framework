package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.authenticator.*;
import net.vortexdata.tsqpf.console.*;

public class CommandDelUser extends CommandInterface {

    UserManager userManager;

    public CommandDelUser(Logger logger, ConsoleHandler consoleHandler) {
        super(logger);
        this.userManager = consoleHandler.getUserManager();
    }

    @Override
    public String getHelpMessage() {
        return "Deletes a user";
    }

    @Override
    public void gotCalled(String[] args) {
        if (args.length == 1) {
            System.out.println("Please specify a username.");
            return;
        } else {
            boolean success = userManager.deleteUser(args[1]);
            if (success)
                System.out.println("User " + args[1] + " deleted.");
            else
                System.out.println("User " + args[1] + " does not exist.");
        }
    }

    @Override
    public String getName() {
        return "deluser";
    }

}
