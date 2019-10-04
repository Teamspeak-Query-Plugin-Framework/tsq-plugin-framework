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
import net.vortexdata.tsqpf.console.ConsoleTerminal;
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
 *
 * @since 1.0.0
 */
public class Framework {

    private TS3Config config;
    private static final Logger rootLogger = LogManager.getRootLogger();
    private static Framework instance;
    private TS3Api api;
    private ConsoleHandler consoleHandler;
    private ChatCommandListener chatCommandListener;
    private PluginManager pluginManager;
    private net.vortexdata.tsqpf.console.Logger logger;
    private ConnectionListener connectionListener;

    public static void main(String[] args) {
        instance = new Framework();
        instance.init();
    }

    public void init() {
        // Init BootHandler
        BootHandler bootHandler = new BootHandler();
        bootHandler.setBootStartTime();

        // Load main config
        ConfigMain configMain = new ConfigMain();
        logger.printDebug("Loading main config...");
        boolean didConfigExist = configMain.load();
        if (!didConfigExist) {
            logger.printWarn("Could not find config file, therefor created a new one. Please review and adjust its values to avoid any issues.");
            shutdown(false);
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
                wake(configMain, ts3Query);
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
            shutdown(true);
        }


    }

    public void shutdown() {
        logger.printInfo("Shutting down for system halt.");
        logger.printDebug("Shutting down console handler...");
        if (consoleHandler != null)
            consoleHandler.shutdown();
        else
            logger.printDebug("Console handler was not initialized, there was not needed to be unloaded.");
        if (pluginManager != null) {
            logger.printInfo("Unloading plugins...");
            pluginManager.disableAll();
        }
        logger.printInfo("Successfully unloaded plugins and disabled console handler.");
        logger.printInfo("Ending framework logging...");
        System.exit(0);
    }

    public void sleep() {
        logger.printDebug("Sleep initiated.");
        pluginManager.disableAll();
    }

    public TS3Query wake(ConfigMain configMain, TS3Query query) {

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

        chatCommandListener = new ChatCommandListener(this, configMain);

        logger.printDebug("Trying to register global events...");
        api.registerAllEvents();
        api.addTS3Listeners(new GlobalEventHandler(this));
        logger.printDebug("Successfully registered global events.");


        logger.printDebug("Initializing plugin controller...");
        pluginManager = new PluginManager(this);
        logger.printDebug("Loading and enabling plugins...");
        pluginManager.enableAll();
        logger.printDebug("Successfully loaded plugins.");


        return query;

    }

    public void evaluateArgs(String[] args) {

        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("-debug")) {
                rootLogger.setLevel(Level.DEBUG);
            } else if (args[i].contains("-setup")) {
                InstallWizzard installWizzard = new InstallWizzard();
                installWizzard.init();
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

}
