package net.vortexdata.tsqpf;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.TS3Listener;
import com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;
import net.vortexdata.tsqpf.commands.*;
import net.vortexdata.tsqpf.configs.ConfigMain;
import net.vortexdata.tsqpf.console.ConsoleHandler;
import net.vortexdata.tsqpf.console.FrameworkLogger;
import net.vortexdata.tsqpf.installers.InstallWizzard;
import net.vortexdata.tsqpf.listeners.ChatCommandListener;
import net.vortexdata.tsqpf.listeners.GlobalEventHandler;
import net.vortexdata.tsqpf.modules.BootHandler;
import net.vortexdata.tsqpf.modules.PluginManager;
import net.vortexdata.tsqpf.remoteShell.ConnectionListener;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Copyright (C) VortexdataNET - All Rights Reserved
 * Unauthorized redistribution of this software, via any medium is prohibited!
 *
 * @author Sandro Kierner (sandro@vortexdata.net)
 * @author Michael Wiesinger (michael@vortexdata.net)
 */
public class Framework {

    private static final Logger rootLogger = LogManager.getRootLogger();
    private static Framework instance;
    private TS3Api api;
    private ConsoleHandler consoleHandler;
    private ChatCommandListener chatCommandListener;
    private PluginManager pluginManager;
    private net.vortexdata.tsqpf.console.Logger logger;
    private ConnectionListener connectionListener;

    public static void main(String[] args) {

        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("-debug")) {
                rootLogger.setLevel(Level.DEBUG);
            } else if (args[i].contains("-setup")) {
                InstallWizzard installWizzard = new InstallWizzard();
                installWizzard.init();
            }
        }

        instance = new Framework();
        instance.init();
    }

    private void init() {

        // Display Copy Header & wait 1 seconds
        System.out.println("|| ==================================================== ||");
        System.out.println("|| Teamspeak Query Plugin Framework                     ||");
        System.out.println("|| https://projects.vortexdata.net/tsq-plugin-framework ||");
        System.out.println("||                                                      ||");
        System.out.println("|| Support: support@vortexdata.net                      ||");
        System.out.println("|| Authors: Michael Wiesinger, Sandro Kierner           ||");
        System.out.println("|| Publisher: VortexdataNET                             ||");
        System.out.println("|| Copyright: Copyright (C) 2019 VortexdataNET          ||");
        System.out.println("|| ==================================================== ||");
        System.out.println();

        // Sleep for 1 second
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // Ignore
        }

        System.out.println("Loading libraries... Please wait.");

        // Init BootHandler
        BootHandler bootHandler = new BootHandler();
        bootHandler.setBootStartTime();

        // Init Logger
        logger = new FrameworkLogger(this);
        logger.printInfo("Initializing... Please wait.");

        // Load main config
        ConfigMain configMain = new ConfigMain();
        logger.printDebug("Loading main config...");
        boolean didConfigExist = configMain.load();
        if (!didConfigExist) {
            logger.printWarn("Could not find config file, therefor created a new one. Please review and adjust its values to avoid any issues.");
            shutdown(false);
        }
        logger.printDebug("Main config loaded.");

        // Create config
        final TS3Config config = new TS3Config();
        logger.printDebug("Trying to assign server address...");
        config.setHost(configMain.getProperty("serverAddress"));
        logger.printDebug("Server address set.");

        // Set Reconnect Strategy
        logger.printDebug("Trying to set reconnect strategy...");
        String reconnectStrategy = configMain.getProperty("reconnectStrategy");
        if (reconnectStrategy.equalsIgnoreCase("exponentialBackoff") || reconnectStrategy.equalsIgnoreCase("") || reconnectStrategy.isEmpty()) {
            config.setReconnectStrategy(ReconnectStrategy.exponentialBackoff());
        } else if (reconnectStrategy.equalsIgnoreCase("disconnect")) {
            config.setReconnectStrategy(ReconnectStrategy.disconnect());
        } else if (reconnectStrategy.equalsIgnoreCase("linearBackoff")) {
            config.setReconnectStrategy(ReconnectStrategy.linearBackoff());
        } else if (reconnectStrategy.equalsIgnoreCase("userControlled")) {
            logger.printWarn("UserControlled reconnect strategy is currently not supported, reverting to disconnect. You will have to manually restart the framework after a timeout.");
            config.setReconnectStrategy(ReconnectStrategy.disconnect());
        } else {
            logger.printWarn("Could not identify reconnect strategy " + reconnectStrategy + ", falling back to exponentialBackoff.");
            config.setReconnectStrategy(ReconnectStrategy.exponentialBackoff());
        }
        logger.printDebug("Reconnect strategy set.");

        config.setConnectionHandler(new ConnectionHandler() {

            @Override
            public void onConnect(TS3Query ts3Query) {
                api = ts3Query.getApi();
                connect(configMain, ts3Query);
            }

            @Override
            public void onDisconnect(TS3Query ts3Query) {
                // Nothing
            }

        });

        // Create query
        logger.printDebug("Trying to connect to server...");

        final TS3Query query = new TS3Query(config);

        try {
            query.connect();
        } catch (Exception e) {
            logger.printError("Connection to server failed, dumping error details: ", e);
            System.exit(0);
        }

        logger.printInfo("Successfully established connection to server.");

        logger.printDebug("Initializing console handler...");
        consoleHandler = new ConsoleHandler(logger, rootLogger, Level.DEBUG);
        logger.printDebug("Console handler loaded.");
        logger.printDebug("Registering console commands...");
        consoleHandler.registerCommand(new CommandHelp(logger, consoleHandler));
        consoleHandler.registerCommand(new CommandStop(logger, this));
        consoleHandler.registerCommand(new CommandClear(logger));
        consoleHandler.registerCommand(new CommandLogout(logger, consoleHandler));
        consoleHandler.registerCommand(new CommandAddUser(logger, consoleHandler));
        logger.printDebug("Console handler and console commands successfully initialized and registered.");


        // Load modules

        logger.printDebug("Initializing plugin controller...");
        pluginManager = new PluginManager(this);
        logger.printDebug("Loading and enabling plugins...");
        pluginManager.enableAll();
        logger.printDebug("Successfully loaded plugins.");
        bootHandler.setBootEndTime();

        logger.printInfo("Boot process finished.");
        logger.printInfo("It took " + bootHandler.getBootTime() + " milliseconds to start the framework and load plugins.");

        consoleHandler.start();

    }

    public void shutdown(boolean isManagerEnabled) {
        logger.printInfo("Shutting down for system halt.");
        logger.printDebug("Shutting down console handler...");
        consoleHandler.shutdown();
        logger.printInfo("Unloading plugins...");
        if (isManagerEnabled)
            pluginManager.disableAll();
        logger.printInfo("Successfully unloaded plugins and disabled console handler.");
        logger.printInfo("Ending framework logging...");
        System.exit(0);
    }

    public TS3Query connect(ConfigMain configMain, TS3Query query) {
        api = query.getApi();
        try {
            logger.printDebug("Trying to sign into query...");
            api.login(configMain.getProperty("queryUser"), configMain.getProperty("queryPassword"));
            logger.printInfo("Successfully signed into query.");
        } catch (Exception e) {
            logger.printError("Failed to sign into query, dumping error details: ", e);
            System.exit(0);
        }

        // Select virtual host
        logger.printDebug("Trying to select virtual server...");
        try {
            api.selectVirtualServerById(Integer.parseInt(configMain.getProperty("virtualServerId")));
            logger.printInfo("Successfully selected virtual server.");
        } catch (Exception e) {
            logger.printError("Failed to select virtual server, dumping error details: ", e);
            System.exit(0);
        }

        try {
            logger.printDebug("Trying to assign nickname...");
            api.setNickname(configMain.getProperty("clientNickname"));
            logger.printDebug("Successfully set nickname.");
        } catch (Exception e) {
            logger.printError("Failed to set nickname, dumping error details: ", e);
            System.exit(0);
        }

        chatCommandListener = new ChatCommandListener(this, configMain);

        logger.printDebug("Trying to register global events...");
        api.registerAllEvents();
        api.addTS3Listeners(new GlobalEventHandler(this));
        logger.printDebug("Successfully registered global events.");

        connectionListener = new ConnectionListener(logger);
        connectionListener.start();


        return query;
    }

    public ConsoleHandler getConsoleHandler() {
        return consoleHandler;
    }

    public ChatCommandListener getChatCommandListener() {
        return chatCommandListener;
    }

    public TS3Api getApi() {
        return api;
    }

    public net.vortexdata.tsqpf.console.Logger getLogger() {
        return logger;
    }

    public Logger getRootLogger() {
        return rootLogger;
    }


    public void addEventHandler(TS3Listener listener) {
        api.addTS3Listeners(listener);
    }
}
