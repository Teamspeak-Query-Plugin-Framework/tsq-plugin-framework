package net.vortexdata.tsqpf;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.TS3Listener;
import com.github.theholywaffle.teamspeak3.api.reconnect.ConnectionHandler;
import com.github.theholywaffle.teamspeak3.api.reconnect.ReconnectStrategy;
import net.vortexdata.tsqpf.authenticator.UserManager;
import net.vortexdata.tsqpf.commands.*;
import net.vortexdata.tsqpf.configs.ConfigMain;
import net.vortexdata.tsqpf.configs.ConfigMessages;
import net.vortexdata.tsqpf.console.CommandContainer;
import net.vortexdata.tsqpf.console.FrameworkLogger;
import net.vortexdata.tsqpf.console.LocalShell;
import net.vortexdata.tsqpf.framework.FrameworkStatus;
import net.vortexdata.tsqpf.heartbeat.HeartBeatListener;
import net.vortexdata.tsqpf.listeners.ChatCommandListener;
import net.vortexdata.tsqpf.listeners.GlobalEventHandler;
import net.vortexdata.tsqpf.modules.BootHandler;
import net.vortexdata.tsqpf.plugins.PluginManager;
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
 * @since 1.0.0
 */
public class Framework {

    private static final Logger rootLogger = LogManager.getRootLogger();
    private static Framework instance;
    private TS3Config config;
    private TS3Api api;
    private LocalShell localShell;
    private UserManager userManager;
    private ChatCommandListener chatCommandListener;
    private PluginManager pluginManager;
    private net.vortexdata.tsqpf.console.Logger logger;
    private ConnectionListener connectionListener;
    private ReconnectStrategy reconnectStrategy;
    private boolean resetRoot = false;
    private FrameworkStatus frameworkStatus;

    public static void main(String[] args) {
        instance = new Framework();
        instance.init(args);
    }

    public static Framework getInstance() {
        return instance;
    }

    public void init(String[] args) {

        frameworkStatus = FrameworkStatus.STARTING;
        evaluateArgs(args);
        printCopyHeader();
        logger = new FrameworkLogger(this);

        // Init BootHandler
        BootHandler bootHandler = new BootHandler();
        bootHandler.setBootStartTime();

        // Load main config
        ConfigMain configMain = new ConfigMain();
        ConfigMessages configMessages = new ConfigMessages();
        logger.printDebug("Loading configs...");
        boolean didConfigMainExist = configMain.load();
        boolean didConfigMessagesExist = configMessages.load();
        if (!didConfigMessagesExist)
            logger.printWarn("Could not find message config file, therefor created a new one. You might want to review its values.");
        if (!didConfigMainExist) {
            logger.printWarn("Could not find config file, therefor created a new one. Please review and adjust its values to avoid any issues.");
            shutdown();
        }
        logger.printDebug("Main config loaded.");

        config = new TS3Config();
        logger.printDebug("Trying to assign server address...");
        config.setHost(configMain.getProperty("serverAddress"));
        logger.printDebug("Server address set.");

        // Set Reconnect Strategy
        logger.printDebug("Trying to set reconnect strategy...");
        String reconnectStrategy = configMain.getProperty("reconnectStrategy");
        if (reconnectStrategy.equalsIgnoreCase("exponentialBackoff") || reconnectStrategy.equalsIgnoreCase("") || reconnectStrategy.isEmpty()) {
            config.setReconnectStrategy(ReconnectStrategy.exponentialBackoff());
            this.reconnectStrategy = ReconnectStrategy.exponentialBackoff();
        } else if (reconnectStrategy.equalsIgnoreCase("disconnect")) {
            config.setReconnectStrategy(ReconnectStrategy.disconnect());
            this.reconnectStrategy = ReconnectStrategy.disconnect();
        } else if (reconnectStrategy.equalsIgnoreCase("linearBackoff")) {
            config.setReconnectStrategy(ReconnectStrategy.linearBackoff());
            this.reconnectStrategy = ReconnectStrategy.linearBackoff();
        } else if (reconnectStrategy.equalsIgnoreCase("userControlled")) {
            logger.printWarn("UserControlled reconnect strategy is currently not supported, reverting to disconnect. You will have to manually restart the framework after a timeout.");
            config.setReconnectStrategy(ReconnectStrategy.disconnect());
            this.reconnectStrategy = ReconnectStrategy.disconnect();
        } else {
            logger.printWarn("Could not identify reconnect strategy " + reconnectStrategy + ", falling back to exponentialBackoff.");
            config.setReconnectStrategy(ReconnectStrategy.exponentialBackoff());
            this.reconnectStrategy = ReconnectStrategy.exponentialBackoff();
        }
        logger.printDebug("Reconnect strategy set.");

        config.setConnectionHandler(new ConnectionHandler() {

            @Override
            public void onConnect(TS3Query ts3Query) {
                api = ts3Query.getApi();
                wake(configMain, configMessages, ts3Query);
            }

            @Override
            public void onDisconnect(TS3Query ts3Query) {
                sleep();
            }

        });

        // Create query
        logger.printDebug("Trying to connect to server...");

        final TS3Query query = new TS3Query(config);

        try {
            query.connect();
        } catch (Exception e) {
            logger.printError("Connection to server failed, dumping error details: ", e);
            shutdown();
        }

        //chatCommandListener = new ChatCommandListener(this, configMessages);

        logger.printInfo("Successfully established connection to server.");

        logger.printDebug("Initializing console handler...");
        userManager = new UserManager(this.logger);
        localShell = new LocalShell(logger, resetRoot);
        logger.printDebug("Console handler loaded.");
        logger.printDebug("Registering console commands...");

        CommandContainer.registerCommand(new CommandHelp(logger));
        CommandContainer.registerCommand(new CommandStop(logger, this));
        CommandContainer.registerCommand(new CommandClear(logger));
        CommandContainer.registerCommand(new CommandLogout(logger));
        CommandContainer.registerCommand(new CommandAddUser(logger, userManager));
        CommandContainer.registerCommand(new CommandDelUser(logger, userManager));
        CommandContainer.registerCommand(new CommandFramework(logger, this));
        logger.printDebug("Console handler and console commands successfully initialized and registered.");

        if (configMain.getProperty("enableRemoteShell").equalsIgnoreCase("true")) {
            logger.printDebug("Opening remote shell port...");
            int shellPort;
            try {
                shellPort = Integer.parseInt(configMain.getProperty("remoteShellPort"));

                connectionListener = new ConnectionListener(logger, shellPort);
                connectionListener.start();
            } catch (Exception e) {
                logger.printError("Failed to parse shell port value, falling back to default.");
                shellPort = Integer.parseInt(configMain.getDefaultProperty("remoteShellPort"));

                connectionListener = new ConnectionListener(logger, shellPort);
                connectionListener.start();
            }
        } else {
            logger.printDebug("Skipping opening of remote shell port as defined per config.");
        }

        HeartBeatListener heartBeatListener;
        if (configMain.getProperty("enableHeartbeat").equalsIgnoreCase("true")) {
            logger.printDebug("Opening heartbeat port...");
            try {
                int port = Integer.parseInt(configMain.getProperty("heartbeatPort"));
                heartBeatListener = new HeartBeatListener(api, port);
            } catch (Exception e) {
                logger.printWarn("Failed to parse heartbeat port, reverting to default value.");
                heartBeatListener = new HeartBeatListener(api, Integer.parseInt(configMain.getDefaultProperty("heartbeatPort")));
            }
        } else {
            logger.printDebug("Skipping opening of heartbeat port as defined per config.");
        }

        bootHandler.setBootEndTime();
        logger.printInfo("Boot process finished.");
        logger.printInfo("It took " + bootHandler.getBootTime() + " milliseconds to start the framework and load plugins.");
        bootHandler = null;

        localShell.start();

    }

    public void shutdown() {
        logger.printInfo("Shutting down for system halt.");

        if (connectionListener != null) {
            logger.printDebug("Shutting down shell connection listener...");
            connectionListener.stop();
        }

        if (localShell != null) {
            logger.printDebug("Shutting down console handler...");
            localShell.shutdown();
        }


        if (pluginManager != null) {
            logger.printInfo("Unloading plugins...");
            pluginManager.disableAll();
        }

        logger.printInfo("Successfully unloaded plugins and disabled console handler.");
        logger.printInfo("Ending framework logging...");
        System.exit(0);
    }

    public void sleep() {
        if (this.reconnectStrategy == ReconnectStrategy.disconnect()) {
            shutdown();
            return;
        }
        logger.printDebug("Sleep initiated.");
        logger.printDebug("Disabling all plugins...");
        pluginManager.disableAll();
        logger.printDebug("All plugins disabled.");
    }

    public TS3Query wake(ConfigMain configMain, ConfigMessages configMessages, TS3Query query) {

        frameworkStatus = FrameworkStatus.WAKING;
        logger.printDebug("Wakeup initiated.");
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

        // Old Listeners and Handlers could cause unwanted side effects when reconnecting.

        logger.printDebug("Starting up ChatCommandListener.");
        //TODO: Implement reuseable ChatCommandListener
        chatCommandListener = new ChatCommandListener(this, configMessages);

        logger.printDebug("Trying to register global events...");
        api.registerAllEvents();
        //TODO: Implement reuseable GlobalEventHandler
        api.addTS3Listeners(new GlobalEventHandler(this));
        logger.printDebug("Successfully registered global events.");


        logger.printDebug("Initializing plugin controller...");
        //TODO: Implement reuseable PluginManager
        pluginManager = new PluginManager(this);
        logger.printDebug("Loading and enabling plugins...");
        pluginManager.enableAll();
        logger.printDebug("Successfully loaded plugins.");

        frameworkStatus = FrameworkStatus.RUNNING;
        return query;

    }

    public void evaluateArgs(String[] args) {

        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("-debug")) {
                rootLogger.setLevel(Level.DEBUG);
            } else if (args[i].contains("-setup")) {
                System.out.println("Setup wizard is not supported in this build.");
                System.exit(0);
            } else if (args[i].contains("-reset-root")) {
                resetRoot = true;
            }
        }

    }

    public void printCopyHeader() {
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
    }

    public LocalShell getLocalShell() {
        return localShell;
    }

    public UserManager getUserManager() {
        return userManager;
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

    public void reload() {
        frameworkStatus = FrameworkStatus.RELOADING;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }
}
