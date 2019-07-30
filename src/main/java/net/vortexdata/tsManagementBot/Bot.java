package net.vortexdata.tsManagementBot;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.TS3Listener;
import com.github.theholywaffle.teamspeak3.api.exception.*;
import net.vortexdata.tsManagementBot.commands.*;
import net.vortexdata.tsManagementBot.configs.ConfigMain;
import net.vortexdata.tsManagementBot.console.BotLogger;
import net.vortexdata.tsManagementBot.console.ConsoleHandler;
import net.vortexdata.tsManagementBot.installers.*;
import net.vortexdata.tsManagementBot.listeners.GlobalEventHandler;
import net.vortexdata.tsManagementBot.modules.PluginManager;
import org.apache.log4j.*;

import java.text.*;
import java.util.*;


public class Bot {

    private static Bot _instance;
    private TS3Api _api;
    private ConsoleHandler _consoleHandler;
    private PluginManager _manager;
    private net.vortexdata.tsManagementBot.console.Logger logger;

    private static final Logger rootLogger = LogManager.getRootLogger();

    static {
        // Custom Property for Logger File Appender
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        System.setProperty("current.date.time", dateFormat.format(new Date()));
    }

    public static void main(String[] args) {


        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("-printDebug")) {
                rootLogger.setLevel(Level.DEBUG);
            } else if (args[i].contains("-setup")) {
                InstallWizzard installWizzard = new InstallWizzard();
                installWizzard.init();
            }
        }

        _instance = new Bot();
        _instance.init();
    }

    private void init() {

        // Display Copy Header & wait 1 seconds
        System.out.println("|| =============================================== ||");
        System.out.println("|| Copyright (C) 2018 - 2019 VortexdataNET         ||");
        System.out.println("|| https://projects.vortexdata.net/tsservermanager ||");
        System.out.println("|| =============================================== ||");
        System.out.println("");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // Ignore
        }
        logger = new BotLogger(this);
        
        
        System.out.println("Loading libraries... Please wait.");
        logger.printToConsole("Initializing... Please wait.");

        // Load main config
        ConfigMain configMain = new ConfigMain();
        logger.printDebug("Loading main config...");
        configMain.load();
        logger.printDebug("Bot config loaded.");

        // Create config
        final TS3Config config = new TS3Config();
        logger.printDebug("Trying to set server address...");
        config.setHost(configMain.getProperty("serverAddress"));
        logger.printDebug("Server address set.");
        // Create query
        final TS3Query query = new TS3Query(config);
        logger.printDebug("Trying to connect to server...");
        try {
            query.connect();
        } catch (Exception e) {
            logger.printError("Connection to server failed, dumping printError printToConsolermation.", e);
            System.exit(0);
        }
        logger.printToConsole("Successfully established connection to server.");
        _api = query.getApi();
        try {
            logger.printDebug("Trying to sign into query...");
            _api.login(configMain.getProperty("queryUser"), configMain.getProperty("queryPassword"));
            logger.printToConsole("Successfully signed into query.");
        } catch (Exception e) {
            logger.printError("Failed to sign into query, dumping printError printToConsolermation.", e);
            System.exit(0);
        }

        // Select virtual host
        logger.printDebug("Trying to select virtual server...");
        try {
            _api.selectVirtualServerById(Integer.parseInt(configMain.getProperty("virtualServer")));
            logger.printToConsole("Successfully selected virtual server.");
        } catch (Exception e) {
            logger.printError("Failed to select virtual server, dumping printError details.", e);
            System.exit(0);
        }

        try {
            logger.printDebug("Trying to set nickname...");
            _api.setNickname(configMain.getProperty("botNickname"));
            logger.printDebug("Successfully set nickname.");
        } catch (Exception e) {
            logger.printError("Failed to set nickname, dumping printError details.", e);
            System.exit(0);
        }

        logger.printDebug("Trying to register events...");
        _api.registerAllEvents();
        _api.addTS3Listeners(new GlobalEventHandler(this));


        _consoleHandler = new ConsoleHandler();
        _consoleHandler.registerCommand(new CommandHelp(logger, getConsoleHandler()));
        _consoleHandler.registerCommand(new CommandStop(logger, this));


        // Load modules

        logger.printDebug("Initializing plugin controller...");
        _manager = new PluginManager(this);
        logger.printToConsole("Enabling plugins...");
        _manager.enableAll();
        logger.printToConsole("Successfully loaded plugins.");

        logger.printToConsole("Boot process finished.");
        _consoleHandler.start();


    }

    public void shutdown() {
        logger.printToConsole("Unloading plugins...");
        _manager.disableAll();
        logger.printToConsole("Successfully unloaded plugins.");
        logger.printToConsole("Shutting down... Bye!");
        System.exit(0);
    }

    public ConsoleHandler getConsoleHandler() {
        return _consoleHandler;
    }

    public TS3Api getApi() {
        return _api;
    }

    public net.vortexdata.tsManagementBot.console.Logger getLogger() {
        return logger;
    }
    public Logger getRootLogger() {
        return rootLogger;
    }



    public void addEventHandler(TS3Listener listener) {
        _api.addTS3Listeners(listener);
    }
}
