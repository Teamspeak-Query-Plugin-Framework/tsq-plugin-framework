package net.vortexdata.tsqpf.console;

import net.vortexdata.tsqpf.authenticator.Authenticator;
import net.vortexdata.tsqpf.commands.CommandInterface;
import org.apache.log4j.Level;

import java.util.*;

/**
 * Manages user console interactions.
 *
 * @author Sandro Kierner
 * @author Michael Wiesinger
 * @since 1.0.0
 */
public class ConsoleHandler implements Runnable {

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

    public boolean login() {
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        clearScreen();
        System.out.println("Authentication required, please sign in to proceed.");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        return authenticator.authenticate(username, password);
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

                sessionActive = login();
                if (!sessionActive)
                    System.out.println("Username or password are incorrect, please try again.");

            } while (!sessionActive);
            System.out.println("Successfully logged in!");

            while (sessionActive) {
                System.out.print("admin@> ");
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

    private void shutdown() {
        active = false;
    }

}
