package net.vortexdata.tsManagementBot;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.event.TS3Listener;
import com.github.theholywaffle.teamspeak3.api.exception.*;
import net.vortexdata.tsManagementBot.configs.ConfigMain;
import net.vortexdata.tsManagementBot.installers.*;
import net.vortexdata.tsManagementBot.listeners.GlobalEventHandler;
import net.vortexdata.tsManagementBot.modules.PluginManager;
import org.apache.log4j.*;

public class Bot {

    private static Bot _instance;
    private TS3Api _api;
    private static final Logger logger = LogManager.getRootLogger();


    public static void main(String[] args) {

        for (int i = 0; i < args.length; i++) {
            if (args[i].contains("-debug")) {
                logger.setLevel(Level.DEBUG);
            } else if (args[i].contains("-setup")) {
                InstallWizzard installWizzard = new InstallWizzard();
            }
        }

        _instance = new Bot();
        _instance.init();
    }

    private void init() {

        // Copy Header
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
        System.out.println("Loading libraries... Please wait.");
        logger.info("Initializing... Please wait.");

        PluginManager manager = new PluginManager(this);
        manager.enableAll();



        // Load main config
        ConfigMain configMain = new ConfigMain();
        logger.debug("Loading main config...");
        configMain.load();
        logger.debug("Bot config loaded.");

        // Create config
        final TS3Config config = new TS3Config();
        logger.debug("Trying to set server address...");
        config.setHost(configMain.getProperty("serverAddress"));
        logger.debug("Server address set.");
        // Create query
        final TS3Query query = new TS3Query(config);
        logger.debug("Trying to connect to server...");
        try {
            query.connect();
        } catch (Exception e) {
            logger.error("Connection to server failed, dumping error information.", e);
            System.exit(0);
        }
        logger.info("Successfully established connection to server.");
        _api = query.getApi();
        try {
            logger.debug("Trying to sign into query...");
            _api.login(configMain.getProperty("queryUser"), configMain.getProperty("queryPassword"));
            logger.info("Successfully signed into query.");
        } catch (TS3ConnectionFailedException e) {
            logger.error("Failed to sign into query, dumping error information.", e);
            System.exit(0);
        }

        // Select virtual host
        logger.debug("Trying to select virtual server...");
        try {
            _api.selectVirtualServerById(Integer.parseInt(configMain.getProperty("virtualServer")));
            logger.info("Successfully selected virtual server.");
        } catch (Exception e) {
            logger.error("Failed to select virtual server, dumping error details.", e);
            System.exit(0);
        }

        try {
            logger.debug("Trying to set nickname...");
            _api.setNickname(configMain.getProperty("botNickname"));
            logger.debug("Successfully set nickname.");
        } catch (Exception e) {
            logger.error("Failed to set nickname, dumping error details.", e);
            System.exit(0);
        }

        logger.debug("Trying to register events...");
        _api.registerAllEvents();
        _api.addTS3Listeners(new GlobalEventHandler(this));

        // Load modules


        logger.info("Boot process finished.");
    }


    public TS3Api getApi() {
        return _api;
    }

    public Logger getLogger() {
        return logger;
    }

    public void addEventHandler(TS3Listener listener) {
        _api.addTS3Listeners(listener);

    }
}
