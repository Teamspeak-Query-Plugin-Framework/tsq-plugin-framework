package net.vortexdata.tsqpf.authenticator;

import net.vortexdata.tsqpf.console.FrameworkLogger;

public class Authenticator {

    private FrameworkLogger logger;

    public Authenticator(FrameworkLogger logger) {
        this.logger = logger;
    }

    UserManager userManager = new UserManager(logger);

    public boolean authenticate(String username, String password) {

    }

}
