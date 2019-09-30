package net.vortexdata.tsqpf;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.TS3Listener;
import com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;
import net.vortexdata.tsqpf.commands.*;
import net.vortexdata.tsqpf.configs.ConfigMain;
import net.vortexdata.tsqpf.console.FrameworkLogger;
import net.vortexdata.tsqpf.console.ConsoleHandler;
import net.vortexdata.tsqpf.installers.*;
import net.vortexdata.tsqpf.listeners.ChatCommandListener;
import net.vortexdata.tsqpf.listeners.GlobalEventHandler;
import net.vortexdata.tsqpf.modules.*;
import org.apache.log4j.*;

import java.text.*;
import java.util.*;


public class Framework {

    private static Framework _instance;
    private TS3Api _api;
    private ConsoleHandler _consoleHandler;
    private ChatCommandListener _ChatCommandListener;
    private PluginManager _manager;
    private net.vortexdata.tsqpf.console.Logger logger;

    private static final Logger rootLogger = LogManager.getRootLogger();

    public static void main(String[] args) {

        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("-debug")) {
                rootLogger.setLevel(Level.DEBUG);
            } else if (args[i].contains("-setup")) {
                InstallWizzard installWizzard = new InstallWizzard();
                installWizzard.init();
            }
        }

        _instance = new Framework();
        _instance.init();
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
        System.out.println("");

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
        if (reconnectStrategy.equalsIgnoreCase("exponentialBackoff") || reconnectStrategy.equalsIgnoreCase("") ||reconnectStrategy.isEmpty()) {
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
                _api = ts3Query.getApi();
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
        _consoleHandler = new ConsoleHandler();
        logger.printDebug("Console handler loaded.");
        logger.printDebug("Registering console commands...");
        _consoleHandler.registerCommand(new CommandHelp(logger, getConsoleHandler()));
        _consoleHandler.registerCommand(new CommandStop(logger, this));
        logger.printDebug("Console handler and console commands successfully initialized and registered.");


        // Load modules

        logger.printDebug("Initializing plugin controller...");
        _manager = new PluginManager(this);
        logger.printDebug("Loading and enabling plugins...");
        _manager.enableAll();
        logger.printDebug("Successfully loaded plugins.");
        bootHandler.setBootEndTime();

        logger.printInfo("Boot process finished.");
        logger.printInfo("It took " + bootHandler.getBootTime() + " milliseconds to start the framework and load plugins.");

        _consoleHandler.start();

    }

    public void shutdown(boolean isManagerEnabled) {
        logger.printInfo("Shutting down for system halt.");
        logger.printInfo("Unloading plugins...");
        if (isManagerEnabled)
            _manager.disableAll();
        logger.printInfo("Successfully unloaded plugins.");
        logger.printInfo("Ending framework logging...");
        System.exit(0);
    }

    public TS3Query connect(ConfigMain configMain, TS3Query query) {
        _api = query.getApi();
        try {
            logger.printDebug("Trying to sign into query...");
            _api.login(configMain.getProperty("queryUser"), configMain.getProperty("queryPassword"));
            logger.printInfo("Successfully signed into query.");
        } catch (Exception e) {
            logger.printError("Failed to sign into query, dumping error details: ", e);
            System.exit(0);
        }

        // Select virtual host
        logger.printDebug("Trying to select virtual server...");
        try {
            _api.selectVirtualServerById(Integer.parseInt(configMain.getProperty("virtualServerId")));
            logger.printInfo("Successfully selected virtual server.");
        } catch (Exception e) {
            logger.printError("Failed to select virtual server, dumping error details: ", e);
            System.exit(0);
        }

        try {
            logger.printDebug("Trying to assign nickname...");
            _api.setNickname(configMain.getProperty("clientNickname"));
            logger.printDebug("Successfully set nickname.");
        } catch (Exception e) {
            logger.printError("Failed to set nickname, dumping error details: ", e);
            System.exit(0);
        }

        _ChatCommandListener = new ChatCommandListener(this, configMain);

        logger.printDebug("Trying to register global events...");
        _api.registerAllEvents();
        _api.addTS3Listeners(new GlobalEventHandler(this));
        logger.printDebug("Successfully registered global events.");

        return query;
    }

    public ConsoleHandler getConsoleHandler() {
        return _consoleHandler;
    }
    public ChatCommandListener getChatCommandListener() {
        return _ChatCommandListener;
    }

    public TS3Api getApi() {
        return _api;
    }

    public net.vortexdata.tsqpf.console.Logger getLogger() {
        return logger;
    }
    public Logger getRootLogger() {
        return rootLogger;
    }



    public void addEventHandler(TS3Listener listener) {
        _api.addTS3Listeners(listener);
    }
}
