package net.vortexdata.tsqpf.authenticator;

import net.vortexdata.tsqpf.console.FrameworkLogger;
import net.vortexdata.tsqpf.exceptions.InvalidCredentialsException;

public class Authenticator {

    private FrameworkLogger logger;
    UserManager userManager;
    public Authenticator(FrameworkLogger logger) {
        this.logger = logger;
        userManager = new UserManager(this.logger);
    }

    public boolean authenticate(String username, String password) {
        try {
            userManager.authenticate(username, password);
            return true;
        } catch (InvalidCredentialsException e) {
            return false;
        }
    }

}