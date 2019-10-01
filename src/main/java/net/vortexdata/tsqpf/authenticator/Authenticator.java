package net.vortexdata.tsqpf.authenticator;

import net.vortexdata.tsqpf.console.FrameworkLogger;
import net.vortexdata.tsqpf.exceptions.InvalidCredentialsException;

/**
 * Manages user authentication
 *
 * @author Sandro Kierner
 * @since 2.0.0
 */
public class Authenticator {

    private FrameworkLogger logger;
    UserManager userManager;
    public Authenticator(FrameworkLogger logger) {
        this.logger = logger;
        userManager = new UserManager(this.logger);
    }

    /**
     * Returns true if login attempt is successful
     *
     * @param username                      The users username / unique identifier
     * @param password                      The users password in plain text
     * @return                              true if username and password are correct
     */
    public boolean authenticate(String username, String password) {
        try {
            userManager.authenticate(username, password);
            return true;
        } catch (InvalidCredentialsException e) {
            return false;
        }
    }

}