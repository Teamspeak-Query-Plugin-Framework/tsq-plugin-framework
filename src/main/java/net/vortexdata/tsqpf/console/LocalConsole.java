package net.vortexdata.tsqpf.console;

import net.vortexdata.tsqpf.authenticator.User;
import net.vortexdata.tsqpf.authenticator.UserManager;
import net.vortexdata.tsqpf.commands.CommandInterface;
import net.vortexdata.tsqpf.exceptions.InvalidCredentialsException;
import org.apache.log4j.Level;

import java.util.*;

/**
 * Manages user console interactions.
 *
 * @author Sandro Kierner
 * @author Michael Wiesinger
 * @since 1.0.0
 */
public class LocalConsole implements Runnable {

    private static boolean running = false;
    private UserManager userManager;
    private User currentUser;
    private org.apache.log4j.Logger rootLogger;
    private Level logLevel;
    private boolean active;
    private Logger logger;
    private Thread thread;
    private ConsoleCommandHandler consoleCommandHandler;

    private boolean didRootExist = false;
    private boolean resetRoot = false;

    public LocalConsole(Logger logger, org.apache.log4j.Logger rootLogger, Level logLevel, boolean resetRoot, ConsoleCommandHandler consoleCommandHandler, UserManager userManager) {
        this.logger = logger;
        thread = new Thread(this);
        if (running) return;
        running = true;
        active = true;
        this.rootLogger = rootLogger;
        this.logLevel = logLevel;
        this.userManager = userManager;
        this.resetRoot = resetRoot;
        this.consoleCommandHandler = consoleCommandHandler;
    }

    /**
     * Starts the console handler.
     */
    public void start() {
        thread.start();
    }





    public String[] login() {
        String[] values = new String[2];
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        System.out.println("Authentication required, please sign in to proceed.");
        System.out.print("Username: ");
        values[0] = scanner.nextLine();
        System.out.print("Password: ");
        values[1] = scanner.nextLine();
        return values;
    }

    /**
     * Starts the console UI.
     */




    public void run() {
        ConsoleTerminal terminal = new ConsoleTerminal();

        userManager.reloadUsers();
        Scanner scanner = new Scanner(System.in);
        String line = "";
        String[] data;

        logger.printDebug("Looking for root user account...");

        if (!userManager.doesRootUserExist()) {
            didRootExist = false;
            userManager.generateRootUser();
        } else {
            logger.printDebug("Root user found.");
            if (resetRoot) {
                resetRoot();
            }
        }

        do {
            do {
                String[] values = login();
                try {
                    User loginAt = userManager.authenticate(values[0], values[1]);
                    if (loginAt.getPassword().equals(userManager.getPasswordHash(values[1]))) {
                        currentUser = loginAt;
                    }
                } catch (InvalidCredentialsException e) {
                    System.out.println("Invalid username or password, please try again.");
                }
            } while (currentUser == null);
            System.out.println("Sign in approved.");

            while (currentUser != null) {
                System.out.print(currentUser.getUsername() + "@local> ");

                line = scanner.nextLine();
                consoleCommandHandler.processInput(line, currentUser, terminal);
            }
        } while (active);

    }

    @Deprecated
    private void clearScreen() {
        try {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows")) {
                Runtime.getRuntime().exec("cls");
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (final Exception e) {
            //  Handle any exceptions
        }
    }

    public void shutdown() {
        active = false;
    }

    public void logout() {
        currentUser = null;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void resetRoot() {
        userManager.deleteUser("root", true);
        userManager.generateRootUser();
    }

    public boolean isDidRootExist() {
        return didRootExist;
    }
}
