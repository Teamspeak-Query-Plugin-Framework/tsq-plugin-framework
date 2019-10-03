package net.vortexdata.tsqpf.commands;

import net.vortexdata.tsqpf.authenticator.*;
import net.vortexdata.tsqpf.console.*;

public class CommandDelUser extends CommandInterface {

    UserManager userManager;

    public CommandDelUser(Logger logger, UserManager userManager) {
        super(logger);
        this.userManager = userManager;
    }

    @Override
    public String getHelpMessage() {
        return null;
    }

    @Override
    public void gotCalled(String[] args) {

    }

    @Override
    public String getName() {
        return null;
    }

}
