package net.vortexdata.tsqpf.console;

import net.vortexdata.tsqpf.authenticator.Authenticator;
import net.vortexdata.tsqpf.authenticator.User;
import net.vortexdata.tsqpf.authenticator.UserManager;
import net.vortexdata.tsqpf.commands.CommandInterface;
import net.vortexdata.tsqpf.exceptions.InvalidCredentialsException;
import org.apache.log4j.Level;

import javax.print.attribute.standard.JobOriginatingUserName;
import java.util.*;

/**
 * Manages user console interactions.
 *
 * @author Sandro Kierner
 * @author Michael Wiesinger
 * @since 1.0.0
 */
public class ConsoleHandler implements Runnable {

    private UserManager userManager;
    private User currentUser;
    private org.apache.log4j.Logger rootLogger;
    private Level logLevel;
    private boolean active;
    private Logger logger;
    private Authenticator authenticator;
    private Thread thread;
    private List<CommandInterface> commands = Collections.synchronizedList(new ArrayList<CommandInterface>());
    private static boolean running = false;

    public ConsoleHandler(Logger logger, org.apache.log4j.Logger rootLogger, Level logLevel) {
        this.logger = logger;
        thread = new Thread(this);
        if (running) return;
        running = true;
        authenticator = new Authenticator(logger);
        active = true;
        this.rootLogger = rootLogger;
        this.logLevel = logLevel;
        this.userManager = new UserManager(this.logger);
    }

    /**
     * Starts the console handler.
     */
    public void start() {
        thread.start();
    }

    /**
     * Registers a console command.
     *
     * @param cmd Class of command to register
     * @return True if command was successfully registerd
     */
    public boolean registerCommand(CommandInterface cmd) {
        for (CommandInterface c : commands)
            if (c.getName().equalsIgnoreCase(cmd.getName())) return false;

        commands.add(cmd);
        return true;
    }

    public List<CommandInterface> getCommands() {
        return Collections.unmodifiableList(commands);
    }

    public String[] login() {
        String[] values = new String[2];
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        clearScreen();
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

        logger.printDebug("Starting console handler... Setting console log level to off.");
        rootLogger.setLevel(Level.OFF);
        do {
            // Command Line
            Scanner scanner = new Scanner(System.in);
            scanner.useDelimiter(System.getProperty("line.separator"));
            //scanner.useDelimiter("\n");
            String line;
            String[] data;


            boolean sessionActive = false;
            clearScreen();

            // Get new Session
            do {

                String[] values = login();
                if (values[0].isEmpty() || values[1].isEmpty() || values[0].contains(" ") || values[1].contains(" "))
                    System.out.println("Username or password are incorrect, please try again.");
                else {
                    try {
                        currentUser = userManager.authenticate(values[0], values[1]);
                        sessionActive = true;
                    } catch (InvalidCredentialsException e) {
                        System.out.println("Username or password are incorrect, please try again.");
                    }
                }

            } while (!sessionActive);
            System.out.println("Successfully logged in!");

            while (sessionActive) {
                System.out.print(currentUser.getUsername() + "@local> ");
                line = scanner.next();
                data = line.split(" ");

                if (data.length > 0 && !data[0].isEmpty()) {
                    boolean commandExists = false;
                    for (CommandInterface cmd : commands) {
                        if (cmd.getName().equalsIgnoreCase(data[0])) {
                            cmd.gotCalled(Arrays.copyOfRange(data, 1, data.length));
                            commandExists = true;
                            break;
                        }
                    }
                    if (!commandExists) {
                        System.out.println(data[0] + ": command not found");
                    }
                }
            }
        } while (active);
        rootLogger.setLevel(Level.DEBUG);
        logger.printDebug("Stopping console handler... Setting console log level to debug.");

    }

    private void clearScreen() {
        for (int i = 0; i < 100; i++)
            System.out.println("");
    }

    public void shutdown() {
        active = false;
    }

}
